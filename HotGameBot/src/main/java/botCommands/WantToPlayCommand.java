package botCommands;

import bot.KeyboardCreator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parsing.HotGameParser;
import parsing.IParser;

import static botCommands.CommandsConstants.WANT_TO_PLAY_DESCRIPTION;
import static botCommands.CommandsConstants.WANT_TO_PLAY_NAME;


public class WantToPlayCommand extends Command {
    public WantToPlayCommand() {
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
            var titles = parser.getRecommendations();
            var titleNames = new String[titles.size()];
            for (var i = 0; i < titleNames.length; i++) titleNames[i] = titles.get(i).getName();
            var keyboard = KeyboardCreator.createKeyboardMarkUp(titleNames);
            message.setReplyMarkup(keyboard);
        } else {
            message.setText("По заданным параметрам ничего не найдено");
        }

        execute(absSender, message, user);
    }
}
