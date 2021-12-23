package botCommands;

import bot.KeyboardCreator;
import bot.KeyboardMarkupTypes;
import db.DBWorker;
import db.IDB;
import db.ReportState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.Arrays;

import static botCommands.CommandsConstants.*;

/**
 * Команда подписки на игру
 */
public class SubscribeCommand extends Command {
    public SubscribeCommand() {
        super(SUBSCRIBE_NAME.toStringValue(), SUBSCRIBE_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var titleName = getName(strings);
        var message = TrySubscribe(user, chat, titleName);
        execute(absSender, message, user);
    }

    public static SendMessage TrySubscribe(User user, Chat chat, String titleName) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (titleName == null || titleName.trim().equals("")) {
            message.setText(U_FORGOT_NAME.toStringValue());
            return message;
        }

        IDB db = new DBWorker();
        if (db.subscribeUser(user.getId(), titleName) == ReportState.OK) {
            message.setText(U_BEEN_SUBSCRIBED.toStringValue() + titleName);
        } else {
            var closest = new ArrayList<>(Arrays.asList(db.getClosestOverall(titleName, 8)));
            var closestIds = new ArrayList<Long>(closest.size());
            for (var gameName : closest) {
                closestIds.add(db.getId(gameName));
            }
            //closest.add(NOT_IT.toStringValue() + String.format("'%s'", titleName));
            var keyboard = KeyboardCreator.createKeyboardMarkUpById(1, closestIds, KeyboardMarkupTypes.DB);
            message.setText(WE_FOUND_MULTIPLE_VARIANTS.toStringValue());
            message.setReplyMarkup(keyboard);
        }

        return message;
    }

    private String getName(String[] strings) {
        if (strings == null || strings.length == 0)
            return null;
        return String.join(" ", strings);
    }
}
