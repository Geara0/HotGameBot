package BotCommands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static BotCommands.CommandsConstants.HELP_DESCRIPTION;
import static BotCommands.CommandsConstants.HELP_NAME;

public class HelpCommand extends Command {
    private final ICommandRegistry commandRegistry;

    public HelpCommand(ICommandRegistry commandRegistry) {
        super(HELP_NAME.toStringValue(), HELP_DESCRIPTION.toStringValue());
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder helpMessageBuilder = new StringBuilder("<b>Available commands:</b>\n");
        commandRegistry.getRegisteredCommands().forEach(command -> helpMessageBuilder.append(command.toString()).append("\n"));
        var helpMessage = new SendMessage();
        helpMessage.setChatId(chat.getId().toString());
        helpMessage.enableHtml(true);
        helpMessage.setText(helpMessageBuilder.toString());

        execute(absSender, helpMessage, user);
    }


}
