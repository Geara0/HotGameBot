package HotGameParsingTests;

import HotGameParsing.HotGameParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HotGameParserTest {
    String link = "https://hot-game.info/game/Control-Ultimate-Edition";
    HotGameParser parser = new HotGameParser();

    @BeforeEach
    public void warmUp(){
        var r2 = parser.parseTitle(link);
    }

    @Test
    public void parseLink(){
        parser.parseTitle(link);
    }

    @Test
    public void parseLink100Time(){
        for (int i = 0; i < 100; i++)
            parser.parseTitle(link);
    }

    @Test
    public void parseLink100TimesAgain(){
        for (int i = 0; i < 100; i++)
            parser.parseTitle(link);
    }

    @Test
    public void getInfoByNameTest(){
        var titles = parser.parseTitle("control deluxe edition");
        for(var title : titles)
            System.out.println(title.getStringForm());
        System.out.println(parser.getReport());
    }
}
