package HotGameParsingTests;

import HotGameParsing.HotGameParser;
import org.junit.jupiter.api.Test;

public class HotGameParserTest {
    String link = "https://hot-game.info/game/1";
    HotGameParser parser = new HotGameParser();

    @Test
    public void warmUp(){
        var r2 = parser.getTitleInfoSelect(link);
    }

    @Test
    public void getInfoSelect(){
        parser.getTitleInfoSelect(link);
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

    @Test
    public void getInfoByNameTest(){
        var title = parser.getTitlesByName("a");
    }
}
