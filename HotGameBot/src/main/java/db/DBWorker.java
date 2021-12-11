package db;

import entities.Levenshtein.LevenshteinCalculator;
import entities.Title;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static db.Converter.*;

/**
 * Класс, работающий с бд
 */
public class DBWorker implements IDB {
    private final String jdbcURL = "jdbc:postgresql://localhost:5432/postgres";
    private final String userName = "postgres";
    private final String password = System.getenv("PostgresPassword");
    /**
     * Соединение с бд
     */
    private Connection connection;

    /**
     * Подключаемся к бд при инициализации
     */
    public DBWorker() {
        try {
            connection = DriverManager.getConnection(jdbcURL, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавить пользователя в бд
     *
     * @param userId id пользователя
     */
    public void addUser(long userId) {
        executeSQL(connection, String.format(
                "INSERT INTO users(id) VALUES (%s)", userId));
    }

    /**
     * Добавить игру в бд
     *
     * @param title наименование игры
     */
    public void addTitle(Title title) {
        executeSQL(connection, String.format(
                "INSERT INTO games(title, link, buy_link, price, developer, publisher, genres, description, picture_jpeg, release_date, is_multiplayer) VALUES (%s)", title.toDB()));
    }

    /**
     * Получить игру из бд
     *
     * @param title наименование игры
     * @return игра
     */
    public Title getTitle(String title) {
        title = title.replaceAll("'", "");
        var result = executeSQL(connection, String.format(
                "SELECT * FROM games WHERE (title = '%s')", title));
        return convertGames(result).get(0);
    }

    @Override
    public String[] getSubscriptions(long userId) {
        String[] subscriptions;
        var result = executeSQL(connection, String.format(
                "SELECT subscriptions FROM users WHERE (id = %s)", userId));
        Set<Integer> subscriptionsIds = hstoreToSet(result, "subscriptions", Integer.class);
        if (subscriptionsIds == null || subscriptionsIds.size() < 1) return new String[0];
        result = executeSQL(connection, String.format(
                "SELECT title FROM games WHERE (id IN %s)", subscriptionsIds.toString()
                        .replace('[', '(').replace(']', ')')));
        subscriptions = convertStringRows(result, "title");
        return subscriptions;
    }

    @Override
    public String[] getClosest(String title) {
        var result = executeSQL(connection, "SELECT title FROM games");
        var allTitles = new HashSet<String>();
        Collections.addAll(allTitles, convertStringRows(result, "title"));
        var levenshtein = new LevenshteinCalculator();
        return levenshtein.getClosestStrings(allTitles, title, 4);
    }

    @Override
    public ReportState subscribeUser(long userId, String title) {
        title = title.replaceAll("'", "");

        var result = executeSQL(connection, String.format(
                "SELECT id FROM games WHERE (title = '%s')", title));
        var titles = convertLongRows(result, "id");

        if (titles.length == 0) return ReportState.BAD_NAME;
        var titleId = titles[0];
        executeSQL(connection, String.format(
                "UPDATE users SET subscriptions = subscriptions || ('%s=>null') WHERE (id = %s)",
                titleId, userId));
        return ReportState.OK;
    }

    @Override
    public ReportState unsubscribeUser(long userId, String title) {
        var result = executeSQL(connection, String.format(
                "SELECT id FROM games WHERE (title = '%s')", title));
        var titles = convertLongRows(result, "id");
        if (titles.length == 0) return ReportState.BAD_NAME;
        var titleId = titles[0];
        executeSQL(connection, String.format(
                "UPDATE users SET subscriptions = delete(subscriptions, '%s') WHERE (id = %s)",
                titleId, userId));
        return ReportState.OK;
    }

    @Override
    public ReportState unsubscribeAllUser(long userId) {
        executeSQL(connection, String.format(
                "UPDATE users SET subscriptions = hstore(array[]::character varying[]) WHERE (id = %s)", userId));
        return ReportState.OK;
    }
}
