import Entities.CheckGamesUpdateTimer;
import Entities.Game;
import Entities.Title;
import Entities.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commands.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConsoleGamesService {
    private final String titlesPath = ".\\JSONs\\Titles";
    private final String usersPath = ".\\JSONs\\HWUserSubsList";
    private final HashMap<Title, Game> gamesMapper;
    private HashMap<String, User> usersMapper;
    private HashMap<String, Title> titlesMapper;
    private HashMap<String, ICommand> commands;
    private boolean isThereAUser;
    private boolean isRunning;

    public ConsoleGamesService() {
        try {
            usersMapper = new Gson().fromJson(Files.readString(Path.of(usersPath)), new TypeToken<HashMap<String, User>>() {
            }.getType());
            titlesMapper = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<HashMap<String, Title>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        gamesMapper = parseUsers(usersMapper);
        commands = new HashMap<>();
        fillCommands();
        isThereAUser = false;
        isRunning = false;
    }

    public boolean isThereAUser() {
        return isThereAUser;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private HashMap<Title, Game> parseUsers(HashMap<String, User> users) {
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

    private void fillCommands() {
        commands.put("/help", new Help());
        commands.put("/mysubs", new MySubs());
        commands.put("/sub", new SubscribeToTitle(titlesMapper));
        commands.put("/unsub", new UnsubscribeFromTitle());
        commands.put("/wanttoplay", new WantToPlay(titlesMapper));
        commands.put("/quit", new Quit());
    }

    public void runCommand(String message, User currentUser) {
        try{
            ICommand command = commands.get(message.toLowerCase());
            command.execute(currentUser);
        }
        catch(NullPointerException ex){
            System.out.println("Введенное вами сообщение не является командой, введите /help для помощи");
        }
        isThereAUser = currentUser.isActive();
    }

    public void start() {
        startTimer();
        isRunning = true;
    }

    private void startTimer() {
        Timer timer = new Timer(true);
        var timerTask = new CheckGamesUpdateTimer(titlesPath, titlesMapper, gamesMapper);
        timer.scheduleAtFixedRate(timerTask, 10 * 1000, 10 * 1000);
    }

    public void stop() {
        writeUserSubs(usersMapper);
        isRunning = false;
    }

    private void writeUserSubs(HashMap<String, User> userMapping) {
        try {
            Files.writeString(Path.of(usersPath), new Gson().toJson(userMapping));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        var sc = new Scanner(System.in);
        System.out.println("Введите имя пользователя или /stop чтобы остановить бота");
        var username = sc.nextLine();
        if("/stop".equals(username)){
            stop();
            return null;
        }
        if (!usersMapper.containsKey(username))
            usersMapper.put(username, new User(username, new HashMap<>()));
        isThereAUser = true;
        User user = usersMapper.get(username);
        user.setActive(true);
        runCommand("/help",user);
        return user;
    }
}
