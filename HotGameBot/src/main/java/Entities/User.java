package Entities;

import java.util.HashMap;

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

    public void setActive() {
        isActive = true;
    }

    public void setInactive() {
        isActive = false;
    }

    /**
     * Словарь <название, тайтл>
     */
    private HashMap<String, Title> titles;
    /**
     * имя пользователя
     */
    private final String username;

    /**
     * Конструктор класса
     *
     * @param titles   {@link User#titles}
     * @param username {@link User#username}
     */
    public User(String username, HashMap<String, Title> titles) {
        this.titles = titles;
        this.username = username;
    }

    /**
     * Метод, прекращающий отслеживание тайтла
     *
     * @param title - экземпляр более не интересующего тайтла
     */
    public void Unwatch(Title title) {
        if (titles.containsKey(title.getName())) {
            titles.remove(title.getName());
            System.out.println("Вы успешно отписались от игры!");

        } else
            System.out.println("Вы не подписаны на эту игру!");
    }

    /**
     * Метод, начинающий отслеживание тайтла
     *
     * @param title ссылка на интересующий тайтл
     */
    public void Watch(Title title) {
        if (!titles.containsKey(title.getName())) {
            titles.put(title.getName(), title);
            System.out.println("Вы успешно подписались на игру!");
        } else
            System.out.println("Вы уже подписаны на эту игру!");
    }

    /**
     * Метод, возвращающий тайтлы, отслеживаемые пользователем
     *
     * @return {@link User#titles}
     */
    public HashMap<String, Title> getTitles() {
        return titles;
    }

    public String getUsername() {
        return username;
    }
}
