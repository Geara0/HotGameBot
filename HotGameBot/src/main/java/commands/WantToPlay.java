package commands;

import Entities.Title;
import Entities.User;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Класс для команды "хочу поиграть"
 */
public class WantToPlay implements ICommand {
    /**
     * Все игры, известные боту
     */
    private HashMap<String, Title> titleMapping;

    /**
     * Конструктор для инициализации поля с тайтлами
     *
     * @param titles - тайтлы, известные боту
     */
    public WantToPlay(HashMap<String, Title> titles) {
        titleMapping = titles;
    }

    /**
     * Реализация ICommand
     *
     * @param user - пользователь, для которого выполняется команда
     */
    @Override
    public void execute(User user) {
        printMessage(getPriceRecommendations(getUserCash()));
    }

    /**
     * Печатает сообщение
     *
     * @param message - сообщение для печати
     */
    private void printMessage(String message) {
        System.out.print(message);
    }

    /**
     * Формирует рекомендации по цене
     *
     * @param cash - стоимость, для которой ведется отбор
     * @return строка со списком тайтлов, цена которых ниже параметра
     */
    private String getPriceRecommendations(int cash) {
        if (cash < 0)
            return CommandsConst.CANT_FIND_TITLE.toStringValue();
        StringBuilder recs = new StringBuilder();//стрингбилдер для формирования строки с рекомендациями
        for (var title : titleMapping.values())
            if (cash >= title.getPrice())
                recs.append(title.getStringForm());
        String result = recs.toString();
        return result.length() > 0 ? result : CommandsConst.CANT_FIND_TITLE.toStringValue();
    }

    /**
     * Получает от пользователя целое число - сумму для рекомендаций по цене
     *
     * @return целое число - сумма для рекомендаций
     */
    private int getUserCash() {
        System.out.println(CommandsConst.AVAILABLE_RECOMMENDATIONS.toStringValue());
        Scanner scanner = new Scanner(System.in);
        int cash = -1;
        try {
            cash = scanner.nextInt();//может выкинуть эксепшн что ввели не число, поэтому я ловлю их все
        } catch (Exception e) {
            System.out.println(CommandsConst.NOT_WHOLE_NUMBER.toStringValue());//и вывожу что это не число, повторно ввести не прошу
        }
        return cash;
    }
}
