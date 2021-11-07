package commandsTests;

import commands.ICommand;
import commands.UnsubscribeFromTitle;
import Entities.Title;
import Entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class UnsubscribeFromTitleTest {
    InputStream sysInBackup = System.in;
    ByteArrayInputStream input;

    @BeforeEach
    public void setUpInput(){
        input = new ByteArrayInputStream("2".getBytes());
        System.setIn(input);
    }

    @Test
    public void checkUnsubscribingWorks(){
        Title toUnsub = new Title("testTitle", "testLink", "testBuyLink",75377);
        Title anotherTitle = new Title("anotherTitleToTest", "anotherTestLink", "testBuyLink",7537);
        HashMap<String,Title> userMapping = new HashMap<>() {{
            put("anotherTitleToTest", anotherTitle);
            put("testTitle", toUnsub);
        }};
        User testUser = new User("testUser", userMapping);
        ICommand unsubCommand = new UnsubscribeFromTitle();
        unsubCommand.execute(testUser);
        Assertions.assertTrue(testUser.getTitles().containsKey(anotherTitle.getName()) &
                !testUser.getTitles().containsKey(toUnsub.getName()));
    }

    @AfterEach
    public void backup(){
        System.setIn(sysInBackup);
    }
}
