package db;

import entities.Levenshtein.LevenshteinCalculator;
import entities.Title;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
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

    public void addUser(long userId) {
        ExecuteSQL(connection, String.format(
                "INSERT INTO users(id) VALUES (%s)", userId));
    }

    public Title getTitle(String title) {
        title = title.replaceAll("'", "");
        var result = ExecuteSQL(connection, String.format(
                "SELECT * FROM games WHERE (title = '%s')", title));
        return ConvertGames(result).get(0);
    }

    @Override
    public String[] getSubscriptions(long userId) {
        String[] subscriptions;
        var result = ExecuteSQL(connection, String.format(
                "SELECT subscriptions FROM users WHERE (id = %s)", userId));
        var subscriptionsIds = HstoreToSet(result, "subscriptions", Integer.class);
        if (subscriptionsIds == null || subscriptionsIds.size() < 1) return new String[0];
        result = ExecuteSQL(connection, String.format(
                "SELECT title FROM games WHERE (id IN %s)", subscriptionsIds.toString()
                        .replace('[', '(').replace(']', ')')));
        subscriptions = ConvertStringRows(result, "title");
        return subscriptions;
    }

    public String[] getClosest(String title) {
        var result = ExecuteSQL(connection, "SELECT title FROM games");
        var allTitles = new HashSet<String>();
        Collections.addAll(allTitles, ConvertStringRows(result, "title"));
        var levenshtein = new LevenshteinCalculator();
        return levenshtein.getClosestStrings(allTitles, title, 5);
    }

    @Override
    public ReportState subscribeUser(long userId, String title) {
        title = title.replaceAll("'", "");

        var result = ExecuteSQL(connection, "SELECT title FROM games");
        var allTitles = new HashSet<String>();
        Collections.addAll(allTitles, ConvertStringRows(result, "title"));
        var levenshtein = new LevenshteinCalculator();
        title = levenshtein.getClosestString(allTitles, title);

        result = ExecuteSQL(connection, String.format(
                "SELECT id FROM games WHERE (title = '%s')", title));
        var titles = ConvertLongRows(result, "id");

        if (titles.length == 0) return ReportState.BAD_NAME;
        var titleId = titles[0];
        ExecuteSQL(connection, String.format(
                "UPDATE users SET subscriptions = subscriptions || ('%s=>null') WHERE (id = %s)",
                titleId, userId));
        return ReportState.OK;
    }

    @Override
    public ReportState unsubscribeUser(long userId, String title) {
        var result = ExecuteSQL(connection, String.format(
                "SELECT id FROM games WHERE (title = '%s')", title));
        var titles = ConvertLongRows(result, "id");
        if (titles.length == 0) return ReportState.BAD_NAME;
        var titleId = titles[0];
        ExecuteSQL(connection, String.format(
                "UPDATE users SET subscriptions = delete(subscriptions, '%s') WHERE (id = %s)",
                titleId, userId));
        return ReportState.OK;
    }

    @Override
    public ReportState unsubscribeAllUser(long userId) {
        ExecuteSQL(connection, String.format(
                "UPDATE users SET subscriptions = hstore(array[]::character varying[]) WHERE (id = %s)", userId));
        return ReportState.OK;
    }
}
