package db;

import bot.HotGameBot;
import entities.Levenshtein.LevenshteinCalculator;
import entities.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static db.Converter.*;
import static db.DBStringConstants.*;

/**
 * Класс, работающий с бд
 */
public class DBWorker implements IDB  {
    private final String jdbcURL = "jdbc:postgresql://localhost:5432/postgres";
    private final String userName = "postgres";
    private final String password = System.getenv("PostgresPassword");
    private static final Logger logger = LogManager.getLogger("db.DBWorker");
    /**
     * Соединение с бд
     */
    private Connection connection;

    /**
     * Подключаемся к бд при инициализации
     */
    public DBWorker() {
        logger.debug("db worker initialized");
        try {
            connection = DriverManager.getConnection(jdbcURL, userName, password);
        } catch (SQLException e) {
            logger.error("connection to db failed: {}", Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Добавить пользователя в бд
     *
     * @param userId id пользователя
     */
    public void addUser(long userId) {
        logger.debug("adding user: {}", userId);
        executeSQL(connection, String.format(
                "INSERT INTO users(id) VALUES (%s)", userId));
    }

    /**
     * Добавить игру в бд
     *
     * @param title наименование игры
     */
    @Override
    public void addTitle(Title title) {
        logger.debug("adding title: {}", title.toString());
        executeSQL(connection, String.format(
                "INSERT INTO games(id, title, link, buy_link, price, developer, publisher, genres, description, picture_jpeg, release_date, is_multiplayer) VALUES (%s)", title.toDB()));
    }

    @Override
    public void updateTitle(Title title) {
        var result = executeSQL(connection, String.format("SELECT * FROM games WHERE id = %s", title.getId()));
        var resultTitle = convertGames(result);
        if (resultTitle.size() == 0)
            addTitle(title);
        else {
            var resTitle = resultTitle.get(0);
            executeSQL(connection, String.format(
                    "UPDATE games SET buy_link = %s, price = %s WHERE id = %s",
                    resTitle.getBuyLink(), resTitle.getPrice(), resTitle.getId()));
            if (title.getPrice() < resTitle.getPrice()) {
                var users = hstoreToSet(result, "subscribers", String.class);
                var message = String.format("%s %s\n%s %s\n%s %s\n\n",
                        PRICE_UPDATED, title.getName(),
                        PRICE_BEFORE, resTitle.getPrice(),
                        PRICE_NOW, title.getPrice());
                notifyUsers(users, title, message);
            }
        }
    }

    @Override
    public Title getTitle(String title) {
        title = title.replaceAll("'", "");
        var result = executeSQL(connection, String.format(
                "SELECT * FROM games WHERE (title = '%s')", title));
        logger.debug("getting title: {}", result.toString());
        return convertGames(result).get(0);
    }

    @Override
    public String[] getSubscriptions(long userId) {
        String[] subscriptions;
        var result = executeSQL(connection, String.format(
                "SELECT subscriptions FROM users WHERE (id = %s)", userId));
        Set<Long> subscriptionsIds = hstoreToSet(result, "subscriptions", Long.class);
        if (subscriptionsIds == null || subscriptionsIds.size() < 1) return new String[0];
        result = executeSQL(connection, String.format(
                "SELECT title FROM games WHERE (id IN %s)",
                subscriptionsIds.toString().replace('[', '(').replace(']', ')')));
        subscriptions = convertStringRows(result, "title");
        logger.debug("getting subscriptions: user:{}, sql:{}", userId, result.toString());
        return subscriptions;
    }

    @Override
    public String[] getClosestOverall(String title) {
        var result = executeSQL(connection, "SELECT title FROM games");
        var allTitles = new HashSet<String>();
        Collections.addAll(allTitles, convertStringRows(result, "title"));
        var levenshtein = new LevenshteinCalculator();
        return levenshtein.getClosestStrings(allTitles, title, 4);
    }

    @Override
    public Long getId(String titleName) {
        logger.debug("trying to get id of title: {}", titleName);
        var result = executeSQL(connection, String.format(
                "SELECT id FROM games WHERE title = '%s'", titleName));
        var titleIds = convertLongRows(result, "id");
        if (titleIds.length == 0) {
            logger.warn("no title id which matches title name: {}", titleName);
            return null;
        }
        return titleIds[0];
    }

    @Override
    public String getName(long titleId) {
        logger.debug("trying to get title name with id {}", titleId);
        var result = executeSQL(connection, String.format(
                "SELECT title FROM games WHERE id = %s", titleId));
        var titleNames = convertStringRows(result, "title");
        if (titleNames.length == 0) {
            logger.warn("no such title with id {} in database", titleId);
            return "DATABASE ERROR";
        }
        return titleNames[0];
    }

    @Override
    public ReportState subscribeUser(long userId, Long titleId) {
        logger.debug("trying to subscribe user {} to title {}", userId, titleId);
        var result = executeSQL(connection, String.format(
                "SELECT subscriptions FROM users WHERE id = %s", userId));

        var subscriptions = hstoreToSet(result, "subscriptions", Long.class);
        if (subscriptions.contains(String.valueOf(titleId))) return ReportState.ALREADY;

        executeSQL(connection, String.format(
                "UPDATE games SET subscribers = subscribers || ('%s=>null') WHERE (id = %s)",
                userId, titleId));
        executeSQL(connection, String.format(
                "UPDATE users SET subscriptions = subscriptions || ('%s=>null') WHERE (id = %s)",
                titleId, userId));
        logger.debug("user successfully subscribed: user:{}, title:{}", userId, titleId);
        return ReportState.OK;
    }

    @Override
    public ReportState subscribeUser(long userId, String title) {
        logger.debug("trying subscribe user: user:{}, title:{}", userId, title);
        var titleId = getId(title);
        return subscribeUser(userId, titleId);
    }

    @Override
    public ReportState unsubscribeUser(long userId, String title) {
        var result = executeSQL(connection, String.format(
                "SELECT id FROM games WHERE (title = '%s')", title));
        var titles = convertLongRows(result, "id");
        logger.debug("trying unsubscribe user: user:{}, title:{}", userId, title);
        if (titles.length == 0) {
            logger.debug("cannot unsubscribe user (no result in subs request): user:{}, title:{}", userId, title);
            return ReportState.BAD_NAME;
        }
        var titleId = titles[0];
        executeSQL(connection, String.format(
                "UPDATE games SET subscribers = delete(subscribers, '%s') WHERE (id = %s)",
                userId, titleId));
        executeSQL(connection, String.format(
                "UPDATE users SET subscriptions = delete(subscriptions, '%s') WHERE (id = %s)",
                titleId, userId));
        logger.debug("user successfully unsubscribed: user:{}, title:{}", userId, titleId);
        return ReportState.OK;
    }

    @Override
    public ReportState unsubscribeAllUser(long userId) {
        logger.debug("UNSUBSCRIBING user:{} FROM ALL TITLES", userId);
        executeSQL(connection, String.format(
                "UPDATE users SET subscriptions = hstore(array[]::character varying[]) WHERE (id = %s)", userId));
        return ReportState.OK;
    }

    private void notifyUsers(Set<String> userIds, Title title, String message) {
//        for (var stringId : userIds) {
//            var reply = new SendMessage();
//            reply.setChatId(stringId);
//            reply.setText(message + title.toString());
//            try {
//                AbsSender.execute(reply);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (var stringId : userIds) {
//            HotGameBot.notifyUser(stringId, title, message);
//        }
    }
}
