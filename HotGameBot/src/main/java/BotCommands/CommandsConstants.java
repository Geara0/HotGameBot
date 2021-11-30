package BotCommands;

/**
 * Енам со строковыми константами, не совсем понимаю зачем он нужен, но все равно сделал, может когда-нибудь пойму
 */
public enum CommandsConstants {
    HELP_NAME("help"),
    HELP_DESCRIPTION("list of all known commands"),
    START_NAME("start"),
    START_DESCRIPTION("start using bot"),
    MY_GAMES_NAME("myGames"),
    MY_GAMES_DESCRIPTION("my subscriptions list"),
    NO_SUBS("У вас пока нет подписок"),
    QUIT("Вы вышли!"),
    STOPPING_LINE("specialStoppingLineThatMightBeAnEnumOrAnotherSpecialObjectButIDon'tWantToDoThisKittensAreCute"),
    IS_THE_RIGHT_TITLE_WITH_OPTIONS("Это тайтл, который вы искали?(yes/no/stop)"),
    INCORRECT_INDEX("Введнный индекс некорректен"),
    CANT_FIND_TITLE("Мы не можем подобрать тайтл за такую стоимость"),
    NOT_WHOLE_NUMBER("Ваш ввод не является целым числом"),
    AVAILABLE_RECOMMENDATIONS("Пока доступны только рекомендации по цене\r\nВведите жалемую стоимость в целых числах");


    /**
     * Конструкция для присвоения и получения текстового значения енаму
     */
    private final String value;

    CommandsConstants(String value) {
        this.value = value;
    }

    public String toStringValue() {
        return value;
    }
}
