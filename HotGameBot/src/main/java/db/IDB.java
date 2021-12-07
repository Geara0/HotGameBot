package db;

import entities.Title;

public interface IDB {
    //TODO: Получить подписки с бд
    String[] getSubscriptions(long userId);

    //TODO: Подписать пользователя
    ReportState subscribeUser(long userId, String title);

    ReportState unsubscribeAllUser(long userId);

    //TODO: Отписать пользователя
    ReportState unsubscribeUser(long userId, String title);
}
