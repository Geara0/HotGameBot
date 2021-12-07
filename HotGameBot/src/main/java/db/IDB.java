package db;

import entities.Title;

public interface IDB {
    String[] getSubscriptions(long userId);

    ReportState subscribeUser(long userId, String title);

    ReportState unsubscribeAllUser(long userId);

    ReportState unsubscribeUser(long userId, String title);

    String[] getClosest(String title);
}
