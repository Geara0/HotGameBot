import Entities.Title;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TitleTest {
    @Test
    void gettersTest() {
        var title = new Title("name", "link", "bLink", 0);
        Assertions.assertArrayEquals(title.getName().toCharArray(), "name".toCharArray());
        Assertions.assertArrayEquals(title.getLink().toCharArray(), "link".toCharArray());
        Assertions.assertArrayEquals(title.getBuyLink().toCharArray(), "bLink".toCharArray());
        Assertions.assertEquals(title.getPrice(), 0);

        Assertions.assertArrayEquals(title.toString().toCharArray(),
                "name Цена - 0\r\nbLink\r\n".toCharArray());
    }
}
