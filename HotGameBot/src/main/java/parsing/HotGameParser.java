package parsing;

import entities.Title;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.sql.rowset.serial.SerialBlob;
import javax.xml.crypto.URIReferenceException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


public class HotGameParser implements IParser {
    /**
     * Поле с информацией о завершении процесса парсинга, хранит {@link ReportState}
     */
    private ReportState report;

    public HotGameParser() {
        report = ReportState.INITIAL;
    }

    /**
     * Геттер для {@link HotGameParser#report}
     *
     * @return {@link HotGameParser#report}
     */
    public ReportState getReport() {
        return report;
    }

    /**
     * Реализация интерфейса для сайта hot-game.info
     *
     * @param name имя тайтла для поиска
     * @return - первая страница результатов поиска
     */
    @Override
    public ArrayList<Title> parseTitlesByName(String name) {
        report = ReportState.INITIAL;
        if (name.contains("https://")) {
            report = ReportState.BAD_NAME;
            return new ArrayList<>();
        }
        ArrayList<Title> result = new ArrayList<>();
        for (int i = 0; i < 5 && !report.equals(ReportState.OK); i++)
            result = getTitlesByRequest("q=".concat(name));
        return result;
    }

    /**
     * Реализация интерфейса
     *
     * @param params параметры для запроса рекомендаций
     * @return первые 5 тайтлов со страницы результатов
     */
    @Override
    public ArrayList<Title> getRecommendations(String... params) {
        report = ReportState.INITIAL;
        String parameters = String.join(";", params);
        ArrayList<Title> result = new ArrayList<>();
        for (int i = 0; i < 5 && !report.equals(ReportState.OK); i++)
            result = getTitlesByRequest(parameters);
        if (!report.equals(ReportState.OK)) report = ReportState.BAD_PARAMETERS;
        return (ArrayList<Title>) result.subList(0, 5);
    }

    /**
     * Реализация интерфейса
     *
     * @param link ссылка hot-game.info/game/* на тайтл
     * @return экземпляр тайтла, на который ведёт link
     */
    public Title parseTitleByLink(String link) {
        if (!link.contains("https://")) {
            report = ReportState.BAD_URL;
            return new Title();
        }
        Title result = new Title();
        for (int i = 0; i < 5 && !report.equals(ReportState.OK); i++)
            result = getTitleInfoSelect(link);
        return result;
    }

    /**
     * Парсит первый блок результатов на странице поиска по name
     *
     * @param request имя тайтла для поиска
     * @return список найденных тайтлов
     */
    private ArrayList<Title> getTitlesByRequest(String request) {
        ArrayList<Title> result = new ArrayList<>();
        String searchRequest = request.strip().toLowerCase(); //replaceAll("[^a-zA-Z]","");
        String domainUrl = "https://hot-game.info";
        String searchUrl = domainUrl.concat("/").concat(searchRequest);
        try {
            Document doc = Jsoup.connect(searchUrl).get();
            Element searchResults = doc.selectFirst("body > div.container.content-container > section.yui3-cssreset.result-block.content-table");
            if (searchResults.child(0).className().equals("no-results"))
                throw new URIReferenceException();
            int childrenCount = searchResults.children().size();
            for (var i = 0; i < childrenCount; i++) {
                String href = searchResults.child(i).selectFirst("a").attr("href");
                Title toAdd = getTitleInfoSelect(domainUrl.concat(href));
                if (toAdd.getName() != null)
                    result.add(toAdd);
            }
            setReportOK();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URIReferenceException e) {
            report = ReportState.BAD_NAME;
        }
        return result;
    }

    /**
     * Парсит страницу тайтла
     *
     * @param link страница для парсинга
     * @return экземпляр тайтла со страницы
     */
    private Title getTitleInfoSelect(String link) {
        Title result = new Title();
        try {
            Document doc = Jsoup.connect(link).get();
            Element gameInfo = doc.selectFirst("body > div.container.content-container > section.game.clearfix > aside > div.hg-block.short-game-description > div");
            String name = gameInfo.selectFirst("div.game-title > span:nth-child(1)").text();
            String developer = gameInfo.selectFirst("div.game-developer > span").text();
            String publisher = gameInfo.selectFirst("div.game-publisher > span").text();
            String[] platforms = gameInfo.selectFirst("div.game-title > span.red").text().split(",");
            String releaseDate = gameInfo.selectFirst("div.game-release-date > span").text();
            String[] genres = gameInfo.selectFirst("div.game-genres").getElementsByAttributeValueStarting("class", "hidden-link genre").text().split(" ");
            String mode = gameInfo.selectFirst("div.game-genres > div > span:nth-child(1)").attributes().get("title");
            Element bestMarket = doc.selectFirst("#prices_block > div.game-prices-wrap > div.game-prices-list.game-prices-new > div:nth-child(1)");
            String description = doc.selectFirst("body > div.container.content-container > section.game.clearfix > div.right-side > div.hg-block.description > div:nth-child(2)").text();
            int length = bestMarket.children().size();
            Element price = length == 2 ? bestMarket.child(1).selectFirst("div > div.game-price") :
                    bestMarket.child(2).selectFirst("div > div.game-price");
            String bestLink = bestMarket.child(0).attributes().get("data-href");
            String bestPrice = getBestPrice(price);
            bestLink = bestPrice.equals("0") ? bestLink.concat("\r\nСмотрите по ссылке чтобы узнать о наличии") : bestLink;
            Date date = parseDate(releaseDate);
            boolean isMultiplayer = isMultiplayer(mode);
            result = new Title(name, link, bestLink, Integer.parseInt(bestPrice), publisher, developer, date, genres, isMultiplayer, description, new SerialBlob(new byte[1]));
            setReportOK();
        } catch (IOException e) {
            report = ReportState.BAD_URL;
        } catch (NullPointerException | NumberFormatException e) {
            //ignored
            //тайтлы на отсчете и дата "/" откуда-то, будет обработано при переходе на апи
        } catch (SQLException e) {
            //idk what to do....
        }
        return result;
    }

    /**
     * Получает лучшую тайтла строкой
     *
     * @param price элемент содержащий цену тайтла
     * @return строку с ценой или 0 если "нет в наличии"
     */
    private String getBestPrice(Element price) {
        String bestPrice;

        if ("нет в наличии".equals(price.text())) {
            bestPrice = "0";
        } else {
            bestPrice = price.selectFirst("span").text();
        }
        return bestPrice;
    }

    /**
     * Метод для парсинга даты из формата hot-game.info
     *
     * @param date строковое представление даты со страницы тайтла
     * @return экземпляр Date соотетствующий date
     */
    private Date parseDate(String date) {
        String[] splitDate = date.split(" ");
        int day = Integer.parseInt(splitDate[0]);
        int year = Integer.parseInt(splitDate[2].split("г")[0]);
        int month = switch (splitDate[1]) {
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

    private void setReportOK() {
        report = report.equals(ReportState.INITIAL) ? ReportState.OK : report;
    }
}