package API;

public class APITitle {
    Integer id;
    String title;
    String link;
    MinPrices min_prices;

    static class MinPrices {
        String price;
        Integer percent_discount;
    }
}
