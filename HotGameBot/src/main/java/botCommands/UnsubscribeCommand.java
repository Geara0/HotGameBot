package botCommands;

import db.DBWorker;
import db.IDB;
import db.ReportState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static botCommands.CommandsConstants.*;

/**
 * Команда отписки от игры
 */
public class UnsubscribeCommand extends Command {
    public UnsubscribeCommand() {
        super(UNSUBSCRIBE_NAME.toStringValue(), UNSUBSCRIBE_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());

        String titleName = getName(strings);

        if (titleName == null) {
            message.setText(U_FORGOT_NAME.toStringValue());
            execute(absSender, message, user);
            return;
        }

        //TODO: KeyboardMarkup с вариантами от чего отписаться task3

        IDB db = new DBWorker();
        var report = db.unsubscribeUser(user.getId(), titleName);
        if (report == ReportState.OK)
            message.setText(U_BEEN_UNSUBSCRIBED.toStringValue() + titleName);
        else
            message.setText(ERROR_BECAUSE.toStringValue() + report);

        execute(absSender, message, user);
    }

    private String getName(String[] strings) {
        if (strings == null || strings.length == 0)
            return null;
        return String.join(" ", strings);
    }
}
