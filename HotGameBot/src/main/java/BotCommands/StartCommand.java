package BotCommands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static BotCommands.CommandsConstants.START_DESCRIPTION;
import static BotCommands.CommandsConstants.START_NAME;

public class StartCommand extends Command {
    public StartCommand() {
        super(START_NAME.toStringValue(), START_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var message = new SendMessage();
        message.setText(String.format("Hi, %s!", user.getUserName()));
        execute(absSender, message, user);
    }
}
