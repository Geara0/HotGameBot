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
        //startBot();
        db();


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

    private static void db() {
        var jdbcURL = "jdbc:postgresql://localhost:5432/postgres";
        var userName = "postgres";
        var password = "ChPs2345";
        try {
            var connection = DriverManager.getConnection(jdbcURL, userName, password);
            String sql = "INSERT INTO users (id, subscriptions)" +
                    "VALUES ('12', '\"1\"=>null, \"2\"=>null');";
            var statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
