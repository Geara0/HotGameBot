package commands;

import Entities.Title;
import Entities.User;

/**
 * Класс для команды, выводящей список подписок пользователя
 */
public class MySubsCommand implements ICommand {

    /**
     * Реализация ICommand
     *
     * @param user - пользователь, для которого выполняется команда
     */
    @Override
    public void execute(User user) {
        System.out.println(getUserSubsMessage(user));
    }

    /**
     * Формирует список подписок пользователя или возвращет сообщение о том, что их нет
     *
     * @param user - пользователь, для которого выполняется команда
     * @return сообщение со списком подписок или о том, что их нет
     */
    private String getUserSubsMessage(User user) {
        StringBuilder subs = new StringBuilder();
        if (user.getTitles().size() == 0)
            return CommandsConstants.NO_SUBS.toStringValue();
        int i = 1;
        for (Title title : user.getTitles().values())
            subs.append(String.format("#%d ---- ", i++)).append(title.getStringForm());
        return subs.toString();
    }
}