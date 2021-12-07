package bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardCreator {
    public static InlineKeyboardMarkup createKeyboardMarkUp(String[] buttons) {
        return createKeyboardMarkUp(1, buttons);
    }

    public static InlineKeyboardMarkup createKeyboardMarkUp(String[] buttons, String button) {
        var newButtons = new String[buttons.length + 1];
        System.arraycopy(buttons, 0, newButtons, 0, buttons.length);
        newButtons[buttons.length] = button;
        return createKeyboardMarkUp(1, newButtons);
    }

    public static InlineKeyboardMarkup createKeyboardMarkUp(int columnCount, String[] buttonNames) {
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
        //TODO: setCallbackData со спецсимволом
        if(name.equals("Это не то, чего я хочу"))
            inlineKeyboardButton.setCallbackData("&&".concat(name));
        else inlineKeyboardButton.setCallbackData(name);
        return inlineKeyboardButton;
    }
}