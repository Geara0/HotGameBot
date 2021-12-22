import bot.HotGameBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;

public class Main {

    private final static Logger logger = LogManager.getLogger("root");

    public static void main(String[] args) {
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
}
