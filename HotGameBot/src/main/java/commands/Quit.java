package commands;

import Entities.User;

public class Quit implements ICommand{
    @Override
    public void execute(User user) {
        user.setActive(false);
        System.out.println("Вы вышли!");
    }
}
