package bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardCreator {
    public static InlineKeyboardMarkup createKeyboardMarkUp(Iterable<String> buttons) {
        return createKeyboardMarkUp(1, buttons);
    }


    public static InlineKeyboardMarkup createKeyboardMarkUp(int columnCount, Iterable<String> buttonNames, KeyboardMarkupTypes type) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        var keyboardRow = new ArrayList<InlineKeyboardButton>();
        var count = 0;
        for (var buttonName : buttonNames) {
            if (count >= columnCount) {
                keyboard.add(new ArrayList<>(keyboardRow));
                keyboardRow.clear();
                count = 0;
            }
            keyboardRow.add((createButton(buttonName, type)));
            count++;
        }
        if (!keyboardRow.isEmpty()) {
            keyboard.add(keyboardRow);
        }
        return new InlineKeyboardMarkup(keyboard);
    }

    public static InlineKeyboardMarkup createKeyboardMarkUp(int columnCount, Iterable<String> buttonNames) {
        return createKeyboardMarkUp(columnCount, buttonNames, KeyboardMarkupTypes.DEFAULT);
    }

    private static InlineKeyboardButton createButton(String name, KeyboardMarkupTypes type) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(name);
        if (type == KeyboardMarkupTypes.DB) {
            if (name.contains("Это не то"))
                inlineKeyboardButton.setCallbackData(KeyboardMarkupTypes.NOT_IT.toStringValue().concat(name));
            else inlineKeyboardButton.setCallbackData(type.toStringValue());
        } else
            inlineKeyboardButton.setCallbackData(type.toStringValue());
        inlineKeyboardButton.setCallbackData(name);
        return inlineKeyboardButton;
    }
}