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

public final class HelloBot extends TelegramLongPollingCommandBot {
    private final String BOT_USERNAME = "@HotGameInfo_bot";
    private final String BOT_TOKEN = "2108249890:AAGc5p5xMiLHGuPmqZcDK7r4QnA-pHqatto";

    public HelloBot() {
        //Регистрация комманд
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

    private void processCallbackUpdate(CallbackQuery query) {
        //TODO: коллбек по спецсимволу
        var queryData = query.getData();
        var answer = new SendMessage();
        if (queryData.startsWith("##")) {
            IParser parser = new HotGameParser();
            var titles = parser.parseTitlesByName(
                    queryData.substring(queryData.indexOf("'"), queryData.lastIndexOf("'"))
            );
            var names = new ArrayList<String>(titles.size());
            for (var e : titles) names.add(e.getName());
            var keyboard = KeyboardCreator.createParsedKeyboardMarkUp(1, names);
            if (names.size() != 0)
                answer.setText("Тогда вот другие предложения:");
            else
                answer.setText("Мы ничего не нашли(");
            answer.setReplyMarkup(keyboard);
        } else if (queryData.startsWith("%%")) {
            IParser parser = new HotGameParser();
            var db = new DBWorker();
            var title = parser.parseTitlesByName(queryData.replaceAll("%", "")).get(0);
            db.addTitle(title);
            db.subscribeUser(query.getFrom().getId(), title.getName());
            answer.setText(String.format("Вы подписаны на %s", title.getName()));
        } else if (queryData.startsWith("$$")) {
            var db = new DBWorker();
            var title = queryData.replaceAll("\\$", "");
            db.subscribeUser(query.getFrom().getId(), title);
            answer.setText(String.format("Вы подписаны на %s", title));

        } else {
            var db = new DBWorker();
            var title = db.getTitle(queryData);
            answer.setText(title.toString());
        }

        answer.setChatId(query.getMessage().getChatId().toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getUserName(Message msg) {
        var user = msg.getFrom();
        var userName = user.getUserName();
        return userName != null ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    private void sendAnswer(Long chatId, String userName, String text) {
        var answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            // лог, что не вышло отправить юзернейму
            e.printStackTrace();
        }

    }
}
