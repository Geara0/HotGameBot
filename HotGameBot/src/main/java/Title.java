import java.util.Objects;

/**
 * Класс для хранения тайтла
 *
 * @version 1.0
 * @author Geara0
 */
public class Title {
    /**
     *  Поле названия
     */
    private final String name;
    /**
     * Поле ссылки на HotGame
     */
    private final String link;
    /**
     * Поле ссылки на выгодную покупку
     */
    String BuyLink;
    /**
     * Поле цены
     */
    int Price;

    /**
     * Конструктор класса
     * @param name {@link Title#name}
     * @param link {@link Title#link}
     * @param buyLink {@link Title#BuyLink}
     * @param price {@link Title#Price}
     */
    public Title(String name, String link, String buyLink, int price) {
        this.name = name;
        this.link = link;
        BuyLink = buyLink;
        Price = price;
    }

    /**
     * Пустой конструктор, полезная вещь
     */
    public Title(){
        this.name = null;
        this.link = null;
        BuyLink = null;
        Price = 0;
    }

    /**
     * Геттер названия
     * @return {@link Title#name}
     */
    public String getName() {
        return name;
    }

    /**
     * Геттер ссылки на HotGame
     * @return {@link Title#link}
     */
    public String getLink() {
        return link;
    }

    /**
     * Overrides default equals method
     * @param obj объект для сравнения
     */
    @Override
    public boolean equals(Object obj){
        if(obj.getClass() != this.getClass())
            return false;
        Title another = (Title) obj;
        return (this.name != null && this.name.equals(another.name)) &&
                (this.link != null && this.link.equals(another.link)) &&
                (BuyLink != null && BuyLink.equals(another.BuyLink)) &&
                Price == another.Price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }
}
