package bot;

import botCommands.*;
import db.DBWorker;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import parsing.HotGameParser;
import parsing.IParser;

public final class HelloBot extends TelegramLongPollingCommandBot {
    private final String BOT_USERNAME = "@HotGameInfo_bot";
    private final String BOT_TOKEN = "2108249890:AAGc5p5xMiLHGuPmqZcDK7r4QnA-pHqatto";

    public HelloBot() {
        //Регистрация комманд
        register(new StartCommand());
        register(new MyGamesCommand());
        register(new SubscribeCommand());
        register(new UnsubscribeCommand());
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
        if (queryData.startsWith("&&")) {
            IParser parser = new HotGameParser();
            var titles = parser.parseTitlesByName(queryData.split("&&")[0]);
            var titleNames = new String[titles.size()];
            for(var i=0;i<titleNames.length;i++)
                titleNames[i]=titles.get(i).getName();
            var keyboard = KeyboardCreator.createKeyboardMarkUp(titleNames);
            answer.setReplyMarkup(keyboard);
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
