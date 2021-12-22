package bot;

import botCommands.CommandsConstants;
import db.DBWorker;
import db.IDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static bot.KeyboardMarkupTypes.*;


public class KeyboardCreator {
    public static InlineKeyboardMarkup createKeyboardMarkUp(Iterable<String> buttons) {
        return createKeyboardMarkUp(1, buttons);
    }

    private final static Logger logger = LogManager.getLogger("keyboard");


    public static InlineKeyboardMarkup createKeyboardMarkUpById(int columnCount, Iterable<Long> buttonIds, KeyboardMarkupTypes type) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        var keyboardRow = new ArrayList<InlineKeyboardButton>();
        var count = 0;
        for (var buttonId : buttonIds) {
            if (count >= columnCount) {
                keyboard.add(new ArrayList<>(keyboardRow));
                keyboardRow.clear();
                count = 0;
            }
            keyboardRow.add((createButton(buttonId, type)));
            count++;
        }
        if (!keyboardRow.isEmpty()) {
            keyboard.add(keyboardRow);
        }
        return new InlineKeyboardMarkup(keyboard);
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
        if (type != CONFIRM_UNSUB) {
            logger.warn("keyboardType does not match, required CONFIRM_UNSUB, received: {} \r\n return null",type.toStringValue());
            return null;
        }
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

    private static InlineKeyboardButton createButton(Long id, KeyboardMarkupTypes type) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        IDB db = new DBWorker();
        var name = db.getName(id);
        inlineKeyboardButton.setText(name);
        inlineKeyboardButton.setCallbackData(type.toStringValue() + id);
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