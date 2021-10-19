import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Тесты для parseUsers & parseUserData
 *
 * @author Geara0
 * @version 0.0
 * @see Main#parseUsers(HashSet)
 * @see Main#parseUserData(Iterable)
 */
public class ParsersTest {
    private JsonParser parser = new UserSubsParser();

    @Test
    public void parseUsers() {

    }

    @Test
    public void parseUserData() throws IOException {
        var data = parser.getData(".\\JSONs\\HWUserSubsList");
        var a = Main.parseUserData(data);

    }
}
