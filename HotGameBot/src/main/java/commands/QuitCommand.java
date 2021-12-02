package commands;

import entities.User;

public class QuitCommand implements ICommand {
    /**
     * Реализация ICommand
     *
     * @param user - пользователь, для которого выполняется команда
     */
    @Override
    public void execute(User user) {
        user.setInactive();
        System.out.println(CommandsConstants.QUIT.toStringValue());
    }
}
