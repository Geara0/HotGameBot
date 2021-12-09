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
