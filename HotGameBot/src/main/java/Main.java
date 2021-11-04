import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    private static final String titlesPath = ".\\JSONs\\Titles";
    private static final String usersPath = ".\\JSONs\\/HWUserSubsList";
    private static final HashSet<Title> allTitles = new HashSet<>();
    private static HashMap<String,User> usersMapper;
    private static HashMap<Title, Game> gamesMapper;
    private static HashMap<String,Title> titlesMapper;

    /**
     * Конструктор, запускающий таймер и инициализирующий список пользователей и тайтлов
     *
     * @see CheckGamesUpdateTimer
     */
    public Main() {
        try {
            //userList = new ObjectMapper().readValue(usersPath, HashSet.class);
            //titleSet = new GenericParser<Set<Title>>.parse(titlesPath);
            usersMapper = new Gson().fromJson(Files.readString(Path.of(usersPath)), new TypeToken<HashMap<String,User>>(){}.getType());
            titlesMapper = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<HashMap<String,Title>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        gamesMapper = parseUsers(usersMapper);
    }

    /**
     * Функция, отправляющая уведомление
     *
     * @param game игра, для которой нужно вывести уведомление
     * @return уведомление
     */

    /**
     * Функция, парсящая данные пользователей
     *
     * @param users пользователи
     * @return множество игр
     */
    public static HashMap<Title, Game> parseUsers(HashMap<String,User> users) {
        HashMap<Title, Game> gameSet = new HashMap<>();
        HashMap<Title, HashSet<User>> gameList = new HashMap<>();
        for (var user : users.values()) {
            for (var title : user.getTitles().values()) {
                if (!gameList.containsKey(title))
                    gameList.put(title, new HashSet<>());

                gameList.get(title).add(user);
            }
        }

        for (var game : gameList.entrySet())
            gameSet.put(game.getKey(), new Game(game.getKey(), game.getValue()));
        return gameSet;
    }

    /**
     * Функция, парсящая данные пользователей
     *
     * @param userData данные пользователей
     * @return множество пользователей
     */
    public static HashSet<User> parseUserData(Iterable<User> userData) {
        HashSet<User> userList = new HashSet<>();
        for (var user : userData) {
            userList.add(user);
            allTitles.addAll(user.getTitles().values());
        }
        return userList;
    }

    /**
     * Метод main для запуска программы
     *
     * @param args стандартный параметр
     */
    public static void main(String[] args){
        new Main();
        startTimer();
        var scanner = new Scanner(System.in);
        User currentUser = getUser();
        System.out.println(getHelp());
        while(true){
            String input = scanner.next();
            if(input.equals("/quit"))
                break;
            switch (input) {
                case "/help" -> System.out.println(getHelp());
                case "/wantToPlay" -> {
                    System.out.println("Пока доступны только подборки по цене, введите желаемую стоимость");
                    var cash = scanner.nextInt();
                    for (var title : titlesMapper.values()) {
                        if (cash >= title.getPrice())
                            System.out.println(title);
                    }
                }
                case "/sub" -> {
                    System.out.println("Введите название тайтла, на который хотите попдисаться");
                    String name = getClosestName();
                    if(!name.equals("stop")) {
                        currentUser.Watch(titlesMapper.get(name));
                    }
                }
                case "/unsub" -> {
                    System.out.println("Введите название тайтла, от которого хотите отписаться");
                    String name = getClosestName();
                    currentUser.Unwatch(titlesMapper.get(name));
                }
                case "/mySubs" -> {
                    for(var title : currentUser.getTitles().values()){
                        System.out.println(title);
                    }
                }
                case "/quit" -> {
                    System.out.println("Вы вышли!");
                }
                default -> System.out.println("Введенное вами сообщение не является командой");
            }
        }
    }

    public static void startTimer(){
        Timer timer = new Timer(true);
        TimerTask timerTask = new CheckGamesUpdateTimer(titlesPath, titlesMapper, gamesMapper);
        timer.scheduleAtFixedRate(timerTask, 10 * 1000, 10 * 1000);
    }

    public static User getUser(){
        var sc = new Scanner(System.in);
        System.out.println("Введите имя пользователя ");
        var username = sc.next();
        if(!usersMapper.containsKey(username)){
            usersMapper.put(username,new User(username,new HashMap<>()));
        }
        return usersMapper.get(username);
    }

    public static String getHelp(){
        return """
                Доступны следующие команды:\r
                1. /wantToPlay переключит вас на рекомендации\r
                2. /sub позволит вам добавить тайтл в ваши подписки\r
                3. /quit выведет вас из меню\r
                4. /unsub позволит удалить тайтл из ваших подписок\r
                5. /mySubs выводит ваши подписки
                """;
    }

    public static String getClosestName(){
        String answer;
        String name="";
        var sc = new Scanner(System.in);
        do {
            System.out.println("Введите название тайтла(как можно точнее)");
            var toSearch = sc.nextLine();
            int minDist = Integer.MAX_VALUE;
            for (var title : titlesMapper.values()) {
                var dist = Levenshtein.levenshtein(toSearch, title.getName(), false);
                if (dist < minDist) {
                    minDist = dist;
                    name = title.getName();
                }
                if(minDist==0)
                    break;
            }
            System.out.println("Это тот тайтл, который вы искали(yes/no/stop)?\r\n" + titlesMapper.get(name));
            answer = sc.next();
        }while(!(Objects.equals(answer, "yes") | Objects.equals(answer,"stop")));
        return name;
    }
}

