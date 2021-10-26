import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.TimerTask;

//TODO: com.evolvedbinary.j8fu.function ради TriConsumer или так оставить?

/**
 * Таймер, проверяющий обновление бд
 *
 * @author Geara0
 * @version 1.0
 */
public class CheckGamesUpdTimer extends TimerTask {
    /**
     * Путь до базы тайтлов
     */
    String titlesPath;
    /**
     * Старые тайтлы
     */
    HashMap<String,Title> titleSet;
    /**
     * Старые игры
     */
    HashMap<Title, Game> gameSet;

    /**
     * Конструктор
     * @param titlesPath {@link CheckGamesUpdTimer#titlesPath}
     * @param titleSet {@link CheckGamesUpdTimer#titleSet}
     * @param gameSet {@link CheckGamesUpdTimer#gameSet}
     */
    public CheckGamesUpdTimer(String titlesPath, HashMap<String,Title> titleSet, HashMap<Title, Game> gameSet) {
        this.titlesPath = titlesPath;
        this.titleSet = titleSet;
        this.gameSet = gameSet;
    }

    @Override
    public void run() {
        checkUpd(titlesPath, titleSet, gameSet);
    }

    /**
     * Метод, проверяющий обновления
     * @param titlesPath {@link CheckGamesUpdTimer#titlesPath}
     * @param titleSet {@link CheckGamesUpdTimer#titleSet}
     * @param gameSet {@link CheckGamesUpdTimer#gameSet}
     */
    private static void checkUpd(String titlesPath, HashMap<String,Title> titleSet, HashMap<Title, Game> gameSet) {
        HashMap<String,Title> titleSetUpd = new HashMap<>();
        try {
            titleSetUpd = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<HashMap<String,Title>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (var titleUpd : titleSetUpd.values()) {
            if (titleSet.containsKey(titleUpd.getName()) &&
                    titleSet.get(titleUpd.getName()).Price > titleUpd.Price) {
                System.out.println(Main.createNotification(gameSet.get(titleUpd), titleUpd));
                gameSet.get(titleUpd).updateGameData(titleUpd);
            }
        }
    }
}
