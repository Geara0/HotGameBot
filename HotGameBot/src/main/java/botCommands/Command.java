package botCommands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

/**
 * Абстрактный класс, чтобы удобнее было наследовать
 */
public abstract class Command extends BotCommand {
    public Command(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    protected final static Logger logger = LogManager.getLogger("botCommands");

    void execute(AbsSender sender, SendMessage message, User user) {
        try {
            sender.execute(message);
            logger.debug("command executed, {}, {}, {}",this.getClass(),user.getId(),user.getUserName());
        } catch (TelegramApiException e) {
            logger.error("executing error: {}", Arrays.toString(e.getStackTrace()));
        }
    }
}