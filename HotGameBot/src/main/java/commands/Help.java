package commands;

import Entities.User;

/**
 * Класс команды, возвращающей сообщение справки
 */
public class Help implements ICommand {

    /**
     * Реализация ICommand
     *
     * @param user - пользователь, для которого вызывается команд(здесь не нужно но интерфейс)
     */
    @Override
    public void execute(User user) {
        printMessage(getHelpMessage());
    }

    /**
     * Печатает сообщение
     */
    private void printMessage(String message) {
        System.out.print(message);
    }

    /**
     * Возвращает сообщение справки
     *
     * @return строка-справка
     */
    private String getHelpMessage() {
        return CommandsConst.HELP.toStringValue();
    }
}
