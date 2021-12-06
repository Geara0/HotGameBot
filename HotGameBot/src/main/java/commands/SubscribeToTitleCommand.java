package commands;

import entities.Levenshtein.LevenshteinCalculator;
import entities.Title;
import entities.User;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * Класс команды для подписки пользователя на игру
 */
public class SubscribeToTitleCommand implements ICommand {

    /**
     * Поле со всеми тайтлами которые знает бот, чтобы среди них искать тайтл для пользователя
     */
    private final HashMap<String, Title> titleMapping;

    /**
     * Отдельное поле для сета имен тайтлов, чтобы не каждой итерации поиска не создавать его заново
     */
    private final Set<String> titleNamesSet;

    /**
     * Сканнер
     */
    private final Scanner scanner;

    /**
     * Конструктор команды, иницилизирует хешмап тайтлов, сет имен тайтлов и сканнер
     *
     * @param titles - хешмап всех тайтлов которые знает бот
     */
    public SubscribeToTitleCommand(HashMap<String, Title> titles) {
        titleMapping = titles;
        titleNamesSet = titles.keySet();
        scanner = new Scanner(System.in);
    }

    /**
     * Реализация интерфейса ICommand, метод для выполнения команды
     *
     * @param user - пользователь, для которого команда выполняется
     */
    @Override
    public void execute(User user) {
        String titleName = getTitleName();
        subUserToTitle(titleName, user);
    }

    /**
     * Метод, подписывающий пользователя на тайтл
     *
     * @param titleName - имя тайтла, на которой надо подписать
     * @param user      - пользователь, которого надо подписать
     */
    private void subUserToTitle(String titleName, User user) {
        if (CommandsConstants.STOPPING_LINE.toStringValue().equals(titleName))
            return;
        user.Watch(titleMapping.get(titleName));
    }

    /**
     * Метод в цикле просит у пользователя ввести название тайтла, и подбирает ближайшее к нему из известных боту
     *
     * @return имя тайтла, которое хотел найти пользователь
     */
    private String getTitleName() {
        AnswerConstants answer;
        String name;
        do {
            var input = getUserInput();
            name = new LevenshteinCalculator().getClosestString(titleNamesSet, input);
            answer = askUserAboutTitle(name);
        } while (!AnswerConstants.YES.equals(answer) && !AnswerConstants.STOP.equals(answer));
        return answer.equals(AnswerConstants.YES) ? name : CommandsConstants.STOPPING_LINE.toStringValue();
    }

    /**
     * Спрашивает пользователя, правильный ли тайтл нашел бот, и возвращает ответ
     *
     * @param name - имя тайтла, о котором надо спросить
     * @return Answer.YES если пользователь согласен, Answer.NO если пользователь не согласен,
     * Answer.STOP если пользователь хочет закончить перебор
     */
    private AnswerConstants askUserAboutTitle(String name) {
        String titleString = titleMapping.get(name).getStringForm();
        System.out.println(CommandsConstants.IS_THE_RIGHT_TITLE_WITH_OPTIONS.toStringValue() + "\r\n" + titleString);
        var answer = scanner.nextLine().toLowerCase();
        return switch (answer) {
            case "yes" -> AnswerConstants.YES;
            case "no" -> AnswerConstants.NO;
            case "stop" -> AnswerConstants.STOP;
            default -> null;
        };
    }

    /**
     * Запрашивает у пользователя название тайтла
     *
     * @return введенное пользователем название тайтла в виде строки
     */
    private String getUserInput() {
        System.out.println("Введите название тайтла как можно точнее");
        return scanner.nextLine().toLowerCase();
    }
}