package entities.Levenshtein;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.event.KeyValuePair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LevenshteinCalculator {
    /**
     * Пары символ : символы, лежащие рядом на клавиатуре
     */
    private HashMap<Integer, ArrayList<Integer>> codeFamilies;
    /**
     * Транслитерация русских символов в английские с сохранением звучания(английские символы из ASCII)
     */
    private HashMap<String, String> translit;

    public LevenshteinCalculator() {
        Gson gson = new Gson();
        try {
            var a = Files.readString(Path.of(".\\JSONs\\DistanceCodeKey.json"));
            codeFamilies = gson.fromJson(Files.readString(Path.of(".\\JSONs\\DistanceCodeKey.json")), new TypeToken<HashMap<Integer, ArrayList<Integer>>>() {
            }.getType());
            translit = gson.fromJson(Files.readString(Path.of(".\\JSONs\\Translit.json")), new TypeToken<HashMap<String, String>>() {
            }.getType());

        } catch (IOException e) {
            codeFamilies = new HashMap<>();
            translit = new HashMap<>();
        }
    }

    public String getClosestString(Set<String> stringSet, String original) {
        return getClosestStrings(stringSet, original, 1)[0];
    }

    public String[] getClosestStrings(Set<String> stringSet, String original, int count) {
        var strings = new String[count];
        var pairs = search(stringSet, original);
        for (int i = 0; i < count; i++) {
            strings[i] = pairs.get(i).text;
        }
        return strings;
    }

    /**
     * Алгоритм левенштайна для поиска расстояния между словами
     *
     * @param source слово пользователя
     * @param target слово для сравнения
     * @return расстояние между словами целым числом
     */
    private int calculate(Word source, Word target, boolean fullWord) {
        if (source.text == null) {
            if (target.text == null)
                return 0;
            return target.text.length() * 2;
        }
        if (target.text == null)
            return source.text.length() * 2;
        int len0 = source.text.length();
        int len1 = target.text.length();

        int[][] distance = new int[3][len1 + 1];

        for (int j = 1; j < len1; ++j)
            distance[0][j] = j * 2;

        int currentRow = 0;
        for (int i = 1; i <= len0; ++i) {
            currentRow = i % 3;
            int previousRow = (i - 1) % 3;
            distance[currentRow][0] = i * 2;
            for (int j = 1; j <= len1; j++) {
                int cost_insert = distance[previousRow][j] + getAddition(fullWord, i, len0);
                int cost_delete = distance[currentRow][j - 1] + getAddition(fullWord, i, len0);
                int cost_replace = distance[previousRow][j - 1] + costDistanceSymbol(source, i - 1, target, j - 1);
                distance[currentRow][j] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
                if (i > 1 && j > 1 && source.text.charAt(i - 1) == target.text.charAt(j - 2)
                        && source.text.charAt(i - 2) == target.text.charAt(j - 1))
                    distance[currentRow][j] = Math.min(distance[currentRow][j], distance[(i - 2) % 3][j - 2] + 2);
            }
        }
        return distance[currentRow][len1];
    }

    private int getAddition(boolean fullWord, int i, int len0) {
        return !fullWord && i == len0 ? 1 : 2;
    }

    public List<Pair> search(Set<String> stringSet, String userText) {
        ArrayList<Pair> result = new ArrayList<>();
        AnalizeObject searchObj;
        if (userText.length() > 0)
            searchObj = new AnalizeObject(userText.toLowerCase());
        else
            searchObj = new AnalizeObject();
        for (String toCompare : stringSet) {
            AnalizeObject objToCompare = new AnalizeObject(toCompare);
            double cost = getRangePhrase(objToCompare, searchObj);
            result.add(new Pair(toCompare, cost));
        }

        result.sort(Pair::compareTo);
        return result;
    }

    private double getRangePhrase(AnalizeObject source, AnalizeObject target) {
        if (source.words.isEmpty()) {
            if (target.words.isEmpty())
                return 0;
            return getLengthSum(target.words) * 2;
        }
        if (target.words.isEmpty())
            return getLengthSum(source.words) * 2;
        double result = 0;
        for (int i = 0; i < target.words.size(); i++) {
            double minRangeWord = Double.MAX_VALUE;
            int minIndex = 0;
            for (int j = 0; j < source.words.size(); j++) {
                double currentRangedWord = getRangeWord(source.words.get(j), target.words.get(i));
                if (currentRangedWord < minRangeWord) {
                    minRangeWord = currentRangedWord;
                    minIndex = j;
                }
            }
            result += minRangeWord * 10 + (Math.abs(i - minIndex) / 10.0);
        }
        return result;
    }

    private int getLengthSum(List<Word> words) {
        int sum = 0;
        for (Word word : words)
            sum += word.text.length();
        return sum;
    }

    private double getRangeWord(Word source, Word target) {
        double minDistance = Double.MAX_VALUE;
        Word croppedSource = new Word();
        int length = Math.min(source.text.length(), target.text.length() + 1);
        for (int i = 0; i < source.text.length() - length; i++) {
            croppedSource.text = source.text.substring(i, length);
            croppedSource.codes = source.codes.subList(i, length + i);
            minDistance = Math.min(minDistance, calculate(croppedSource, target, croppedSource.text.length() == source.text.length()));
        }
        return minDistance;
    }

    private int costDistanceSymbol(Word source, int sourcePos, Word target, int targetPos) {
        if (source.text.charAt(sourcePos) == target.text.charAt(targetPos)) return 0;
        if (Objects.equals(source.codes.get(sourcePos), target.codes.get(targetPos))) return 0;
        int resultWeight;
        ArrayList<Integer> nearKeys = codeFamilies.getOrDefault(source.codes.get(sourcePos), new ArrayList<>());
        if (nearKeys.isEmpty())
            resultWeight = 2;
        else
            resultWeight = nearKeys.contains(target.codes.get(targetPos)) ? 1 : 2;
        return resultWeight;
    }
}