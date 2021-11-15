package Entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import notifications.NotificationCreator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
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
    private final AbstractMap<String, Title> titlesMapping;
    /**
     * Старые игры
     */
    private final AbstractMap<Title, Game> gamesMapping;

    /**
     * Конструктор
     *
     * @param titlesPath   {@link CheckGamesUpdateTimer#titlesPath}
     * @param titleMapping {@link CheckGamesUpdateTimer#titlesMapping}
     * @param gameMapping  {@link CheckGamesUpdateTimer#gamesMapping}
     */
    public CheckGamesUpdateTimer(String titlesPath, AbstractMap<String, Title> titleMapping, AbstractMap<Title, Game> gameMapping) {
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
     *
     * @param titlesPath   {@link CheckGamesUpdateTimer#titlesPath}
     * @param titleMapping {@link CheckGamesUpdateTimer#titlesMapping}
     * @param gameMapping  {@link CheckGamesUpdateTimer#gamesMapping}
     */
    private static void checkUpdates(String titlesPath, AbstractMap<String, Title> titleMapping, AbstractMap<Title, Game> gameMapping) {
        HashMap<String, Title> titleSetUpdates = new HashMap<>();
        try {
            titleSetUpdates = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<HashMap<String, Title>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (var titleUpdates : titleSetUpdates.values()) {
            var updatedTitleName = titleUpdates.getName();
            if (titleMapping.containsKey(updatedTitleName) &&
                    titleMapping.get(updatedTitleName).getPrice() > titleUpdates.getPrice()) {
                System.out.println(NotificationCreator.create(gameMapping.get(titleUpdates), titleUpdates));
                gameMapping.get(titleUpdates).setTitle(titleUpdates);
            }
        }
    }
}
