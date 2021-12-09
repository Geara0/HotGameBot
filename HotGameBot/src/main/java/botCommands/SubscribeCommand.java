package botCommands;

import bot.KeyboardCreator;
import bot.KeyboardMarkupTypes;
import db.DBWorker;
import db.IDB;
import db.ReportState;
import entities.Title;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parsing.HotGameParser;
import parsing.IParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static botCommands.CommandsConstants.SUBSCRIBE_DESCRIPTION;
import static botCommands.CommandsConstants.SUBSCRIBE_NAME;

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
            message.setText("Кажется, вы забыли ввести название\nИспользуйте /subscribe [title]");
            execute(absSender, message, user);
            return;
        }

        IDB db = new DBWorker();
        var closest = new ArrayList<>(Arrays.asList(db.getClosest(title)));
        closest.add(String.format("Это не то '%s'", title));
        var keyboard = KeyboardCreator.createKeyboardMarkUp(1, closest, KeyboardMarkupTypes.DB);
        if (db.subscribeUser(user.getId(), title) == ReportState.OK) {
            message.setText(String.format("Вы успешно подписались на %s", title));
        } else {
            message.setText("Мы нашли несколько вариантов по вашему запросу");
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
