import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс с тестами для парсера тайтлов
 *
 * @author Вадим
 * @version 1.0
 */

public class TitleParserTest {
    /**
     * Объявление одинаковых для всех тестов переменных
     */
    String path;
    ArrayList<Title> actual;
    JsonParser parser = new TitleParser();

    /**
     * Тест на чтение одного файла, был нужен для самого начала, сейчас по сути бесполезен, потому что есть Multiple
     *
     * @throws IOException понятия не имею зачем, надо разобраться, но без него не компилит
     */
    @Test
    public void SingleTitle() throws IOException {
        ArrayList<Title> titles = new ArrayList<>();
        titles.add(new Title(
                "Battlefield V",
                "https://hot-game.info/game/Battlefield-V",
                "https://steam-account.ru/oplata/battlefield-5?ai=474161",
                85));
        path = ".\\JSONs\\TESTSingle";
        actual = parser.getData(path);
        Assertions.assertEquals(titles, actual);
    }

    /**
     * Тест на чтение именно коллекции, но делать expected я устал, поэтому коллекция только из двух
     *
     * @throws IOException - не понимаю зачем, но без него не работает
     */
    @Test
    public void MultipleTitles() throws IOException {
        ArrayList<Title> titles = new ArrayList<>();
        titles.add(new Title("Ghost of Tsushima - Director's Cut", "https://hot-game.info/game/Ghost-of-Tsushima-Directors-Cut", "https://store.playstation.com/ru-ru/product/EP9000-CUSA13323_00-GHOSTDIRECTORPS4", 4999));
        titles.add(new Title("Battlefield V", "https://hot-game.info/game/Battlefield-V", "https://steam-account.ru/oplata/battlefield-5?ai=474161", 85));
        path = ".\\JSONs\\TESTMultiple";
        actual = parser.getData(path);
        Assertions.assertArrayEquals(titles.toArray(), actual.toArray());
    }
}
