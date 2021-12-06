package HotGameParsingTests;

import HotGameParsing.HotGameParser;
import HotGameParsing.ReportState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;

public class HotGameParserTest {
    String link = "https://hot-game.info/game/Control-Ultimate-Edition";
    String goodName = "control";
    String badName = "control deluxe editiob";
    HotGameParser parser = new HotGameParser();

    @BeforeEach
    public void warmUp(){
        var r2 = parser.parseTitle(link);
    }

    @Test
    public void parseLink100Time(){
        Instant start = Instant.now();
        for (int i = 0; i < 100; i++)
            parser.parseTitle(link);
        Instant finish = Instant.now();
        System.out.println(Duration.between(start,finish).toMillis()/100.0);
    }

    @Test
    public void parseLink100TimesAgain(){
        Instant start = Instant.now();
        for (int i = 0; i < 100; i++)
            parser.parseTitle(link);
        Instant finish = Instant.now();
        System.out.println(Duration.between(start,finish).toMillis()/100.0);
    }

    @Test
    public void parseName100Times(){
        Instant start = Instant.now();
        for (int i = 0; i < 100; i++)
            parser.parseTitle(goodName);
        Instant finish = Instant.now();
        System.out.println(Duration.between(start,finish).toMillis()/100.0);
    }

    @Test
    public void parseName100TimesAgain(){
        Instant start = Instant.now();
        for (int i = 0; i < 100; i++)
            parser.parseTitle(goodName);
        Instant finish = Instant.now();
        System.out.println(Duration.between(start,finish).toMillis()/100.0);
    }

    @Test
    public void getTitleByBadNameTest(){
        var titles = parser.parseTitle("control deluxe editiob");
        Assertions.assertEquals(parser.getReport(), ReportState.BAD_NAME);
    }

    @Test
    public void getTitleByGoodNameTest(){
        var titles = parser.parseTitle("control deluxe edition");
        Assertions.assertEquals(parser.getReport(), ReportState.OK);
    }

    @Test
    public void getTitleByGoodLinkTest(){
        var title = parser.parseTitle("https://hot-game.info/game/Control");
        Assertions.assertEquals(parser.getReport(), ReportState.OK);
    }

    @Test
    public void getTitleByBadLinkTest(){
        var title = parser.parseTitle("https://hot-game.info/game/1");
        Assertions.assertEquals(parser.getReport(), ReportState.BAD_URL);
    }
}
