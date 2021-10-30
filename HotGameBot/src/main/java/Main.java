import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    private static final String titlesPath = ".\\JSONs\\Titles";
    private static final String usersPath = ".\\JSONs\\HWUserSubsList";
    private static final HashSet<Title> allTitles = new HashSet<>();
    private static HashMap<String, User> userMap;
    private static HashMap<Title, Game> gameMap;
    private static HashMap<String, Title> titleMap;

    /**
     * Конструктор, инициализирующий поля userMap, titleMap & gameMap
     */
    public Main() {
        try {
            userMap = new Gson().fromJson(Files.readString(Path.of(usersPath)), new TypeToken<HashMap<String, User>>() {
            }.getType());
            titleMap = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<HashMap<String, Title>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameMap = parseUsers(userMap);
    }

    /**
     * Функция, отправляющая уведомление
     *
     * @param game игра, для которой нужно вывести уведомление
     * @return уведомление
     */
    public static String createNotification(Game game, Title titleUpd) {
        String notification = null;
        Title title = game.title;
        if (titleUpd.Price < title.Price) {
            notification = String.format("New best price for %s:\n Prev: %s,\nCurrent: %s\nHotGame link: %s,\nBuy link: %s",
                    title.getName(), title.Price, titleUpd.Price, title.getLink(), titleUpd.BuyLink);
        }
        return notification;
    }

    /**
     * Функция, парсящая данные пользователей
     *
     * @param users - словарь пользователей, ключи - никнеймы
     * @return множество игр
     */
    public static HashMap<Title, Game> parseUsers(HashMap<String, User> users) {
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
     * Вроде бы неиспользуемая функция, но пока не удаляю
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
    public static void main(String[] args) {
        new Main();
        startTimer();
        User currentUser = getUser();
        UserInteractor interactor = new UserInteractor(titleMap, currentUser);
        while (true) {
            var status = interactor.processUserInput();
            if ("quit".equals(status))
                break;
        }
        writeUserSubs(userMap);
    }

    /**
     * Метод для записи данных пользователей в файл
     * @param userMap - словарь с экземплярами пользователей
     */
    public static void writeUserSubs(HashMap<String,User> userMap){
        try {
            Files.writeString(Path.of(usersPath),new Gson().toJson(userMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метоод для старта таймера проверки обновления информации о тайтлах
     */
    public static void startTimer() {
        Timer timer = new Timer(true);
        TimerTask timerTask = new CheckGamesUpdTimer(titlesPath, titleMap, gameMap);
        timer.scheduleAtFixedRate(timerTask, 10 * 1000, 10 * 1000);
    }

    /**
     * Метод для получения экземпляра пользователя
     * Находит пользователя по username или создает экземпляр нового и добавялет его в словарь
     * @return экземпляр пользователя, никнейм которого был введен
     */
    public static User getUser() {
        var sc = new Scanner(System.in);
        System.out.println("Введите имя пользователя ");
        var username = sc.next();
        if (!userMap.containsKey(username)) {
            userMap.put(username, new User(username, new HashMap<>()));
        }
        return userMap.get(username);
    }
}

