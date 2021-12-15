package bot;

public enum KeyboardMarkupTypes {
    DEFAULT(""),
    DB("$$"),
    NOT_IT("##"),
    CONFIRM_UNSUB("@@"),
    PARSER("%%");

    private final String value;

    KeyboardMarkupTypes(String value) {
        this.value = value;
    }

    public String toStringValue() {
        return value;
    }
}
