package db;

import entities.Title;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Converter {
    public static ArrayList<Title> ConvertGames(ResultSet resultSet) {
        var titles = new ArrayList<Title>();
        try {
            while (resultSet.next()) {
                titles.add(new Title(resultSet.getString("title"), resultSet.getString("link"), resultSet.getString("buy_link"), Integer.parseInt(resultSet.getString("price")), resultSet.getString("publisher"), resultSet.getString("developer"), resultSet.getDate("release_date"), NormalizeArray(resultSet.getArray("genres")), resultSet.getBoolean("is_multiplayer"), resultSet.getString("description"), resultSet.getBlob("picture_jpeg")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles;
    }

    public static String[] ConvertStringRows(ResultSet resultSet, String column) {
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

    public static Long[] ConvertLongRows(ResultSet resultSet, String column) {
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

    public static <T> T[] ArrayToSet(Array array, Class<T> tClass) {
        T[] set = null;
        try {
            set = (T[]) array.getArray();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return set;
    }

    public static <T> Set<T> HstoreToSet(ResultSet resultSet, String column, Class<T> tClass) {
        Set<T> set = null;
        Map<T, String> map = null;
        try {
            if (resultSet.next()) {
                map = (Map<T, String>) resultSet.getObject(column);
                set = map.keySet();
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return set;
    }

    public static ResultSet ExecuteSQL(Connection connection, String sql) {
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

    public static String[] NormalizeArray(Array array) {
        String[] result = null;
        try {
            result = (String[]) array.getArray();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
