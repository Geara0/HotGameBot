package commands;

import Entities.User;

public interface ICommand {
    void execute(User user);
}
