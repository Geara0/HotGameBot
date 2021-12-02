package botCommands;

import db.DBWorker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

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

        //TODO: KeyboardMarkup с вариантами на что подписаться
        message.setText("Здесь должны быть варианты подписки:");
        execute(absSender, message, user);

        if (DBWorker.subscribeUser(user.getId(), title)) {
            message.setText(String.format("Вы успешно подписались на %s", title));
        } else {
            //TODO: Причины сбоя подписки
            message.setText(String.format("Произошла ошибка, вы не смогли подписаться на %s по причине: хихихихи", title));
        }
        execute(absSender, message, user);
    }

    private String getName(String[] strings) {
        if (strings == null || strings.length == 0)
            return null;
        return String.join(" ", strings);
    }
}
