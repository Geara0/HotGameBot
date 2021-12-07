package botCommands;

import db.DBWorker;
import db.IDB;
import db.ReportState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static botCommands.CommandsConstants.UNSUBSCRIBE_DESCRIPTION;
import static botCommands.CommandsConstants.UNSUBSCRIBE_NAME;

public class UnsubscribeCommand extends Command {
    public UnsubscribeCommand() {
        super(UNSUBSCRIBE_NAME.toStringValue(), UNSUBSCRIBE_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());

        var title = getName(strings);

        if (title == null) {
            message.setText("Кажется, вы забыли ввести название\nИспользуйте /unsubscribe [title]");
            execute(absSender, message, user);
            return;
        }

        //TODO: KeyboardMarkup с вариантами от чего отписаться

        IDB db = new DBWorker();
        var report = db.unsubscribeUser(user.getId(), title);
        if (report == ReportState.OK) {
            message.setText(String.format("Вы успешно отписались от %s", title));
        } else {
            message.setText(String.format("Произошла ошибка, вы не смогли отписаться от %s по причине: %s",
                    title, report));
        }
        execute(absSender, message, user);
    }

    private String getName(String[] strings) {
        if (strings == null || strings.length == 0)
            return null;
        return String.join(" ", strings);
    }
}
