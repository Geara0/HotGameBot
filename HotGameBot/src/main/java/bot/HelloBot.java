package bot;

import botCommands.*;
import db.DBWorker;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import parsing.HotGameParser;
import parsing.IParser;

import java.util.ArrayList;

import static bot.KeyboardMarkupTypes.*;

public final class HelloBot extends TelegramLongPollingCommandBot {
    private final String BOT_USERNAME = "@HotGameInfo_bot";
    private final String BOT_TOKEN = System.getenv("HotGameBotToken");

    /**
     * Регистрация комманд
     */
    public HelloBot() {
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

        var msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = getUserName(msg);
        var answer = String.format("Hi, %s", userName);
        sendAnswer(chatId, userName, answer);

    }

    /**
     * Обработка callback с Markup кнопок
     */
    private void processCallbackUpdate(CallbackQuery query) {
        var queryData = query.getData();
        var answer = new SendMessage();

        if (queryData.startsWith(NOT_IT.toStringValue())) {
            CallbackProcessor.processCallbackNotIt(query, answer);
        } else if (queryData.startsWith(PARSER.toStringValue())) {
            CallbackProcessor.processCallbackParser(query, answer);
        } else if (queryData.startsWith(DB.toStringValue())) {
            CallbackProcessor.processCallbackDB(query, answer);
        } else {
            CallbackProcessor.processCallbackDefault(query, answer);
        }

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
        var user = msg.getFrom();
        var userName = user.getUserName();
        return userName != null ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    //TODO: replace in task3

    /**
     * Отправить пользователю ответ
     */
    private void sendAnswer(Long chatId, String userName, String text) {
        var answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
