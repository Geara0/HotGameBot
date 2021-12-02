package commands;

import entities.User;

/**
 * Класс команды, возвращающей сообщение справки
 */
public class HelpCommand implements ICommand {

    /**
     * Реализация ICommand
     *
     * @param user - пользователь, для которого вызывается команд(здесь не нужно но интерфейс)
     */
    @Override
    public void execute(User user) {
        System.out.print(getHelpMessage());
    }

    /**
     * Возвращает сообщение справки
     *
     * @return строка-справка
     */
    private String getHelpMessage() {
        return CommandsConstants.HELP.toStringValue();
    }
}
