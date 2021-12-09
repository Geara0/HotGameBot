package botCommands;

import bot.KeyboardCreator;
import db.DBWorker;
import db.IDB;
import entities.Title;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;

import static botCommands.CommandsConstants.MY_GAMES_DESCRIPTION;
import static botCommands.CommandsConstants.MY_GAMES_NAME;

/**
 * Команда получения списка игр пользователя
 */
public class MyGamesCommand extends Command {
    public MyGamesCommand() {
        super(MY_GAMES_NAME.toStringValue(), MY_GAMES_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var message = new SendMessage();
        IDB db = new DBWorker();
        message.setChatId(chat.getId().toString());
        var subscriptions = Arrays.asList(db.getSubscriptions(user.getId()));
        var keyboard = KeyboardCreator.createKeyboardMarkUp(subscriptions);
        message.setText("Вы подписаны на:");
        message.setReplyMarkup(keyboard);
        execute(absSender, message, user);
    }
}
