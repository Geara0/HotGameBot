import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.TimerTask;

//TODO: com.evolvedbinary.j8fu.function ради TriConsumer или так оставить?
public class CheckGamesUpdTimer extends TimerTask {
    String titlesPath;
    Set<Title> titleSet;
    HashMap<Title, Game> gameSet;

    public CheckGamesUpdTimer(String titlesPath, Set<Title> titleSet, HashMap<Title, Game> gameSet) {
        this.titlesPath = titlesPath;
        this.titleSet = titleSet;
        this.gameSet = gameSet;
    }

    @Override
    public void run() {
        checkUpd(titlesPath, titleSet, gameSet);
    }

    private static void checkUpd(String titlesPath, Set<Title> titleSet, HashMap<Title, Game> gameSet) {
        Set<Title> titleSetUpd = new Set<>();
        try {
            titleSetUpd = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<Set<Title>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (var titleUpd : titleSetUpd) {
            if (titleSet.contains(titleUpd) &&
                    titleSet.get(titleUpd).Price > titleUpd.Price) {
                System.out.println(Main.createNotification(gameSet.get(titleUpd), titleUpd));
                gameSet.get(titleUpd).updateGameData(titleUpd);
            }
        }
    }
}
