package Entities;

/**
 * Класс для хранения тайтла
 *
 * @author Geara0
 * @version 1.0
 */
public class Title {
    /**
     * Поле названия
     */
    private String name;
    /**
     * Поле ссылки на HotGame
     */
    private String link;
    /**
     * Поле ссылки на выгодную покупку
     */
    private String buyLink;
    /**
     * Поле цены
     */
    private final int price;

    /**
     * Конструктор класса
     *
     * @param name    {@link Title#name}
     * @param link    {@link Title#link}
     * @param buyLink {@link Title#buyLink}
     * @param price   {@link Title#price}
     */
    public Title(String name, String link, String buyLink, int price) {
        this.name = name;
        this.link = link;
        this.buyLink = buyLink;
        this.price = price;
    }

    /**
     * Пустой конструктор, полезная вещь
     */
    public Title() {
        price = 0;
    }

    /**
     * Геттер названия
     *
     * @return {@link Title#name}
     */
    public String getName() {
        return name;
    }

    /**
     * Геттер ссылки на HotGame
     *
     * @return {@link Title#link}
     */
    public String getLink() {
        return link;
    }

    /**
     * Геттер ссылки на покупку
     * @return {@link Title#buyLink}
     */
    public String getBuyLink() {
        return buyLink;
    }

    /**
     * Геттер цены
     *
     * @return {@link Title#price}
     */
    public int getPrice() {
        return price;
    }

    public String getStringForm(){
        return this.toString();
    }

    @Override
    public String toString() {
        return getName() + "  Цена - " + getPrice() + "\r\n" + getBuyLink() + "\r\n";
    }
}
