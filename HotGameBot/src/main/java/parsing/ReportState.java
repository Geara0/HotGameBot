package parsing;

/**
 * Енам состояний парсера
 */
public enum ReportState {
    INITIAL("REPORTSTATE: INITIAL"),
    OK("REPORTSTATE: ALL GOOD"),
    BAD_URL("REPORTSTATE: BAD_URL"),
    BAD_NAME("REPORTSTATE: BAD_NAME"),
    BAD_PARAMETERS("REPORTSTATE: BAD_PARAMETERS");


    private final String value;

    ReportState(String value) {
        this.value = value;
    }

    public String toStringValue() {
        return value;
    }
}
