package parsing;

/**
 * Енам состояний парсера
 */
public enum ReportState {
    INITIAL("парсер в начальном положении"),
    OK("все хорошо"),
    BAD_URL("Вы ввели некорректную ссылку"),
    BAD_NAME("Вы ввели некорректное название");


    private final String value;

    ReportState(String value) {
        this.value = value;
    }

    public String toStringValue() {
        return value;
    }
}
