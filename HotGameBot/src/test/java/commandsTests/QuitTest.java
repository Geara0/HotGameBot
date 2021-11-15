package commandsTests;

import commands.QuitCommand;
import Entities.User;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

public class QuitTest {
    private User testUser;
    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams(){
        System.setOut(new PrintStream(output));
    }

    @Test
    public void checkIsActiveChanges(){
        testUser = new User("test", new HashMap<>());
        testUser.setInactive();
        new QuitCommand().execute(testUser);
        Assertions.assertFalse(testUser.isActive());
    }

    @Test
    public void checkMessageIsPrinted(){
        testUser = new User("test", new HashMap<>());
        new QuitCommand().execute(testUser);
        Assertions.assertEquals("Вы вышли!\r\n", output.toString());
    }

    @AfterEach
    public void cleanUpStreams(){
        System.setOut(null);
    }
}
