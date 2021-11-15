package commandsTests;

import commands.ICommand;
import commands.WantToPlayCommand;
import Entities.Title;
import Entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;

import static commands.CommandsConstants.AVAILABLE_RECOMMENDATIONS;
import static commands.CommandsConstants.CANT_FIND_TITLE;

public class WantToPlayTest {

    PrintStream sysOutBackup = System.out;
    InputStream sysInBackup = System.in;
    ByteArrayInputStream input;
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    Title toUnsub = new Title("testTitle", "testLink", "testBuyLink", 75377);
    Title anotherTitle = new Title("anotherTitleToTest", "anotherTestLink", "testBuyLink", 7537);
    HashMap<String, Title> testMapping = new HashMap<>() {{
        put("anotherTitleToTest", anotherTitle);
        put("testTitle", toUnsub);
    }};

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(output));
    }

    @Test
    public void negativeCashWritesCantFind() {
        input = new ByteArrayInputStream("-1".getBytes());
        System.setIn(input);

        ICommand wantToPlayCommand = new WantToPlayCommand(testMapping);
        wantToPlayCommand.execute(new User("username", new HashMap<>()));

        String expected = AVAILABLE_RECOMMENDATIONS.toStringValue() +
                "\r\n" + CANT_FIND_TITLE.toStringValue();

        Assertions.assertEquals(expected, output.toString());
    }

    @Test
    public void maximumPricePrintsAll() {
        var expected = new StringBuilder().append(AVAILABLE_RECOMMENDATIONS.toStringValue()).append("\r\n");
        int maxPrice = 0;
        for (Title title : testMapping.values()) {
            if (maxPrice < title.getPrice())
                maxPrice = title.getPrice();
            expected.append(title.getStringForm());
        }
        input = new ByteArrayInputStream(Integer.toString(maxPrice).getBytes());
        System.setIn(input);
        ICommand wantToPlayCommand = new WantToPlayCommand(testMapping);
        wantToPlayCommand.execute(new User("username", new HashMap<>()));
        Assertions.assertEquals(expected.toString(),output.toString());
    }

    @AfterEach
    public void backup() {
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }
}
