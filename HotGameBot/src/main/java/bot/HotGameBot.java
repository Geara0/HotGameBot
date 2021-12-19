package bot;

import botCommands.*;
import db.DBWorker;
import db.IDB;
import entities.Title;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import parsing.HotGameParser;
import parsing.IParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bot.ConstantReplies.NOTHING_FOUND;
import static bot.ConstantReplies.OTHER_SUGGESTIONS;
import static bot.KeyboardMarkupTypes.*;
import static botCommands.CommandsConstants.*;

public final class HotGameBot extends TelegramLongPollingCommandBot {
    private final String BOT_USERNAME = "@HotGameInfo_bot";
    private final String BOT_TOKEN = System.getenv("HotGameBotToken");

    /**
     * Регистрация комманд
     */
    public HotGameBot() {
        register(new StartCommand());
        register(new MyGamesCommand());
        register(new SubscribeCommand());
        register(new UnsubscribeCommand());
        register(new UnsubscribeAllCommand());
        register(new WantToPlayCommand());
        var helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction(((absSender, message) -> {
            var text = new SendMessage();
            text.setText(String.format("Command not found: %s", message.getText()));
            text.setChatId(message.getChatId().toString());
            try {
                absSender.execute(text);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        }));
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
        if (update.hasCallbackQuery()) processCallbackUpdate(update.getCallbackQuery());
        if (!(update.hasMessage() && update.getMessage().hasText())) return;

        IDB db = new DBWorker();
        var message = update.getMessage();
        String text = message.getText();
        User user = message.getFrom();
        Long userId = user.getId();
        String userName = getUserName(message);
        var subscriptions = db.getSubscriptions(userId);

        var reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        if (Arrays.asList(subscriptions).contains(text))
            processCallbackDefault(new CallbackQuery(
                    null, user, message, null, text, null, null), reply);
        else
            reply = SubscribeCommand.TrySubscribe(user, message.getChat(), text);


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

        if (queryData.startsWith(KeyboardMarkupTypes.NOT_IT.toStringValue()))
            processCallbackNotIt(query, answer);
        else if (queryData.startsWith(PARSER.toStringValue()))
            processCallbackParser(query, answer);
        else if (queryData.startsWith(DB.toStringValue()))
            processCallbackDB(query, answer);
        else if (queryData.startsWith(CONFIRM_UNSUB.toStringValue()))
            processCallbackConfirmUnsub(query, answer);
        else
            processCallbackDefault(query, answer);

        answer.setChatId(query.getMessage().getChatId().toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
        Title title = db.getTitle(query.getData());
        answer.setText(title.toString());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        var keyboardRow = new ArrayList<InlineKeyboardButton>();
        var button = KeyboardCreator.createButton(UNSUBSCRIBE.toStringValue(), CONFIRM_UNSUB, title.getName());
        keyboardRow.add(button);
        keyboard.add(keyboardRow);
        answer.setReplyMarkup(new InlineKeyboardMarkup(keyboard));
    }

    /**
     * Обработать callback с подтверждения отписки
     *
     * @param answer ответное сообщение пользователю
     */
    private static void processCallbackConfirmUnsub(CallbackQuery query, SendMessage answer) {
        IDB db = new DBWorker();
        var titleName = query.getData().replaceAll("@", "");
        db.unsubscribeUser(query.getFrom().getId(), query.getData().replaceAll("@", ""));
        answer.setText(U_BEEN_UNSUBSCRIBED.toStringValue() + titleName);
    }

    /**
     * Обработать callback с кнопки, работающей с бд
     *
     * @param answer ответное сообщение пользователю
     */
    private static void processCallbackDB(CallbackQuery query, SendMessage answer) {
        IDB db = new DBWorker();
        String titleName = query.getData().replaceAll("\\$", "");
        db.subscribeUser(query.getFrom().getId(), titleName);
        answer.setText(U_BEEN_SUBSCRIBED.toStringValue() + titleName);
    }

    /**
     * Обработать callback с кнопки, работающей с парсером
     *
     * @param answer ответное сообщение пользователю
     */
    private static void processCallbackParser(CallbackQuery query, SendMessage answer) {
        IParser parser = new HotGameParser();
        IDB db = new DBWorker();
        Title title = parser.parseTitlesByName(query.getData().replaceAll("%", "")).get(0);
        db.addTitle(title);
        db.subscribeUser(query.getFrom().getId(), title.getName());
        answer.setText(U_BEEN_UNSUBSCRIBED.toStringValue() + title.getName());
    }

    /**
     * Обработать callback с кнопки, переводящей работу с бд на парсер
     *
     * @param answer ответное сообщение пользователю
     */
    private static void processCallbackNotIt(CallbackQuery query, SendMessage answer) {
        IParser parser = new HotGameParser();
        String queryData = query.getData();
        ArrayList<Title> titleNames = parser.parseTitlesByName(
                queryData.substring(queryData.indexOf("'"), queryData.lastIndexOf("'"))
        );
        var names = new ArrayList<String>(titleNames.size());
        for (var e : titleNames) names.add(e.getName());
        var keyboard = KeyboardCreator.createKeyboardMarkUp(1, names, PARSER);
        if (names.size() != 0)
            answer.setText(OTHER_SUGGESTIONS.toStringValue());
        else
            answer.setText(NOTHING_FOUND.toStringValue());
        answer.setReplyMarkup(keyboard);
    }
}
