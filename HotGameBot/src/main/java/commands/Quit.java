package commands;

import Entities.User;

public class Quit implements ICommand {
    /**
     * Реализация ICommand
     *
     * @param user - пользователь, для которого выполняется команда
     */
    @Override
    public void execute(User user) {
        user.setInactive();
        System.out.println(CommandsConst.QUIT.toStringValue());
    }
}
