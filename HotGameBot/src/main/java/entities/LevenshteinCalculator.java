package entities;

import java.util.*;

public class LevenshteinCalculator {
    /**
     * Метод принимает для определения строки из коллекции с минимальным расстоянием до данной
     * @param stringSet - коллекция строк, среди которых надо сравнивать
     * @param original - строка, с которой мы сравниваем коллекцию
     * @return - ближайшую к данной строку из коллекции
     */
    public String getClosestString(Set<String> stringSet, String original){
        String closestString = "";
        int minDistance = Integer.MAX_VALUE;
        for (var str : stringSet){
            var distance = calculateDistance(str.toLowerCase(), original.toLowerCase());
            if (distance==0)
                return str;
            if(distance<minDistance){
                minDistance = distance;
                closestString = str;
            }
        }
        return closestString;
    }

    /**
     * Calculate the Entities.Levenshtein distance between two strings. Basically, the number of
     * changes that need to be made to convert one string into another. Very useful when
     * determining string similarties.
     *
     * @param first - первая строка для сравнения
     * @param second - вторая строка для сравнения
     * @return The Entities.Levenshtein distance
     */
    private static int calculateDistance(String first, String second) {
        // if we want to ignore case sensitivity, lower case the strings
        first = first.toLowerCase();
        second = second.toLowerCase();

        // store length
        int m = first.length();
        int n = second.length();

        // matrix to store differences
        int[][] deltaM = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            deltaM[i][0] = i;
        }

        for (int j = 1; j <= n; j++) {
            deltaM[0][j] = j;
        }

        for (int j = 1; j <= n; j++) {
            for (int i = 1; i <= m; i++) {
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    deltaM[i][j] = deltaM[i - 1][j - 1];
                } else {
                    deltaM[i][j] = Math.min(
                            deltaM[i - 1][j] + 1,
                            Math.min(
                                    deltaM[i][j - 1] + 1,
                                    deltaM[i - 1][j - 1] + 1
                            )
                    );
                }
            }
        }
        return deltaM[m][n];
    }
}