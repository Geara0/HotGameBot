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
 * Отписывает пользователя от всех игр
 */
public class UnsubscribeAllCommand extends Command {
    public UnsubscribeAllCommand() {
        super(UNSUBSCRIBE_ALL_NAME.toStringValue(), UNSUBSCRIBE_ALL_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());

        IDB db = new DBWorker();
        var report = db.unsubscribeAllUser(user.getId());

        if (report == ReportState.OK)
            message.setText(U_BEEN_UNSUBSCRIBED_ALL.toStringValue());
        else
            message.setText(ERROR_BECAUSE.toStringValue() + report.toStringValue());

        execute(absSender, message, user);
    }
}
