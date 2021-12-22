package db;

public enum ReportState {
    INITIAL("БД в начальном положении"),
    OK("Все хорошо"),
    ALREADY("Это значение уже содержится в базе"),
    BAD_URL("Вы ввели некорректную ссылку"),
    BAD_NAME("Вы ввели некорректное название"),
    FAILURE("Проблема на нашей стороне");

    private final String value;

    ReportState(String value) {
        this.value = value;
    }

    public String toStringValue() {
        return value;
    }
}
