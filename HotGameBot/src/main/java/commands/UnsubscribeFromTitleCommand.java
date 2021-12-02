package commands;

import entities.Title;
import entities.User;

import java.util.Scanner;

/**
 * Класс с командой для отписки пользователя от тайтла
 */
public class UnsubscribeFromTitleCommand implements ICommand {

    /**
     * Сканнер
     */
    private final Scanner scanner;

    /**
     * Конструктор с инициализацией сканнера
     */
    public UnsubscribeFromTitleCommand() {
        scanner = new Scanner(System.in);
    }

    /**
     * Реализация ICommand
     *
     * @param user - пользователь, для которого выполняется команда
     */
    @Override
    public void execute(User user) {
        unsubscribeUserFromTitle(user);
    }

    /**
     * Метод, отписывающий пользователя от тайтла(немного спагетти, но работает)
     *
     * @param user - пользователь, для которого выполняется команда
     */
    private void unsubscribeUserFromTitle(User user) {
        System.out.println("Тайтлы, на которые вы подписаны:");

        Title[] userTitles = user.getTitles().values().toArray(new Title[]{});
        //чтобы была возможность выбрать тайтл по индексу
        //мне кажется так получится быстрее и удобнее чем в SubscribeToTitle с подбором по имени

        if (userTitles.length == 0)
            return;

        new MySubsCommand().execute(user);//вывод подписок пользователя, удобно получилось
        System.out.println("Выберите тайтл для отписки по индексу");
        int index = getIndex(userTitles.length);
        user.Unwatch(userTitles[index]);
    }

    /**
     * Получает от пользователя индекс тайтла из выведенного на экран списка
     *
     * @param upperBorder - верхняя граница списка чтоб проверять выход за пределы
     * @return целое число - индекс тайтла, от которого хочет отписаться пользователь
     */
    private int getIndex(int upperBorder) {
        int index = -1;
        do {
            try {
                index = Integer.parseInt(scanner.nextLine());//может создать исключение если введено не целое число
                index--;
                if (isNotInBounds(index, upperBorder))
                    throw new Exception();
            } catch (Exception e) {
                System.out.println(CommandsConstants.INCORRECT_INDEX.toStringValue());//и тут я их отлавливаю и говорю что индекс некорректный
            }
        } while (isNotInBounds(index, upperBorder));
        return index;
    }

    /**
     * Простой метод для проверки принадлежности числа отрезку [0;upperBorder]
     *
     * @param i           - число для проверки
     * @param upperBorder - верхня граница
     * @return true если число внутри отрезка, false если нет
     */
    private boolean isNotInBounds(int i, int upperBorder) {
        return !(i > -1 & i < upperBorder);
    }
}
