package bot;

import db.DBWorker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import parsing.HotGameParser;
import parsing.IParser;

import java.util.ArrayList;

import static bot.KeyboardMarkupTypes.PARSER;

/**
 * Вспомогательный класс, обрабатывающий callback с Markup кнопок
 */
public class CallbackProcessor {
    /**
     * Обработать callback с обычной кнопки
     * @param answer ответное сообщение пользователю
     */
    public static void processCallbackDefault(CallbackQuery query, SendMessage answer) {
        var db = new DBWorker();
        var title = db.getTitle(query.getData());
        answer.setText(title.toString());

    }

    /**
     * Обработать callback с кнопки, работающей с бд
     * @param answer ответное сообщение пользователю
     */
    public static void processCallbackDB(CallbackQuery query, SendMessage answer) {
        var db = new DBWorker();
        var title = query.getData().replaceAll("\\$", "");
        db.subscribeUser(query.getFrom().getId(), title);
        answer.setText(String.format("Вы подписаны на %s", title));
    }

    /**
     * Обработать callback с кнопки, работающей с парсером
     * @param answer ответное сообщение пользователю
     */
    public static void processCallbackParser(CallbackQuery query, SendMessage answer) {
        IParser parser = new HotGameParser();
        var db = new DBWorker();
        var title = parser.parseTitlesByName(query.getData().replaceAll("%", "")).get(0);
        db.addTitle(title);
        db.subscribeUser(query.getFrom().getId(), title.getName());
        answer.setText(String.format("Вы подписаны на %s", title.getName()));
    }

    /**
     * Обработать callback с кнопки, переводящей работу с бд на парсер
     * @param answer ответное сообщение пользователю
     */
    public static void processCallbackNotIt(CallbackQuery query, SendMessage answer) {
        IParser parser = new HotGameParser();
        var queryData = query.getData();
        var titles = parser.parseTitlesByName(
                queryData.substring(queryData.indexOf("'"), queryData.lastIndexOf("'"))
        );
        var names = new ArrayList<String>(titles.size());
        for (var e : titles) names.add(e.getName());
        var keyboard = KeyboardCreator.createKeyboardMarkUp(1, names, PARSER);
        if (names.size() != 0)
            answer.setText("Тогда вот другие предложения:");
        else
            answer.setText("Мы ничего не нашли(");
        answer.setReplyMarkup(keyboard);
    }
}
