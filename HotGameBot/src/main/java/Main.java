import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class Main {
    private static String titlesPath = "JSONs/AllTitles";
    private static String usersPath = "JSONs/HWUserSubsList";
    private static HashSet<Title> allTitles = new HashSet<>();
    private static HashSet<User> userList;
    private static HashMap<Title, Game> gameSet;
    private static Set<Title> titleSet;

    public Main() {
        try {
            //userList = new ObjectMapper().readValue(usersPath, HashSet.class);
            //titleSet = new GenericParser<Set<Title>>.parse(titlesPath);
            userList = new Gson().fromJson(Files.readString(Path.of(usersPath)), new TypeToken<HashSet<User>>(){}.getType());
            titleSet = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<Set<Title>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameSet = parseUsers(userList);
        TimerTask timerTask = new CheckGamesUpdTimer(titlesPath, titleSet, gameSet);
        // стартуем TimerTask в виде демона

        Timer timer = new Timer(true);
        // будем запускать каждых 10 секунд (10 * 1000 миллисекунд)
        timer.scheduleAtFixedRate(timerTask, 10 * 1000, 10 * 1000);
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
     * @param users пользователи
     * @return множество игр
     */
    public static HashMap<Title, Game> parseUsers(Iterable<User> users) {
        HashMap<Title, Game> gameSet = new HashMap<>();
        HashMap<Title, HashSet<User>> gameList = new HashMap<>();
        for (var user : users) {
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
}

