package Entities;

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

    private boolean isActive = false;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    /**
     * Словарь <название, тайтл>
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
     * @param title - экземпляр более не интересующего тайтла
     */
    public void Unwatch(Title title) {
        if(titles.containsKey(title.getName())) {
            titles.remove(title.getName());
            System.out.println("Вы успешно отписались от игры!");

        }
        else
            System.out.println("Вы не подписаны на эту игру!");
    }

    /**
     * Метод, начинающий отслеживание тайтла
     * @param title ссылка на интересующий тайтл
     */
    public void Watch(Title title) {
        if(!titles.containsKey(title.getName())){
            titles.put(title.getName(),title);
            System.out.println("Вы успешно подписались на игру!");
        }
        else
            System.out.println("Вы уже подписаны на эту игру!");
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
