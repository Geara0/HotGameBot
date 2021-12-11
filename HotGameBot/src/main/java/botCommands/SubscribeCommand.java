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
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());

        var title = getName(strings);

        if (title == null) {
            message.setText(U_FORGOT_NAME.toStringValue());
            execute(absSender, message, user);
            return;
        }

        IDB db = new DBWorker();
        var closest = new ArrayList<>(Arrays.asList(db.getClosest(title)));
        closest.add(NOT_IT.toStringValue() + String.format("'%s'", title));
        var keyboard = KeyboardCreator.createKeyboardMarkUp(1, closest, KeyboardMarkupTypes.DB);
        if (db.subscribeUser(user.getId(), title) == ReportState.OK) {
            message.setText(U_BEEN_SUBSCRIBED.toStringValue() + title);
        } else {
            message.setText(WE_FOUND_MULTIPLE_VARIANTS.toStringValue());
            message.setReplyMarkup(keyboard);
        }
        execute(absSender, message, user);
    }

    private String getName(String[] strings) {
        if (strings == null || strings.length == 0)
            return null;
        return String.join(" ", strings);
    }
}
