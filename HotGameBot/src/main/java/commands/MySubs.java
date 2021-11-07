package commands;

import Entities.Title;
import Entities.User;

public class MySubs implements ICommand {
    private User currentUser;

    public MySubs() {
    }

    @Override
    public void execute(User user) {
        currentUser = user;
        printUserSubs();
    }

    private void printUserSubs() {
        String message;
        if (currentUser.getTitles().values().size() == 0)
            message = "У вас пока нет подписок";
        else
            message = getMessage();
        System.out.println(message);
    }

    private String getMessage() {
        StringBuilder subs = new StringBuilder();
        int i = 1;
        for (Title title : currentUser.getTitles().values())
            subs.append(String.format("#%d ---- ", i++)).append(title.toString());
        return subs.toString();
    }
}
