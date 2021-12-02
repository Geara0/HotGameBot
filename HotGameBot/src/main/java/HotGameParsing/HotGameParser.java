package HotGameParsing;

import Entities.Title;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HotGameParser implements Parser {
    @Override
    public Title parseTitle(String link) {
        return getTitleInfoSelect(link);
    }

    public ArrayList<Title> getTitlesByName(String name) {
        ArrayList<Title> result = new ArrayList<>();
        String searchName = name.strip().toLowerCase();
        String searchUrl = "https://hot-game.info/q=".concat(searchName);
        try {
            Document doc = Jsoup.connect(searchUrl).get();
            Element searchResults = doc.selectFirst("body > div.container.content-container > section.yui3-cssreset.result-block.content-table");
            int childrenCount = searchResults.children().size();
            for (var i=0;i<childrenCount;i++) {
                var href = searchResults.child(i).selectFirst("a").attr("href");
                result.add(getTitleInfoSelect("https://hot-game.info".concat(href)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Title getTitleInfoSelect(String link) {
        Title result;
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
            var bestPrice = bestMarket.child(1).selectFirst("div.price-col-3 > div.game-price > span").text();
            var bestLink = bestMarket.child(0).attributes().get("data-href");
            var date = parseDate(releaseDate);
            var isMultiplayer = isMultiplayer(mode);
            result = new Title(name, link, bestLink, Integer.parseInt(bestPrice), publisher, developer, date, genres, isMultiplayer);
        } catch (IOException e) {
            System.out.println("error 404");
            result = new Title();
        }
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
}
