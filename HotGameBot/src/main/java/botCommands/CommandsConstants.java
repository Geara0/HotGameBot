package botCommands;

/**
 * Енам со строковыми константами, не совсем понимаю зачем он нужен, но все равно сделал, может когда-нибудь пойму
 */
public enum CommandsConstants {
    // for /help
    AVAILABLE_COMMANDS("<b>Доступные команды:</b>"),
    HELP_NAME("help"),
    HELP_DESCRIPTION("Список всех доступных команд"),
    START_NAME("start"),
    START_DESCRIPTION("Начать использование бота"),
    MY_GAMES_NAME("myGames"),
    MY_GAMES_DESCRIPTION("Мои подписки"),
    SUBSCRIBE_NAME("subscribe"),
    SUBSCRIBE_DESCRIPTION("Подписаться на игру"),
    UNSUBSCRIBE_NAME("unsubscribe"),
    UNSUBSCRIBE_DESCRIPTION("Отписаться от игры"),
    UNSUBSCRIBE_ALL_NAME("unsubscribeAll"),
    UNSUBSCRIBE_ALL_DESCRIPTION("Отписаться от всех игр"),
    WANT_TO_PLAY_NAME("wantToPlay"),
    WANT_TO_PLAY_DESCRIPTION("Подборка игр по параметру"),
    NO_SUBS("У вас пока нет подписок"),
    QUIT("Вы вышли!"),
    // for commands themselves
    UR_SUBSCRIPTIONS("Вы подписаны на:"),
    HELLO("Привет, "),
    HELP_ON_START("С помощью этого бота ты сможешь быть в курсе нижайших цен на выбранные тобой игры.\n\nПример использования бота:"),
    U_FORGOT_NAME("Кажется, вы забыли ввести название\nИспользуйте /subscribe [title]"),
    NOT_IT("Это не то "),
    U_BEEN_SUBSCRIBED("Вы успешно подписались на "),
    U_BEEN_UNSUBSCRIBED("Вы успешно отписались от "),
    U_BEEN_UNSUBSCRIBED_ALL("Вы успешно отписались от всех игр"),
    WE_FOUND_MULTIPLE_VARIANTS("Мы нашли несколько вариантов по вашему запросу"),
    ERROR_BECAUSE("Произошла ошибка по причине "),
    WE_RECOMMEND("По заданным параметрам рекомендуем:"),
    WE_CANT_RECOMMEND("По заданным параметрам ничего не найдено")
    ;

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
