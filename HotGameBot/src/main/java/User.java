import java.util.HashMap;
import java.util.Objects;

/**
 * Класс пользователя.
 * Используется для хранения тайтлов пользователя.
 *
 * @author Geara0
 * @version 1.0
 */
public class User {
    /**
     * Словарь <ссылка, тайтл>
     */
    private HashMap<String, Title> titles;
    /**
     * имя пользователя
     */
    private String username;

    /**
     * Конструктор класса
     * @param titles {@link User#titles}
     * @param username {@link User#username}
     */
    public User(String username, HashMap<String, Title> titles) {
        this.titles = titles;
        this.username = username;
    }

    /**
     * Метод, прекращающий отслеживание тайтла
     * @param link ссылка на более не интересующий тайтл
     */
    public void Unwatch(String link) {
        titles.remove(link);
    }

    /**
     * Метод, начинающий отслеживание тайтла
     * @param title ссылка на интересующий тайтл
     */
    public void Watch(Title title) {
        titles.put(title.getLink(), title);
    }

    /**
     * Метод, возвращающий тайтлы, отслеживаемые пользователем
     * @return {@link User#titles}
     */
    public HashMap<String, Title> getTitles() {
        return titles;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Overrides default equals method
     * @param o объект для сравнения
     * @return результат сравнения
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() == "".getClass() && o == username) return true;
        if (getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    /**
     * Overrides default hashCode method
     * @return resulting hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
