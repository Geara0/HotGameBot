package botCommands;

import db.DBWorker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static botCommands.CommandsConstants.UNSUBSCRIBE_ALL_DESCRIPTION;
import static botCommands.CommandsConstants.UNSUBSCRIBE_ALL_NAME;


public class UnsubscribeAllCommand extends Command {
    public UnsubscribeAllCommand(String commandIdentifier, String description) {
        super(UNSUBSCRIBE_ALL_NAME.toStringValue(), UNSUBSCRIBE_ALL_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (DBWorker.unsubscribeAllUser(user.getId())) {
            message.setText("Вы успешно отписались от всех игр");
        } else {
            message.setText("Произошла ошибка");
        }

        execute(absSender, message, user);
    }
}
