import Entities.User;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleGamesService bot = new ConsoleGamesService();
        bot.start();
        while(bot.isRunning()){
            User currentUser = bot.getUser();
            while(bot.isThereAUser()){
                System.out.println("Введите комманду:");
                String message = scanner.nextLine();
                bot.runCommand(message,currentUser);
            }
        }
    }
}
