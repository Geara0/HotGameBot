import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Тесты для parseUsers & parseUserData
 *
 * @author Geara0
 * @version 0.0
 * @see Main()
 */
public class ParsersTest {
    private static final String usersPath = ".\\JSONs\\HWUserSubsList";
    private static final String usersPathTest = ".\\JSONs\\Users";
    @Test
    public void parseUsers() throws InterruptedException {
        var a = 0;
        new Main();
        Thread.sleep( 10 * 1000);
    }

    @Test
    public void writingParsingTest() throws IOException {
        HashMap<String,User> userMap = new Gson().fromJson(Files.readString(Path.of(usersPath)), new TypeToken<HashMap<String, User>>() {}.getType());
        Main.writeUserSubs(userMap);
        HashMap<String,User> newUserMap = new Gson().fromJson(Files.readString(Path.of(usersPathTest)), new TypeToken<HashMap<String, User>>() {}.getType());
        boolean flag = true;
        for(var username : userMap.keySet())
            flag = userMap.get(username).equals(newUserMap.get(username));
        Assertions.assertTrue(flag);
    }
}
