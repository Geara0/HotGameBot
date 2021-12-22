package botCommands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static botCommands.CommandsConstants.*;

public class ReportCommand extends Command {

    public ReportCommand() {
        super(REPORT_NAME.toStringValue(), REPORT_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText(REPORT_THANKS.toStringValue());
        execute(absSender, message, user);
        logger.warn("user report: {}", String.join(" ", strings));
    }
}
