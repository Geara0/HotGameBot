package bot;

public enum ConstantReplies {
    OTHER_SUGGESTIONS("Тогда вот другие предложения:"),
    BAD_URL("Вы ввели некорректную ссылку"),
    //No usage
    NOTHING_FOUND("По этому запросу ничего не найдено"),
    BAD_NAME("Вы ввели некорректное название"),
    FAILURE("Проблема на нашей стороне");

    private final String value;

    ConstantReplies(String value) {
        this.value = value;
    }

    public String toStringValue() {
        return value;
    }
}
