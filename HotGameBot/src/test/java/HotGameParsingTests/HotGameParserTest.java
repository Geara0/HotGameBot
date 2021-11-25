package HotGameParsingTests;

import HotGameParsing.HotGameParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HotGameParserTest {
    String link = "https://hot-game.info/game/Control-Ultimate-Edition";
    HotGameParser parser = new HotGameParser();

    @Test
    public void warmUp(){
        var result = parser.getTitleInfo(link);
        var r2 = parser.getTitleInfoSelect(link);
    }

    @Test
    public void getSomething() {
        parser.getTitleInfo(link);
    }

    @Test
    public void getInfoSelect(){
        parser.getTitleInfoSelect(link);
    }

    @Test
    public void parse100Times() {
        for (int i = 0; i < 100; i++) {
            parser.getTitleInfo(link);
        }
    }

    @Test
    public void parse100TimesAgain() {
        for (int i = 0; i < 100; i++)
            parser.getTitleInfo(link);
    }

    @Test
    public void parse100TimeSelect(){
        for (int i = 0; i < 100; i++)
            parser.getTitleInfoSelect(link);
    }

    @Test
    public void parse100TimeSelectAgain(){
        for (int i = 0; i < 100; i++)
            parser.getTitleInfoSelect(link);
    }
}
