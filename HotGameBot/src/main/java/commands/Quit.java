package commands;

import Entities.User;

public class Quit implements ICommand {
    @Override
    public void execute(User user) {
        user.setInactive();
        System.out.println("Вы вышли!");
    }
}
