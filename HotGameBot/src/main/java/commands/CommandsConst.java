package commands;

/**
 * Енам со строковыми константами, не совсем понимаю зачем он нужен, но все равно сделал, может когда-нибудь пойму
 */
public enum CommandsConst {
    HELP("""
            Доступны следующие команды:\r
            1. /wantToPlay переключит вас на рекомендации\r
            2. /sub позволит вам добавить тайтл в ваши подписки\r
            3. /quit выведет вас из меню\r
            4. /unsub позволит удалить тайтл из ваших подписок\r
            5. /mySubs выводит ваши подписки
            """),
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

    CommandsConst(String value) {
        this.value = value;
    }

    public String toStringValue() {
        return value;
    }
}
