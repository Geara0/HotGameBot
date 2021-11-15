import Entities.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commands.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Класс с логикой работы бота
 */
public class ConsoleGamesService {
    /**
     * Пути до жсон файлов с данными о известных боту тайтлах и данными о подписках пользователей
     */
    private final String titlesPath = ".\\JSONs\\Titles";
    private final String usersPath = ".\\JSONs\\HWUserSubsList";
    /**
     * словари пользователей и тайтлов
     */
    private final HashMap<Title, Game> gamesMapper;
    /**
     * словарь команд
     */
    private final Map<String, ICommand> commands = new HashMap<>();
    /**
     * Словарь с данными о пользователях(пары "имя пользователя":"пользователь")
     */
    private HashMap<String, User> usersMapper;
    /**
     * Словарь с данными о тайтлах(пары "название тайтла":"тайтл")
     */
    private HashMap<String, Title> titlesMapper;
    /**
     * флаги для работы бота на наличие пользователя и активности бота в целом
     */
    private boolean hasUser;
    private boolean isRunning;
    private final int timerDelay = 10 * 1000;
    private final int timerPeriod = 10 * 1000;

    /**
     * конструктор с чтением файлов из жсонов в словари пользователей и игр
     * словарь с парами Title:Game заполняется отдельным методом
     * Инициализируется и заполняется словарь с командами
     * Инициализируются флаги
     */
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
        fillCommands();
    }

    /**
     * геттер для флага наличия активного пользователя
     *
     * @return - true если пользователь работает с ботом, false если нет
     */
    public boolean hasUser() {
        return hasUser;
    }

    /**
     * геттер для флага активности бота
     *
     * @return - true если бот ждет команд, false если бот выключен
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * метод для заполнения словаря Title:Game по словарю String:User
     *
     * @param users - словарь со списком пользователей
     * @return словарь с парами Title:Game
     */
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

    /**
     * метод для заполнения словаря команд
     */
    private void fillCommands() {
        commands.put("/help", new HelpCommand());
        commands.put("/mysubs", new MySubsCommand());
        commands.put("/sub", new SubscribeToTitleCommand(titlesMapper));
        commands.put("/unsub", new UnsubscribeFromTitleCommand());
        commands.put("/wanttoplay", new WantToPlayCommand(titlesMapper));
        commands.put("/quit", new QuitCommand());
    }

    /**
     * метод, выполняющий команду
     *
     * @param message     - сообщение пользователя, которое должно быть интерпретировано в команду
     * @param currentUser - текущий пользователь, для которого должна быть выполнена команда
     */
    public void runCommand(String message, User currentUser) {
        try {
            ICommand command = commands.get(message.toLowerCase());
            command.execute(currentUser);
        } catch (NullPointerException ex) {
            System.out.println("Введенное вами сообщение не является командой, введите /help для помощи");
        }
        hasUser = currentUser.isActive();//три строчки комментариев ниже относятся только к этому присваиванию
        //когда активный пользователь выполняет команду, поле isActive = true, и поле isThereAUser = true соответственно
        //при выполнении команды /quit у пользователя поле isActive становится false
        //по исполнению метода runCommand для команды /quit бот понимает что активного пользователя нет и переходит к ожиданию
    }

    /**
     * метод для запуска работы бота
     */
    public void start() {
        startTimer();
        isRunning = true;
    }

    /**
     * метод для запуска таймера на обновление инфорфмации о тайтлах
     */
    private void startTimer() {
        Timer timer = new Timer(true);
        var timerTask = new CheckGamesUpdateTimer(titlesPath, titlesMapper, gamesMapper);
        timer.scheduleAtFixedRate(timerTask, timerDelay, timerPeriod);
    }

    /**
     * метод для остановки работы бота
     */
    public void stop() {
        writeUserData(usersMapper);
        isRunning = false;
    }

    /**
     * метод для записи данных о пользовательских подписках в жсон файл
     *
     * @param userMapping - словарь с пользователями
     */
    private void writeUserData(HashMap<String, User> userMapping) {
        try {
            Files.writeString(Path.of(usersPath), new Gson().toJson(userMapping));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод для логина, читает ввод имени пользователя и находит объект пользователя в словаре или заводит новую запись
     *
     * @return объект пользователя из словаря пользователей
     */
    public User getUser() {
        var sc = new Scanner(System.in);
        System.out.println("Введите имя пользователя или /stop чтобы остановить бота");
        var username = sc.nextLine();
        if ("/stop".equals(username)) {
            stop();
            return null;
        }
        if (!usersMapper.containsKey(username))
            usersMapper.put(username, new User(username, new HashMap<>()));
        hasUser = true;
        User user = usersMapper.get(username);
        user.setActive();
        runCommand("/help", user);
        return user;
    }
}
