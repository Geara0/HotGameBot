import API.APIWorker;
import bot.HotGameBot;
import db.DBWorker;
import db.IDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private final static Logger logger = LogManager.getLogger("root");
    private final static Long zeroMinutes = 0L;
    private final static Long fifteenMinutes = 900000L;
    private final static Long threeHours = 10800000L;

    public static void main(String[] args) {
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                dbUpdatingFromAPI();
            }
        }, zeroMinutes, threeHours);
        startBot();
    }


    private static void startBot() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new HotGameBot());
        } catch (TelegramApiException e) {
            logger.error("bot startup error: {}", Arrays.toString(e.getStackTrace()));
        }
    }

    private static void dbUpdatingFromAPI() {
        var apiWorker = new APIWorker();
        IDB dbWorker = new DBWorker();
        var data = apiWorker.getData();
        for (var title : data) {
            dbWorker.updateTitle(title);
        }
        //TODO запихивать тайтлы в бд
    }
}
