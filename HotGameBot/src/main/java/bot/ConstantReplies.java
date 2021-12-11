package bot;

public enum ConstantReplies {
    U_R_SUBSCRIBED_ON("Вы подписаны на %s"),
    OTHER_SUGGESTIONS("Тогда вот другие предложения:"),
    NOTHING_FOUND("Вы ввели некорректную ссылку"),
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
