package db;

public enum DBIntegerConstants {
    NAME_MAX(100),
    LINK_MAX(150),
    BUY_LINK_MAX(400),
    PRICE_MAX(10),
    DEVELOPER_MAX(200),
    PUBLISHER_MAX(200),
    GENRE_MAX(20),
    DESCRIPTION_MAX(3000);

    private final int value;

    DBIntegerConstants(int value) {
        this.value = value;
    }

    public int toIntValue() {
        return value;
    }
}
