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
     * Сеттер для поля title
     * @param title тайтл для присвоения полю
     */
    public void setTitle(Title title){
        this.title = title;
    }

    /**
     * Геттер для поля title
     * @return тайтл из поля title
     */
    public Title getTitle(){
        return title;
    }
}
