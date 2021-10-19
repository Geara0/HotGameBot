import java.util.HashSet;
import java.util.Objects;

/**
 * Класс игры
 * @author Geara0
 * @version 1.0
 */
public class Game {
    /**
     * тайтл
     */
    Title title;
    /**
     * множество пользователей, подписанных на тайтл
     */
    HashSet<User> users;

    /**
     * Конструктор класса
     * @param title {@link Game#title}
     * @param users {@link Game#users}
     */
    Game(Title title, HashSet<User> users) {
        this.title = title;
        this.users = users;
    }

    /**
     * Метод, удаляющий пользователя
     * @param username имя пользователя для удаления
     */
    void removeUser(String username) {
        users.remove(new User(username, null));
    }

    /**
     * Метод, добавляющий пользователя
     * @param user пользователь для добавления
     */
    void addUser(User user) {
        users.add(user);
    }

    /**
     * Overrides default equals method
     * @param o object to compare
     * @return comparison result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() == "".getClass() && o == title.getName()) return true;
        if (getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(title, game.title);
    }

    /**
     * Overrides default hashCode method
     * @return resulting hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
