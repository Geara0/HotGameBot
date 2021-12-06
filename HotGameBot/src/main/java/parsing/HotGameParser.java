package parsing;

import entities.Title;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.xml.crypto.URIReferenceException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class HotGameParser implements IParser {
    /**
     * Поле с информацией о завершении процесса парсинга, хранит {@link ReportState}
     */
    private ReportState report;

    /**
     * Конструктор, устанавливает {@link HotGameParser#report} в начальное положение
     */
    public HotGameParser(){
        report=ReportState.DEFAULT;
    }

    /**
     * Геттер для {@link HotGameParser#report}
     * @return {@link HotGameParser#report}
     */
    public ReportState getReport(){
        return report;
    }

    /**
     * Метод осуществляющий парсинг тайтла с сайта Хот-Гейм
     * @param name текст, содержащий имя тайтла
     * @return список всех найденных по входным данным тайтлов
     */
    public ArrayList<Title> parseTitlesByName(String name) {
        if(name.contains("https://")){
            report = ReportState.BAD_NAME;
            return new ArrayList<>();
        }
        else return getTitlesByName(name);
    }

    @Override
    public String[] getRecommendations(String... params) {
        return new String[0];
    }

    public Title parseTitleByLink(String link){
        if(!link.contains("https://")){
            report = ReportState.BAD_URL;
            return new Title();
        }
        else return getTitleInfoSelect(link);
    }

    /**
     * Парсит первый блок результатов на странице поиска по name
     * @param name имя тайтла для поиска
     * @return список найденных тайтлов
     */
    private ArrayList<Title> getTitlesByName(String name) {
        ArrayList<Title> result = new ArrayList<>();
        String searchName = name.strip().toLowerCase(); //replaceAll("[^a-zA-Z]","");
        String searchUrl = "https://hot-game.info/q=".concat(searchName);
        try {
            Document doc = Jsoup.connect(searchUrl).get();
            Element searchResults = doc.selectFirst("body > div.container.content-container > section.yui3-cssreset.result-block.content-table");
            if(searchResults.child(0).className().equals("no-results"))
                throw new URIReferenceException();
            int childrenCount = searchResults.children().size();
            for (var i=0;i<childrenCount;i++) {
                var href = searchResults.child(i).selectFirst("a").attr("href");
                result.add(getTitleInfoSelect("https://hot-game.info".concat(href)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URIReferenceException e){
            report = ReportState.BAD_NAME;
        }
        setReportOK();
        return result;
    }

    private Title getTitleInfoSelect(String link) {
        Title result = new Title();
        try {
            Document doc = Jsoup.connect(link).get();
            var gameInfo = doc.selectFirst("body > div.container.content-container > section.game.clearfix > aside > div.hg-block.short-game-description > div");
            var name = gameInfo.selectFirst("div.game-title > span:nth-child(1)").text();
            var developer = gameInfo.selectFirst("div.game-developer > span").text();
            var publisher = gameInfo.selectFirst("div.game-publisher > span").text();
            var platforms = gameInfo.selectFirst("div.game-title > span.red").text().split(",");
            var releaseDate = gameInfo.selectFirst("div.game-release-date > span").text();
            var genres = gameInfo.selectFirst("div.game-genres").getElementsByAttributeValueStarting("class", "hidden-link genre").text().split(" ");
            var mode = gameInfo.selectFirst("div.game-genres > div > span:nth-child(1)").attributes().get("title");
            var bestMarket = doc.selectFirst("#prices_block > div.game-prices-wrap > div.game-prices-list.game-prices-new > div:nth-child(1)");
            var description = doc.selectFirst("body > div.container.content-container > section.game.clearfix > div.right-side > div.hg-block.description > div:nth-child(2)").text();
            var length = bestMarket.children().size();
            var price = length == 2 ? bestMarket.child(1).selectFirst("div > div.game-price"):
                    bestMarket.child(2).selectFirst("div > div.game-price");
            var bestLink = bestMarket.child(0).attributes().get("data-href");
            String bestPrice;
            if("нет в наличии".equals(price.text())){
                bestPrice = "0";
                bestLink+="\r\nСмотрите по ссылке чтобы узнать о наличии";
            } else{
                bestPrice = price.selectFirst("span").text();
            }
            var date = parseDate(releaseDate);
            var isMultiplayer = isMultiplayer(mode);
            result = new Title(name, link, bestLink, Integer.parseInt(bestPrice), publisher, developer, date, genres, isMultiplayer,description);
        } catch (IOException e) {
            report = ReportState.BAD_URL;
        } catch (NullPointerException | NumberFormatException e){
            //ignored
            //тайтлы на отсчете и дата "/" откуда-то
        }
        setReportOK();
        return result;
    }

    private Date parseDate(String date) {
        var splitDate = date.split(" ");
        var day = Integer.parseInt(splitDate[0]);
        var year = Integer.parseInt(splitDate[2].split("г")[0]);
        var month = switch (splitDate[1]) {
            case "янв." -> Calendar.JANUARY;
            case "февр." -> Calendar.FEBRUARY;
            case "март" -> Calendar.MARCH;
            case "апр." -> Calendar.APRIL;
            case "май" -> Calendar.MAY;
            case "июнь" -> Calendar.JUNE;
            case "июль" -> Calendar.JULY;
            case "авг." -> Calendar.AUGUST;
            case "сент." -> Calendar.SEPTEMBER;
            case "окт." -> Calendar.OCTOBER;
            case "нояб." -> Calendar.NOVEMBER;
            case "дек." -> Calendar.DECEMBER;
            default -> Calendar.UNDECIMBER;
        };
        return new GregorianCalendar(year, month, day).getTime();
    }

    private boolean isMultiplayer(String mode) {
        return !"Режим игры: singleplayer".equals(mode);
    }

    private void setReportOK(){
        report = report.equals(ReportState.DEFAULT) ? ReportState.OK : report;
    }
}
