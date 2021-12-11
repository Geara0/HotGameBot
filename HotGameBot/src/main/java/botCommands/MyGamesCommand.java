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
import java.util.List;

import static botCommands.CommandsConstants.*;

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
        List<String> subscriptionsNames = Arrays.asList(db.getSubscriptions(user.getId()));
        var keyboard = KeyboardCreator.createKeyboardMarkUp(subscriptionsNames);
        message.setText(UR_SUBSCRIPTIONS.toStringValue());
        message.setReplyMarkup(keyboard);
        execute(absSender, message, user);
    }
}
