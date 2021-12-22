package db;

public enum DBStringConstants {
    PRICE_UPDATED("Обновлена цена на"),
    PRICE_BEFORE("Цена до:"),
    PRICE_NOW("Цена теперь:"),
    ;

    private final String value;

    DBStringConstants(String value) {
        this.value = value;
    }

    public String toStringValue() {
        return value;
    }
}
