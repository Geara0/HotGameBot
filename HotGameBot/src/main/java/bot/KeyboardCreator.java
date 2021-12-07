package bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardCreator {
    public static InlineKeyboardMarkup createKeyboardMarkUp(Iterable<String> buttons) {
        return createKeyboardMarkUp(1, buttons);
    }

    public static InlineKeyboardMarkup createdbKeyboardMarkUp(int columnCount, Iterable<String> buttonNames) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        var keyboardRow = new ArrayList<InlineKeyboardButton>();
        var count = 0;
        for (var buttonName : buttonNames) {
            if (count >= columnCount) {
                keyboard.add(new ArrayList<>(keyboardRow));
                keyboardRow.clear();
                count = 0;
            }
            keyboardRow.add((createdbButton(buttonName)));
            count++;
        }
        if (!keyboardRow.isEmpty()) {
            keyboard.add(keyboardRow);
        }
        return new InlineKeyboardMarkup(keyboard);
    }

    private static InlineKeyboardButton createdbButton(String name) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(name);
        if (name.contains("Это не то"))
            inlineKeyboardButton.setCallbackData("##".concat(name));
        else inlineKeyboardButton.setCallbackData("$$".concat(name));
        return inlineKeyboardButton;
    }

    public static InlineKeyboardMarkup createParsedKeyboardMarkUp(int columnCount, Iterable<String> buttonNames) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        var keyboardRow = new ArrayList<InlineKeyboardButton>();
        var count = 0;
        for (var buttonName : buttonNames) {
            if (count >= columnCount) {
                keyboard.add(new ArrayList<>(keyboardRow));
                keyboardRow.clear();
                count = 0;
            }
            keyboardRow.add((createParsedButton(buttonName)));
            count++;
        }
        if (!keyboardRow.isEmpty()) {
            keyboard.add(keyboardRow);
        }
        return new InlineKeyboardMarkup(keyboard);
    }

    private static InlineKeyboardButton createParsedButton(String name) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(name);
        inlineKeyboardButton.setCallbackData("%%".concat(name));
        return inlineKeyboardButton;
    }

    public static InlineKeyboardMarkup createKeyboardMarkUp(int columnCount, Iterable<String> buttonNames) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        var keyboardRow = new ArrayList<InlineKeyboardButton>();
        var count = 0;
        for (var buttonName : buttonNames) {
            if (count >= columnCount) {
                keyboard.add(new ArrayList<>(keyboardRow));
                keyboardRow.clear();
                count = 0;
            }
            keyboardRow.add((createButton(buttonName)));
            count++;
        }
        if (!keyboardRow.isEmpty()) {
            keyboard.add(keyboardRow);
        }
        return new InlineKeyboardMarkup(keyboard);
    }

    private static InlineKeyboardButton createButton(String name) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(name);
        inlineKeyboardButton.setCallbackData(name);
        return inlineKeyboardButton;
    }
}