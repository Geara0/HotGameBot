package db;

import entities.Title;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static db.Converter.*;

public class DBWorker implements IDB {
    private final String jdbcURL = "jdbc:postgresql://localhost:5432/postgres";
    private final String userName = "postgres";
    private final String password = "ChPs2345";
    private Connection connection;

    public DBWorker() {
        try {
            connection = DriverManager.getConnection(jdbcURL, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getSubscriptions(long userId) {
        String[] subscriptions = null;
        var result = ExecuteSQL(connection, String.format(
                "SELECT subscriptions FROM users WHERE (id == %s)", userId));
        var subscriptionsIds = HstoreToSet(result, Integer.class);
        result = ExecuteSQL(connection, String.format(
                "SELECT title FROM games WHERE (id IN %s)", subscriptionsIds));
        try {
            subscriptions = ArrayToSet(result.getArray("title"), String.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }

    @Override
    public ReportState subscribeUser(long userId, String title) {
        return null;
    }

    @Override
    public ReportState unsubscribeAllUser(long userId) {
        return null;
    }

    @Override
    public ReportState unsubscribeUser(long userId, String title) {
        return null;
    }
}
