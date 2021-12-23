package db;

import entities.Title;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Вспомогательный класс, тк в проекте нет Hibernate
 */
public class Converter {
    /**
     * Конвертировать ResultSet игр в список игр
     */
    public static ArrayList<Title> convertGames(ResultSet resultSet) {
        var titles = new ArrayList<Title>();
        try {
            while (resultSet.next()) {
                titles.add(new Title(
                        resultSet.getString("title"),
                        resultSet.getString("link"),
                        resultSet.getString("buy_link"),
                        Float.parseFloat(resultSet.getString("price")),
                        resultSet.getString("publisher"), resultSet.getString("developer"),
                        resultSet.getDate("release_date"),
                        arrayToJavaArray(resultSet.getArray("genres"), String.class),
                        resultSet.getBoolean("is_multiplayer"),
                        resultSet.getString("description"),
                        resultSet.getBlob("picture_jpeg")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles;
    }

    /**
     * Конвертировать ResultSet рядов таблицы в массив string рядов таблицы
     *
     * @param column колонка, из которой берутся данные
     * @return массив рядов таблицы
     */
    public static String[] convertStringRows(ResultSet resultSet, String column) {
        var titles = new ArrayList<String>();
        try {
            while (resultSet.next()) {
                titles.add(resultSet.getString(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles.toArray(new String[0]);
    }

    /**
     * Конвертировать ResultSet рядов таблицы в массив long рядов таблицы
     *
     * @param column колонка, из которой берутся данные
     * @return массив рядов таблицы
     */
    public static Long[] convertLongRows(ResultSet resultSet, String column) {
        var titles = new ArrayList<Long>();
        try {
            while (resultSet.next()) {
                titles.add(resultSet.getLong(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles.toArray(new Long[0]);
    }

    /**
     * Конвертировать SQL массив в Java массив
     *
     * @param array  SQL массив
     * @param tClass класс обозначающий тип данных массива
     * @param <T>    тип данных массива
     * @return массив
     */
    public static <T> T[] arrayToJavaArray(Array array, Class<T> tClass) {
        T[] javaArray = null;
        try {
            javaArray = (T[]) array.getArray();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return javaArray;
    }

    /**
     * Конвертировать hstore в Set
     *
     * @param hstore hstore
     * @param tClass класс обозначающий тип данных hstore
     * @param <T>    тип данных hstore
     * @return set
     */
    public static <T> Set<T> hstoreToSet(ResultSet hstore, String column, Class<T> tClass) {
        Set<T> set = null;
        Map<T, String> map = null;
        try {
            if (hstore.next()) {
                map = (Map<T, String>) hstore.getObject(column);
                set = map.keySet();
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return set;
    }

    /**
     * Выполнить SQL запрос
     *
     * @param connection соединение
     * @param sql        запрос
     * @return ResultSet - результат
     */
    public static ResultSet executeSQL(Connection connection, String sql) {
        ResultSet result = null;
        try {
            var statement = connection.createStatement();
            statement.execute(sql);
            result = statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
