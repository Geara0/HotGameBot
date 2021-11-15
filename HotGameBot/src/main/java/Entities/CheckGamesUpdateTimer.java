package Entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import notifications.CreateNotification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.TimerTask;


/**
 * Таймер, проверяющий обновление бд
 *
 * @author Geara0
 * @version 1.0
 */
public class CheckGamesUpdateTimer extends TimerTask {
    /**
     * Путь до базы тайтлов
     */
    private final String titlesPath;
    /**
     * Старые тайтлы
     */
    private final HashMap<String,Title> titlesMapping;
    /**
     * Старые игры
     */
    private final HashMap<Title, Game> gamesMapping;

    /**
     * Конструктор
     * @param titlesPath {@link CheckGamesUpdateTimer#titlesPath}
     * @param titleMapping {@link CheckGamesUpdateTimer#titlesMapping}
     * @param gameMapping {@link CheckGamesUpdateTimer#gamesMapping}
     */
    public CheckGamesUpdateTimer(String titlesPath, HashMap<String,Title> titleMapping, HashMap<Title, Game> gameMapping) {
        this.titlesPath = titlesPath;
        this.titlesMapping = titleMapping;
        this.gamesMapping = gameMapping;
    }

    @Override
    public void run() {
        checkUpdates(titlesPath, titlesMapping, gamesMapping);
    }

    /**
     * Метод, проверяющий обновления
     * @param titlesPath {@link CheckGamesUpdateTimer#titlesPath}
     * @param titleMapping {@link CheckGamesUpdateTimer#titlesMapping}
     * @param gameMapping {@link CheckGamesUpdateTimer#gamesMapping}
     */
    private static void checkUpdates(String titlesPath, HashMap<String,Title> titleMapping, HashMap<Title, Game> gameMapping) {
        HashMap<String,Title> titleSetUpdates = new HashMap<>();
        try {
            titleSetUpdates = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<HashMap<String,Title>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (var titleUpdates : titleSetUpdates.values()) {
            if (titleMapping.containsKey(titleUpdates.getName()) &&
                    titleMapping.get(titleUpdates.getName()).getPrice() > titleUpdates.getPrice()) {
                System.out.println(CreateNotification.createNotification(gameMapping.get(titleUpdates), titleUpdates));
                gameMapping.get(titleUpdates).setTitle(titleUpdates);
            }
        }
    }
}
