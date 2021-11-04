import java.util.*;

/**
 * Класс для взаимодействия с пользователем
 */
public class UserInteractor {
    /**
     * Поле в которое передается идентичное из Main, нужно для работы printRecommendations() и subUserToTitle()
     */
    private final HashMap<String,Title> titleMap;
    /**
     * Поле текущего пользователя, хранит экземпляр текущего пользователя
     */
    private final User currentUser;
    private final Scanner mainScanner = new Scanner(System.in);

    /**
     * Конструктор класса, инициализирует поля titleMap и currentUser и выводит справку,
     * потому что взаимодействие пользователя с ботом начинается только после инициализации интерактора
     * @param titleMap - ссылка на аналогичное поле в Main
     * @param curUser - пользователь для взаимодействия, передается из Main
     */
    public UserInteractor(HashMap<String,Title> titleMap, User curUser){
        this.titleMap = titleMap;
        this.currentUser = curUser;
        printHelp();
    }

    /**
     * Основной метод класса, принимает и обрабатывает пользовательский ввод
     * @return строку-состояние:"quit" если пользователь закончил работу;"fine"(заглушка, может быть любая строка) если
     * работа продолжается
     */
    public String processUserInput(){
        String userInput = getInput();
        if("/quit".equals(userInput))
            return "quit";
        switch (userInput){
            case "/help" -> printHelp();
            case "/wantToPlay", "/wanttoplay" -> printPriceRecs();
            case "/sub" -> subUserToTitle();
            case "/unsub", "/unSub" -> unsubUserFromTitle();
            case "/mySubs", "/mysubs" -> printUserSubs();
            default -> System.out.println("Ваш ввод не является командой");
        }
        return "fine";
    }

    /**
     * Получает пользовательский ввод из консоли
     * @return комманда, введенная пользователем
     */
    private String getInput(){
        System.out.println("\r\nВведите комманду");
        return mainScanner.nextLine().trim();
    }

    /**
     * Печатает сообщение справки
     */
    private void printHelp(){
        System.out.print(getHelpMessage());
    }

    /**
     * Формирует сообщение справки
     * @return строка-справка
     */
    private String getHelpMessage(){
        return """
                Доступны следующие команды:\r
                1. /wantToPlay переключит вас на рекомендации\r
                2. /sub позволит вам добавить тайтл в ваши подписки\r
                3. /quit выведет вас из меню\r
                4. /unsub позволит удалить тайтл из ваших подписок\r
                5. /mySubs выводит ваши подписки
                """;
    }

    /**
     * Печатает подписки пользователей
     * Если подписок нет, печатает что подписок нет
     */
    private void printUserSubs(){
        if(currentUser.getTitles().values().size()==0) {
            System.out.println("У вас пока нет подписок");
            return;
        }
        var i=1;
        for(var title : currentUser.getTitles().values())
            System.out.println(String.format("#%d ---- ",i++ ) + title.getStringForm());
    }

    /**
     * Печатает рекомендации по цене
     */
    private void printPriceRecs(){
        System.out.println("Пока доступны только рекомендации по цене\r\nВведите жалемую стоимость в целых числах");
        int cash = -1;
        try{
            cash = mainScanner.nextInt();//может выкинуть эксепшн что ввели не число, поэтому я ловлю их все
        }
        catch (Exception e){
            System.out.println("Ваш ввод не является целым числом");//и вывожу что это не число, повторно ввести не прошу
        }
        StringBuilder recs = new StringBuilder();//стрингбилдер для формирования строки с рекомендациями
        for(var title : titleMap.values())
            if(cash>=title.Price)
                recs.append(title.getStringForm()).append("\r\n");
        //тернарник вернет строку с рекомендациями если в ней что-то есть, если нет, вернет заглушку что тайтлов нет
        String result = recs.length()>0 ? recs.toString() : "Нет тайтлов по подходящей цене";
        System.out.print(result);
        mainScanner.nextLine();//нужно для того чтоб getInput() не прочитал пустую строку как команду
    }

    /**
     * Подписывает пользователя на тайтл
     */
    private void subUserToTitle(){
        var titleName = getClosestName();
        if(!"stop".equals(titleName))
            currentUser.Watch(titleMap.get(titleName));
    }

    /**
     * Отписывает пользователя от тайтла
     */
    private void unsubUserFromTitle(){
        System.out.println("Тайтлы, на которые вы подписаны:");
        var titleArray = currentUser.getTitles().values().toArray(new Title[]{});
        printUserSubs();
        System.out.println("Выберите тайтл для отписки по индексу");
        int index = getIndex(titleArray.length);
        currentUser.Unwatch(titleArray[index]);
        mainScanner.nextLine();//нужно для того чтоб getInput() не прочитал пустую строку как команду
    }

    /**
     * Метод нужен для контроля ввода индекса в методе unsubUserFromTitle()
     * @param upperBorder - верхняя граница, индекс должен быть строго меньше
     * @return целочисленный индекс в пределах от 0 до верхней границы
     */
    private int getIndex(int upperBorder){
        int index = -1;
        while(index<0 | index>=upperBorder){
            try{
                index = mainScanner.nextInt();//может создать исключение если введено не целое число
                index--;
            }
            catch (Exception e){
                System.out.println("Введнный индекс некорректен");//и тут я их отлавливаю и говорю что индекс некорректный
            }
        }
        return index;
    }

    /**
     * Возвращает ближайшее(по расстоянию Левенштайна) имя тайтла к тому, которое ввел пользователь
     * @return строку - имя тайтла
     */
    private String getClosestName(){
        var name="";
        var answer="";
        do {
            System.out.println("Введите название игры как можно точнее");
            var nameToCompare = mainScanner.nextLine();
            int minDist = Integer.MAX_VALUE;
            for(var title : titleMap.values()){
                var dist = Levenshtein.levenshtein(nameToCompare, title.getName(), false);
                if(dist<minDist){
                    minDist=dist;
                    name = title.getName();
                }
                if(minDist==0)
                    break;
            }
            System.out.println("Это тот тайтл, который вы искали(yes/no/stop)?\r\n" + titleMap.get(name));
            answer = mainScanner.nextLine();
        }while(!(answer.equals("yes") | answer.equals("stop")));
        return answer.equals("stop") ? "stop" : name;
    }
}
