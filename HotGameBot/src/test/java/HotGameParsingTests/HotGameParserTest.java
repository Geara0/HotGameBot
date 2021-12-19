package HotGameParsingTests;

import parsing.HotGameParser;
import parsing.IParser;
import parsing.ReportState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

public class HotGameParserTest {
    final String link = "https://hot-game.info/game/Control-AWE";
    final String goodName = "control awe";
    String badName = "control deluxe editiob";
    final IParser parser = new HotGameParser();

    @BeforeEach
    public void warmUp(){
        var r2 = parser.parseTitleByLink(link);
    }

    @Test
    public void parseLink100Time(){
        Instant start = Instant.now();
        for (int i = 0; i < 10; i++)
            parser.parseTitleByLink(link);
        Instant finish = Instant.now();
        System.out.println(Duration.between(start,finish).toMillis()/10.0);
    }

    @Test
    public void parseLink100TimesAgain(){
        Instant start = Instant.now();
        for (int i = 0; i < 100; i++)
            parser.parseTitleByLink(link);
        Instant finish = Instant.now();
        System.out.println(Duration.between(start,finish).toMillis()/100.0);
    }

    @Test
    public void parseName100Times(){
        Instant start = Instant.now();
        for (int i = 0; i < 100; i++)
            parser.parseTitlesByName(goodName);
        Instant finish = Instant.now();
        System.out.println(Duration.between(start,finish).toMillis()/100.0);
    }

    @Test
    public void parseName100TimesAgain(){
        Instant start = Instant.now();
        for (int i = 0; i < 100; i++)
            parser.parseTitlesByName(goodName);
        Instant finish = Instant.now();
        System.out.println(Duration.between(start,finish).toMillis()/100.0);
    }

    @Test
    public void getTitleByBadNameTest(){
        var titles = parser.parseTitlesByName(badName);
        Assertions.assertEquals(parser.getReport(), ReportState.BAD_NAME);
    }

    @Test
    public void getTitleByGoodNameTest(){
        var titles = parser.parseTitlesByName(goodName);
        Assertions.assertEquals(parser.getReport(), ReportState.OK);
    }

    @Test
    public void getTitleByGoodLinkTest(){
        var title = parser.parseTitleByLink("https://hot-game.info/game/Control");
        Assertions.assertEquals(parser.getReport(), ReportState.OK);
    }

    @Test
    public void getTitleByBadLinkTest(){
        var title = parser.parseTitleByLink("https://hot-game.info/game/1");
        Assertions.assertEquals(parser.getReport(), ReportState.BAD_URL);
    }
}
