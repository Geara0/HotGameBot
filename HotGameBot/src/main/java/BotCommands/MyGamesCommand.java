package BotCommands;

import Entities.Title;
import db.dbWorker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static BotCommands.CommandsConstants.MY_GAMES_DESCRIPTION;
import static BotCommands.CommandsConstants.MY_GAMES_NAME;

public class MyGamesCommand extends Command {
    public MyGamesCommand() {
        super(MY_GAMES_NAME.toStringValue(), MY_GAMES_DESCRIPTION.toStringValue());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        var message = new SendMessage();
        message.setText(dbWorker.getSubscriptions(user.getId()));
        execute(absSender, message, user);
    }

    private String getUserSubsMessage(Entities.User user) {
        var subs = new StringBuilder();
        if (user.getTitles().size() == 0)
            return CommandsConstants.NO_SUBS.toStringValue();
        int i = 1;
        for (Title title : user.getTitles().values())
            subs.append(String.format("#%d ---- ", i++)).append(title.getStringForm());
        return subs.toString();
    }
}
