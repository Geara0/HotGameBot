import java.util.*;

public class Main {
    private HashMap<String, Title> Titles = new HashMap<>();
    private static String jsonPath = "";
    private static HashSet<Title> allTitles = new HashSet<>();

    /**
     * Функция, отправляющая уведомление
     *
     * @param name    тайтл
     * @param link    ссылка на HotGame
     * @param buyLink ссылка на предложение
     * @param price   цена
     * @return уведомление
     */
    public String sendNotification(String name, String link, String buyLink, int price) {
        if (!Titles.containsKey(link)) {
            Titles.put(link, new Title(name, link, buyLink, price));
            return null;
        }
        String notification = null;
        Title title = Titles.get(link);
        if (price < title.Price) {
            title.BuyLink = buyLink;
            notification = String.format("New best price for %s:\n Prev: %s,\nCurrent: %s\nHotGame link: %s,\nBuy link: %s",
                    title.getName(), title.Price, price, title.getLink(), title.BuyLink);
        }
        title.Price = price;
        return notification;
    }

    /**
     * Функция, парсящая данные пользователей
     *
     * @param users пользователи
     * @return множество игр
     */
    public static HashSet<Game> parseUsers(HashSet<User> users) {
        HashSet<Game> gameSet = new HashSet<>();
        HashMap<Title, HashSet<User>> gameList = new HashMap<>();
        for (var user : users) {
            for (var title : user.getTitles().values()) {
                if (!gameList.containsKey(title))
                    gameList.put(title, new HashSet<>());

                gameList.get(title).add(user);
            }
        }

        for (var game : gameList.entrySet())
            gameSet.add(new Game(game.getKey(), game.getValue()));
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
/* А надо ли это вообще?
    public static class ParseTimer extends TimerTask {
    public static void Main() {
        TimerTask checkTimer = new ParseTimer();
        // стартуем TimerTask в виде демона

        Timer timer = new Timer(true);
        // будем запускать каждых 10 секунд (10 * 1000 миллисекунд)
        timer.scheduleAtFixedRate(checkTimer, 0, 10 * 1000);
        System.out.println("TimerTask начал выполнение");
    }

        @Override
        public void run() {
            var userList = parseUserData(parseJson(jsonPath));
            for (var user:userList) {
               if (users.contains(user)){
                   for (var title :
                           user.getTitles()) {

                   }
               }
            }
        }
    }

 */

