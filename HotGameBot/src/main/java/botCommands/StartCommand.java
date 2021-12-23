package botCommands;

import db.DBWorker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static botCommands.CommandsConstants.*;

/**
 * Команда запуска бота
 * Записывает оного в бд
 */
public class StartCommand extends Command {
    public StartCommand() {
        super(START_NAME.toStringValue(), START_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var db = new DBWorker();
        db.addUser(user.getId());
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText(HELLO.toStringValue() + user.getUserName() + "!\n" + HELP_ON_START.toStringValue());
        var photo = new SendPhoto();
        photo.setChatId(chat.getId().toString());
        photo.setPhoto(new InputFile().setMedia(new File("src/main/java/botCommands/SubscribeExample.png")));
        execute(absSender, message, user);
        execute(absSender, photo, user);
    }
}
