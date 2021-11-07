package commandsTests;

import commands.MySubs;
import Entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

public class MySubsTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream systemOutBackup = System.out;
    User testUser;
    @BeforeEach
    public void setUpStreams(){
        System.setOut(new PrintStream(output));
    }

    @Test
    public void newUserSubscriptions(){
        testUser = new User("test", new HashMap<>());
        new MySubs().execute(testUser);
        Assertions.assertEquals("У вас пока нет подписок\r\n",output.toString());
    }

    @AfterEach
    public void setSystemOutBackup(){
        System.setOut(systemOutBackup);
    }
}
