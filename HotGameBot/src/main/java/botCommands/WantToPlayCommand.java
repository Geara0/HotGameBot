package botCommands;

import bot.KeyboardCreator;
import bot.KeyboardMarkupTypes;
import entities.Title;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parsing.HotGameParser;
import parsing.IParser;

import java.util.ArrayList;

import static botCommands.CommandsConstants.*;


/**
 * Команда, выдающая игры по критерию
 */
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
            message.setText(WE_RECOMMEND.toStringValue());
            ArrayList<Title> titles = parser.getRecommendations(strings);
            var titleNames = new ArrayList<String>(titles.size());
            for (Title title : titles) titleNames.add(title.getName());
            var keyboard = KeyboardCreator.createKeyboardMarkUp(1, titleNames, KeyboardMarkupTypes.PARSER);
            message.setReplyMarkup(keyboard);
        } else {
            message.setText(WE_CANT_RECOMMEND.toStringValue());
        }

        execute(absSender, message, user);
    }
}
