import bot.HelloBot;
import entities.User;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        startBot();

        //Scanner scanner = new Scanner(System.in);
        //ConsoleGamesService bot = new ConsoleGamesService();
        //bot.start();
        //while (bot.isRunning()) {
        //    User currentUser = bot.getUser();
        //    while (bot.hasUser()) {
        //        System.out.println("Введите комманду:");
        //        String message = scanner.nextLine();
        //        bot.runCommand(message, currentUser);
        //    }
        //}
    }


    private static void startBot() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new HelloBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
