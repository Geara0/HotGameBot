package Entities;

import java.util.HashSet;

/**
 * Класс игры
 * @author Geara0
 * @version 1.0
 */
public class Game {
    /**
     * тайтл
     */
    private Title title;
    /**
     * множество пользователей, подписанных на тайтл
     */
    private final HashSet<User> users;

    /**
     * Конструктор класса
     * @param title {@link Game#title}
     * @param users {@link Game#users}
     */
    public Game(Title title, HashSet<User> users) {
        this.title = title;
        this.users = users;
    }

    /**
     * Метод, изменяющий данные об игре
     * @param title тайтл на замену старому
     */
    public void setTitle(Title title){
        this.title = title;
    }
    public Title getTitle(){
        return title;
    }
}
