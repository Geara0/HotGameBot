package HotGameParsing;

import Entities.Title;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.format.datetime.joda.DateTimeParser;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class HotGameParser implements Parser {
    @Override
    public Title parseTitle(String link) {

        return null;
    }

    public String getJson(){
        return "";
    }

    public String getTitleInfo(String link){
        String result="";
        try {
            Document doc = Jsoup.connect(link).get();
            Element gameInfo = doc.getElementsByAttributeValue("class", "game-info").first();
            String titleName = gameInfo.getElementsByAttributeValue("itemprop","name").text();
            String[] redText = gameInfo.getElementsByAttributeValue("class","red").eachText().toArray(new String[]{});
            var publisher = redText[redText.length-1];
            var developer = redText[redText.length-2];
            var platforms = redText[0].split(",");
            var releaseDate = gameInfo.getElementsByAttributeValue("class","gray").text();
            var genres = gameInfo.getElementsByAttributeValueStarting("class","hidden-link genre").text().split(" ");
            var mode = gameInfo.getElementsByAttribute("title").first().attributes().get("title");
            var s = gameInfo.children();
            result = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getTitleInfoSelect(String link){
        String result;
        try {
            Document doc = Jsoup.connect(link).get();
            var gameInfo = doc.selectFirst("body > div.container.content-container > section.game.clearfix > aside > div.hg-block.short-game-description > div");
            var name = gameInfo.selectFirst("div.game-title > span:nth-child(1)").text();
            var developer = gameInfo.selectFirst("div.game-developer > span").text();
            var publisher = gameInfo.selectFirst("div.game-publisher > span").text();
            var platforms = gameInfo.selectFirst("div.game-title > span.red").text().split(",");
            var releaseDate = gameInfo.selectFirst("div.game-release-date > span").text();
            var genres = gameInfo.selectFirst("div.game-genres").getElementsByAttributeValueStarting("class","hidden-link genre").text().split(" ");
            var mode = gameInfo.selectFirst("div.game-genres > div > span:nth-child(1)").attributes().get("title");
            var bestMarket = doc.selectFirst("#prices_block > div.game-prices-wrap > div.game-prices-list.game-prices-new > div:nth-child(1)");
            var bestPrice = bestMarket.child(1).selectFirst("div.price-col-3 > div.game-price > span").text();
            var bestLink = bestMarket.child(0).attributes().get("data-href");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
