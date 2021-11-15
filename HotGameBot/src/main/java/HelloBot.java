import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HelloBot extends TelegramLongPollingCommandBot {
    private final String botName = "@HotGameInfo_bot";

    private final String botToken = "2108249890:AAGc5p5xMiLHGuPmqZcDK7r4QnA-pHqatto";

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (!(update.hasMessage() && update.getMessage().hasText())) return;

        var msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = getUserName(msg);
        var answer = String.format("Hi, %s", userName);
        sendAnswer(chatId, userName, answer);
    }

    @Override
    public String getBotToken() {
        return botToken;
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
