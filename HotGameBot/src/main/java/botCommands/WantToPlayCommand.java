package botCommands;

import parsing.HotGameParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import bot.KeyboardCreator;
import parsing.IParser;

import static botCommands.CommandsConstants.WANT_TO_PLAY_DESCRIPTION;
import static botCommands.CommandsConstants.WANT_TO_PLAY_NAME;


public class WantToPlayCommand extends Command {
    public WantToPlayCommand(String commandIdentifier, String description) {
        super(WANT_TO_PLAY_NAME.toStringValue(), WANT_TO_PLAY_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        new KeyboardRow();
        IParser parser = new HotGameParser();
        if (strings != null && strings.length >= 1) {
            message.setText("По заданным параметрам рекомендуем:");
            message.setReplyMarkup(KeyboardCreator.createKeyboardMarkUp(parser.getRecommendations(strings)));
        } else {
            message.setText("По заданным параметрам ничего не найдено");
        }

        execute(absSender, message, user);
    }
}
