package bot;

import API.APIWorker;
import botCommands.*;
import db.DBWorker;
import db.IDB;
import db.ReportState;
import entities.Levenshtein.LevenshteinCalculator;
import entities.Title;
import entities.UpdateReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static bot.KeyboardMarkupTypes.CONFIRM_UNSUB;
import static botCommands.CommandsConstants.*;

public final class HotGameBot extends TelegramLongPollingCommandBot {
    private final String BOT_USERNAME = "@HotGameInfo_bot";
    private final String BOT_TOKEN = System.getenv("HotGameBotToken");
    private final static Logger logger = LogManager.getLogger("bot");
    private final static Long zeroMinutes = 0L;
    private final static Long fifteenMinutes = 900000L;
    private final static Long threeHours = 10800000L;

    /**
     * Регистрация команд и запуск таймера
     */
    public HotGameBot() {
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAndNotify();
            }
        }, fifteenMinutes, threeHours);

        register(new StartCommand());
        register(new MyGamesCommand());
//        register(new SubscribeCommand());
//        register(new UnsubscribeCommand());
        register(new UnsubscribeAllCommand());
//        register(new WantToPlayCommand());
        register(new ReportCommand());
        var helpCommand = new HelpCommand(this);
        register(helpCommand);
        logger.info("commands registered");

        registerDefaultAction(((absSender, message) -> {
            var text = new SendMessage();
            text.setText(String.format("Command not found: %s", message.getText()));
            text.setChatId(message.getChatId().toString());
            try {
                logger.debug("executing command: {}", text.getText());
                absSender.execute(text);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        }));
        logger.info("bot is stated");
    }

    private void updateAndNotify() {
        var apiWorker = new APIWorker();
        IDB db = new DBWorker();
        var data = apiWorker.getData();
        var reports = new ArrayList<UpdateReport>();
        for (var title : data) {
            var currentReport = db.updateTitle(title);
            if (currentReport.wasUpdated())
                reports.add(db.updateTitle(title)); //если обновился - множество айди подписчиков, если нет - пустой
            if (currentReport.getSubscribers() != null && currentReport.getSubscribers().size() > 0) {
                for (var userId : currentReport.getSubscribers())
                    notifyUser(userId, currentReport.getTitle(), currentReport.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        var minimalDistance = 20;

        if (update.hasCallbackQuery()) processCallbackUpdate(update.getCallbackQuery());
        if (!(update.hasMessage() && update.getMessage().hasText())) return;

        IDB db = new DBWorker();
        var levenshtein = new LevenshteinCalculator();
        var message = update.getMessage();
        String text = message.getText();
        User user = message.getFrom();
        Long userId = user.getId();
        String userName = getUserName(message);
        logger.debug("received non-command update: user:{}, userName:{}, text:{}", userId, userName, text);
        var subscriptions = db.getSubscriptions(userId);
        var reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        var superClosestStrings = levenshtein.getStringsInDistance(subscriptions, text, minimalDistance);
        if (superClosestStrings.length > 0 && Arrays.asList(subscriptions).contains(superClosestStrings[0])) {
            processCallbackDefault(new CallbackQuery(
                    null, user, message, null, String.valueOf(db.getId(superClosestStrings[0])), null, null), reply);
        } else {
            logger.debug("subscribe processing");
            reply = SubscribeCommand.TrySubscribe(user, message.getChat(), text);
        }

        try {
            execute(reply);
            logger.debug("reply executed: user:{}, {}, reply:{}", user.getId(), user.getUserName(), reply.getText());
        } catch (TelegramApiException e) {
            logger.error("exception thrown: {}", Arrays.toString(e.getStackTrace()));
        }

    }

    private void notifyUser(String userId, Title title, String message) {
        var reply = new SendMessage();
        reply.setChatId(userId);
        reply.setText(message + title.toString());
        try {
            execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обработка callback с Markup кнопок
     */
    private void processCallbackUpdate(CallbackQuery query) {
        var queryData = query.getData();
        var answer = new SendMessage();

        logger.debug("processing callback: {}, {}, {}",
                query.getFrom().getId(), query.getFrom().getUserName(), query.getData());
        if (queryData.startsWith(KeyboardMarkupTypes.DB.toStringValue())) {
            logger.debug("processing 'db' callback");
            processCallbackDB(query, answer);
        } else if (queryData.startsWith(KeyboardMarkupTypes.CONFIRM_UNSUB.toStringValue())) {
            logger.debug("processing 'confirm-unsub' callback");
            processCallbackConfirmUnsub(query, answer);
        } else {
            logger.debug("processing default callback");
            processCallbackDefault(query, answer);
        }
        logger.debug("processing complete: {}", answer.getText());

        answer.setChatId(query.getMessage().getChatId().toString());
        try {
            execute(answer);
            logger.debug("answer executed: user:{}, {}, text:{}",
                    query.getFrom().getId(), query.getFrom().getUserName(), answer.getText());
        } catch (TelegramApiException e) {
            logger.debug("exception thrown: {}", Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Получить имя пользователя по сообщению
     *
     * @param msg сообщение
     * @return имя пользователя
     */
    private String getUserName(Message msg) {
        User user = msg.getFrom();
        var userName = user.getUserName();
        return userName != null ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    /**
     * Обработать callback с обычной кнопки
     *
     * @param answer ответное сообщение пользователю
     */
    private static void processCallbackDefault(CallbackQuery query, SendMessage answer) {
        IDB db = new DBWorker();
        var id = Long.parseLong(query.getData());
        Title title = db.getTitle(db.getName(id));
        answer.setText(title.toString());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        var keyboardRow = new ArrayList<InlineKeyboardButton>();
        var button = KeyboardCreator.createButton(UNSUBSCRIBE.toStringValue(), CONFIRM_UNSUB, String.valueOf(id));
        keyboardRow.add(button);
        keyboard.add(keyboardRow);
        answer.setReplyMarkup(new InlineKeyboardMarkup(keyboard));
        answer.enableHtml(true);
    }

    /**
     * Обработать callback с подтверждения отписки
     *
     * @param answer ответное сообщение пользователю
     */
    private static void processCallbackConfirmUnsub(CallbackQuery query, SendMessage answer) {
        IDB db = new DBWorker();
        var titleName = db.getName(Long.parseLong(query.getData().replaceAll("@", "")));
        db.unsubscribeUser(query.getFrom().getId(), titleName);
        answer.setText(U_BEEN_UNSUBSCRIBED.toStringValue() + titleName);
    }

    /**
     * Обработать callback с кнопки, работающей с бд
     *
     * @param answer ответное сообщение пользователю
     */
    private static void processCallbackDB(CallbackQuery query, SendMessage answer) {
        IDB db = new DBWorker();
        var titleId = Long.parseLong(query.getData().replaceAll("\\$", ""));
        var report = db.subscribeUser(query.getFrom().getId(), titleId);
        if (report == ReportState.OK)
            answer.setText(U_BEEN_SUBSCRIBED.toStringValue() + db.getName(titleId));
        else if (report == ReportState.ALREADY)
            answer.setText(U_ALREADY_SUBSCRIBED.toStringValue() + db.getName(titleId));
        else //TODO:лог
            answer.setText(ERROR_BECAUSE.toStringValue() + report.toStringValue());
    }
}
