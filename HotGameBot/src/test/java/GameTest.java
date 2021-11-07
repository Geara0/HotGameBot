import Entities.Game;
import Entities.Title;
import Entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

public class GameTest {
    @Test
    public void changeTitle(){
        var title1 = new Title("title1", "link1", "bLink1", 1);
        var title2 = new Title("title2", "link2", "bLink2", 2);
        var users = new HashSet<User>();
        for (int i = 0; i < 5; i++)
            users.add(new User(String.format("user%d", i), new HashMap<>()));

        var game = new Game(title1, users);
        Assertions.assertArrayEquals(game.getTitle().getName().toCharArray(), title1.getName().toCharArray());

        game.setTitle(title2);
        Assertions.assertArrayEquals(game.getTitle().getName().toCharArray(), title2.getName().toCharArray());
    }
}
