package commandsTests;

import commands.*;
import entities.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class SubscribeToTitleTest {
    InputStream sysInBackup = System.in;
    ByteArrayInputStream input;

    @BeforeEach
    public void setUpInput(){
        input = new ByteArrayInputStream("minecraft\r\nyes".getBytes());
        System.setIn(input);
    }

    @Test
    public void isSubscribingWorks(){
        Title testTitleOne = new Title("minecraft","testLink","testBuyLink",7327);
        Title testTitleTwo = new Title("also minecraft", "testLink", "testBuyLink",7327);
        User testUser = new User("testUser",new HashMap<>());

        HashMap<String,Title> testMapping = new HashMap<>();
        testMapping.put(testTitleOne.getName(), testTitleOne);
        testMapping.put(testTitleTwo.getName(),testTitleTwo);

        ICommand subscribeCommand = new SubscribeToTitleCommand(testMapping);
        subscribeCommand.execute(testUser);

        Assertions.assertTrue(testUser.getTitles().containsKey(testTitleOne.getName())
                & !testUser.getTitles().containsKey(testTitleTwo.getName()));
    }

    @AfterEach
    public void backup(){
        System.setIn(sysInBackup);
    }
}
