import Entities.Title;
import Entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class UserTest {
    @Test
    void watchAndUnwatch() {
        var titles = new HashMap<String, Title>();
        Title someTitle = null;
        for (int i = 0; i < 5; i++) {
            var title = new Title(
                    String.format("name%d", i),
                    String.format("link%d", i),
                    String.format("bLink%d", i),
                    i);
            titles.put(String.format("title%d", i), title);
            if (i == 0) someTitle = title;
        }


        var user = new User("user", titles);
        Assertions.assertTrue(HashMapsEqual(user.getTitles(), titles));

        user.Unwatch(someTitle);
        titles.remove("title0");
        Assertions.assertTrue(HashMapsEqual(user.getTitles(), titles));

        user.Watch(someTitle);
        titles.put("title0", someTitle);
        Assertions.assertTrue(HashMapsEqual(user.getTitles(), titles));
    }

    @Test
    void activeTest() {
        var user = new User("user", new HashMap<>());
        Assertions.assertFalse(user.isActive());
        user.setActive();
        Assertions.assertTrue(user.isActive());
        user.setInactive();
        Assertions.assertFalse(user.isActive());
    }

    <K, V> boolean HashMapsEqual(HashMap<K, V> a, HashMap<K, V> b) {
        for (var pair : a.entrySet())
            if (!b.entrySet().contains(pair))
                return false;

        return true;
    }
}
