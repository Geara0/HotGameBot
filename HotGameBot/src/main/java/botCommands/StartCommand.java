package botCommands;

import db.DBWorker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static botCommands.CommandsConstants.START_DESCRIPTION;
import static botCommands.CommandsConstants.START_NAME;

/**
 * Команда запуска бота
 * Записывает оного в бд
 */
public class StartCommand extends Command {
    public StartCommand() {
        super(START_NAME.toStringValue(), START_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var db = new DBWorker();
        db.addUser(user.getId());
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText(String.format("Hi, %s!", user.getUserName()));
        execute(absSender, message, user);
    }
}
