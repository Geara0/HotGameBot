package bot;

import botCommands.CommandsConstants;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.management.Notification;
import java.util.ArrayList;
import java.util.List;

import static bot.KeyboardMarkupTypes.*;


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
        return createKeyboardMarkUp(columnCount, buttonNames, DEFAULT);
    }


    public static InlineKeyboardMarkup createConfirmationKeyboard(KeyboardMarkupTypes type, String titleName) {
        //TODO: это точно надо в лог
        if (type != CONFIRM_UNSUB) return null;
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        var keyboardRow = new ArrayList<InlineKeyboardButton>();
        keyboardRow.add(createButton("Да", type, titleName));
        keyboardRow.add(createButton("Нет", type, titleName));
        keyboard.add(keyboardRow);
        return new InlineKeyboardMarkup(keyboard);
    }

    public static InlineKeyboardButton createButton(String name, KeyboardMarkupTypes type, String callbackData) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(name);
        inlineKeyboardButton.setCallbackData(type.toStringValue() + callbackData);
        return inlineKeyboardButton;
    }


    private static InlineKeyboardButton createButton(String name, KeyboardMarkupTypes type) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(name);
        if (type == DB && name.contains(CommandsConstants.NOT_IT.toStringValue())) {
            inlineKeyboardButton.setCallbackData(NOT_IT.toStringValue() + name);
            return inlineKeyboardButton;
        }
        inlineKeyboardButton.setCallbackData(type.toStringValue().concat(name));
        return inlineKeyboardButton;
    }
}