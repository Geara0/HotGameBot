package bot;

public enum KeyboardMarkupTypes {
    DEFAULT(""),
    DB("$$"),
    NOT_IT("##"),
    PARSER("%%");

    private final String value;

    KeyboardMarkupTypes(String value) {
        this.value = value;
    }

    public String toStringValue() {
        return value;
    }
}
