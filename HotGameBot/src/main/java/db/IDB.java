package db;

import entities.Title;

public interface IDB {
    /**
     * Получить подписки пользователя
     *
     * @param userId id пользователя
     * @return наименования подписок пользователя
     */
    String[] getSubscriptions(long userId);

    /**
     * Подписать пользователя на игру
     *
     * @param userId id пользователя
     * @param title  наименование игры
     * @return ReportState с кодом ошибки
     */
    ReportState subscribeUser(long userId, String title);

    /**
     * Отписать пользователя от всех игр
     *
     * @param userId id пользователя
     * @return ReportState с кодом ошибки
     */
    ReportState unsubscribeAllUser(long userId);

    /**
     * Отписать пользователя от игры
     *
     * @param userId id пользователя
     * @param title  наименование игры
     * @return ReportState с кодом ошибки
     */
    ReportState unsubscribeUser(long userId, String title);

    /**
     * Получить ближайшие по Левенштейну игры
     *
     * @param title наименование игры
     * @return ближайшие по Левенштейну игры
     */
    String[] getClosestOverall(String title);

    /**
     * Получить игру из бд
     *
     * @param title наименование игры
     * @return игра
     */
    Title getTitle(String title);

    /**
     * Добавить игру в бд
     *
     * @param title наименование игры
     */
    void addTitle(Title title);

    /**
     * Обновить игру в бд
     *
     * @param title наименование игры
     */
    void updateTitle(Title title);
}
