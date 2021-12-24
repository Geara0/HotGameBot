package entities;

import java.sql.Blob;
import java.util.Date;

/**
 * Класс для хранения тайтла
 *
 * @author Geara0
 * @version 1.0
 */
public class Title {

    private String description;
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
    private final float price;
    private String publisher;
    private String developer;
    private Date releaseDate;
    private String[] genres;
    private Blob pictureJpeg;

    /**
     * синглплеер или мультиплеер
     */
    private boolean isMultiplayer;

    /**
     * Конструктор класса
     *
     * @param name    {@link Title#name}
     * @param link    {@link Title#link}
     * @param buyLink {@link Title#buyLink}
     * @param price   {@link Title#price}
     */
    public Title(String name, String link, String buyLink, float price) {
        this.name = name;
        this.link = link;
        this.buyLink = buyLink;
        this.price = price;
    }

    /**
     * Расширенный рабочий конструктор класся
     *
     * @param name        название тайтла
     * @param link        ссылка на тайтл
     * @param buyLink     ссылка на покупку с минимальной ценой
     * @param price       минимальная цена
     * @param publisher   издатель
     * @param developer   разбраточик
     * @param releaseDate дата релиза
     * @param genres      список жанров
     * @param multiplayer является ли мултиплеерной
     */
    public Title(String name, String link, String buyLink, float price, String publisher, String developer,
                 Date releaseDate, String[] genres, boolean multiplayer, String description, Blob pictureJpeg) {
        this.name = name;
        this.link = link;
        this.buyLink = buyLink;
        this.price = price;
        this.publisher = publisher;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.isMultiplayer = multiplayer;
        this.description = description;
        this.pictureJpeg = pictureJpeg;
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
     *
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
    public float getPrice() {
        return price;
    }

    public String getStringForm() {
        return this.toString();
    }

    @Override
    public String toString() {
        return String.format("<b>%s</b>\nЦена - %s\n\nКупить:\n%s\n\nHotGame:\n%s\n", getName(), getPrice(), getBuyLink(), getLink());
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String[] getGenres() {
        return genres;
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public String getDescription() {
        return description;
    }

    public String toDB() {
        var genres = new StringBuilder("{");
        for (var genre : getGenres()) genres.append(genre).append(", ");
        if (genres.length() > 1)
            genres.delete(genres.length() - 2, genres.length() - 1);
        genres.append("}");
        var params = new String[]{getName(),
                getLink(),
                getBuyLink(),
                String.valueOf(getPrice()),
                getDeveloper(),
                getPublisher(),
                genres.toString(),
                getDescription(),
                null,
                String.valueOf(getReleaseDate()),
                isMultiplayer() ? "true" : "false"};
        var result = new StringBuilder();
        for (var param : params) {
            if (param == null) result.append("null, ");
            else result.append("'").append(param.replaceAll("'","")).append("', ");
        }
        result.delete(result.length() - 2, result.length() - 1);
        return result.toString();
    }
}
