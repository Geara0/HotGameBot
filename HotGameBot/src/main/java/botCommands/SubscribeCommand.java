package botCommands;

import bot.KeyboardCreator;
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
        var keyboard = KeyboardCreator.createdbKeyboardMarkUp(1, closest);
        if (db.subscribeUser(user.getId(), title) == ReportState.OK) {
            message.setText(String.format("Вы успешно подписались на %s", title));
        } else {
            message.setText("Мы нашли несколько вариантов по вашему запросу");
            message.setReplyMarkup(keyboard);
            //IParser parser = new HotGameParser();
            //var titles = parser.parseTitlesByName(title);
            //if (parser.getReport() == parsing.ReportState.OK) {
            //    var names = new ArrayList<String>(titles.size());
            //    for (var e : titles) names.add(e.getName());
            //    keyboard = KeyboardCreator.createSpecialKeyboardMarkUp(1, names.toArray(new String[0]));
            //    message.setText("Мы нашли несколько вариантов по вашему запросу");
            //    message.setReplyMarkup(keyboard);
            //} else {
            //    message.setText(String.format("Произошла ошибка, вы не смогли подписаться на %s по причине: %s",
            //            title, parser.getReport().toStringValue()));
            //}
        }
        execute(absSender, message, user);
    }

    private String getName(String[] strings) {
        if (strings == null || strings.length == 0)
            return null;
        return String.join(" ", strings);
    }
}
