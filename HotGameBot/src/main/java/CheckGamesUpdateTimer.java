import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commands.CreateNotification;

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
     * @param titleSet {@link CheckGamesUpdateTimer#titlesMapping}
     * @param gameSet {@link CheckGamesUpdateTimer#gamesMapping}
     */
    public CheckGamesUpdateTimer(String titlesPath, HashMap<String,Title> titleSet, HashMap<Title, Game> gameSet) {
        this.titlesPath = titlesPath;
        this.titlesMapping = titleSet;
        this.gamesMapping = gameSet;
    }

    @Override
    public void run() {
        checkUpdates(titlesPath, titlesMapping, gamesMapping);
    }

    /**
     * Метод, проверяющий обновления
     * @param titlesPath {@link CheckGamesUpdateTimer#titlesPath}
     * @param titleSet {@link CheckGamesUpdateTimer#titlesMapping}
     * @param gameSet {@link CheckGamesUpdateTimer#gamesMapping}
     */
    private static void checkUpdates(String titlesPath, HashMap<String,Title> titleSet, HashMap<Title, Game> gameSet) {
        HashMap<String,Title> titleSetUpdates = new HashMap<>();
        try {
            titleSetUpdates = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<HashMap<String,Title>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (var titleUpdates : titleSetUpdates.values()) {
            if (titleSet.containsKey(titleUpdates.getName()) &&
                    titleSet.get(titleUpdates.getName()).getPrice() > titleUpdates.getPrice()) {
                System.out.println(CreateNotification.createNotification(gameSet.get(titleUpdates), titleUpdates));
                gameSet.get(titleUpdates).setTitle(titleUpdates);
            }
        }
    }
}
