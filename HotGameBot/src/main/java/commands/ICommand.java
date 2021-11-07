package commands;

import Entities.User;

/**
 * Интерфейс для команд
 */
public interface ICommand {
    /**
     * Метод для запуска выполнения программ
     *
     * @param user - пользователь, для которого выполняется команда
     */
    void execute(User user);
}
