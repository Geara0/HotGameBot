package entities.Levenshtein;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Модифицированное расстояние левенштайна с измененными весами замены, удаления и вставки символа и с учетом вероятности опечатки
 */
public class LevenshteinCalculator {
    public HashMap<Character, Integer> keyCodes = new HashMap<>();
    /**
     * Пары символ : символы, лежащие рядом на клавиатуре
     */
    private HashMap<Integer, ArrayList<Integer>> codeFamilies;

    /**
     * Конструктор класса, заполняет словарь с группами клавиш
     */
    public LevenshteinCalculator() {
        Gson gson = new Gson();
        try {
            //надо все это руками переписать непосредственно внутрь класса, чтобы занимало меньше вренми, но так лениво
            codeFamilies = gson.fromJson(Files.readString(Path.of(".\\JSONs\\DistanceCodeKey.json")), new TypeToken<HashMap<Integer, ArrayList<Integer>>>() {
            }.getType());
            keyCodes.putAll(gson.fromJson(Files.readString(Path.of(".\\JSONs\\CodeKeysEng.json")), new TypeToken<HashMap<Character, Integer>>() {
            }.getType()));
            keyCodes.putAll(gson.fromJson(Files.readString(Path.of(".\\JSONs\\CodeKeysRus.json")), new TypeToken<HashMap<Character, Integer>>() {
            }.getType()));
        } catch (IOException e) {
            codeFamilies = new HashMap<>();
            keyCodes = new HashMap<>();
        }
    }

    /**
     * Метод для поиска ближайшего совпадения в коллекции stringSet со строкой original
     *
     * @param stringSet коллекция строк для поиска
     * @param original  строка, расстояние до которой считаем
     * @return ближайшая по расстоянию строка
     */
    public String getClosestString(Set<String> stringSet, String original) {
        return getClosestStrings(stringSet, original, 1)[0];
    }

    /**
     * Метод для поиска count ближайших строк в коллекции stringSet со строкой original
     *
     * @param stringSet коллекия строк для поиска
     * @param original  строка, расстояние до которой считаем
     * @param count     количество желаемых результатов
     * @return массив строк в порядке увеличения расстояния до original
     */
    public String[] getClosestStrings(Set<String> stringSet, String original, int count) {
        String[] strings = new String[count];
        List<StringDoublePair> pairs = search(stringSet, original);
        for (int i = 0; i < count; i++) {
            strings[i] = pairs.get(i).getText();
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

        for (int j = 1; j <= len1; ++j)
            distance[0][j] = j * 2;

        int currentRow = 0;
        for (int i = 1; i <= len0; ++i) {
            currentRow = i % 3;
            int previousRow = (i - 1) % 3;
            distance[currentRow][0] = i * 2;
            for (int j = 1; j <= len1; j++) {
                int cost_insert = distance[previousRow][j] + getAddition(fullWord, i, len0);
                int cost_delete = distance[currentRow][j - 1] + getAddition(fullWord, i, len0);
                int cost_replace = distance[previousRow][j - 1] + replaceCost(source, i - 1, target, j - 1);
                distance[currentRow][j] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
                if (i > 1 && j > 1 && source.text.charAt(i - 1) == target.text.charAt(j - 2)
                        && source.text.charAt(i - 2) == target.text.charAt(j - 1))
                    distance[currentRow][j] = Math.min(distance[currentRow][j], distance[(i - 2) % 3][j - 2] + 2);
            }
        }
        return distance[currentRow][len1];
    }

    /**
     * Получает прибавку к удалению или вставке символа в зависимости от того, является ли текущий фрейм (смотрите {@link #getRangeWord(Word, Word)}) полным словом
     *
     * @param fullWord true - полное слово, false - неполное слово
     * @param i        переменная итерации по слову source
     * @param len0     длинна текста в source
     * @return 1 если текущий фрейм не полное слово и итерация по последнему символу слова
     * 2 если текущий фрейм полное слово или если итерация не по последнему символу
     */
    private int getAddition(boolean fullWord, int i, int len0) {
        return !fullWord && i == len0 ? 1 : 2;
    }

    /**
     * Получает лист пар "строка"-"расстояние до userText" упорядоченный по убыванию веса
     *
     * @param stringSet коллекция строк которым надо присудить веса
     * @param userText  строка для сравнения
     * @return Получает лист пар "строка"-"расстояние до userText" упорядоченный по убыванию веса
     */
    public List<StringDoublePair> search(Set<String> stringSet, String userText) {
        ArrayList<StringDoublePair> result = new ArrayList<>();
        AnaliseObject searchObj;
        if (userText.length() > 0)
            searchObj = new AnaliseObject(userText.toLowerCase(), keyCodes);
        else
            searchObj = new AnaliseObject();
        for (String toCompare : stringSet) {
            AnaliseObject objToCompare = new AnaliseObject(toCompare.toLowerCase(), keyCodes);
            double cost = getRangePhrase(objToCompare, searchObj);
            result.add(new StringDoublePair(toCompare, cost));
        }

        result.sort(StringDoublePair::compareTo);
        return result;
    }

    /**
     * Метод для сравнения расстояний между фразами с учетом перестановки удаления и вставвки целых слов
     *
     * @param source объект анализа на основе строки из stringSet, содержащий фразу
     * @param target объект анализа на основе данной строки, cодержащий фразу
     * @return расстояние между фразами
     */
    private double getRangePhrase(AnaliseObject source, AnaliseObject target) {
        if (source.words.isEmpty()) {
            if (target.words.isEmpty())
                return 0;
            return target.getLength() * 2;
        }
        if (target.words.isEmpty())
            return source.getLength() * 2;

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

    /**
     * Метод для вычисления расстояния между словами с учетом плавающего окна, которое позволяет производить сравнение подстрок
     *
     * @param source слово из фразы из строки из stringSet
     * @param target слово из фразы из строки для сравнения
     * @return расстояние между данными словами
     */
    private double getRangeWord(Word source, Word target) {
        double minDistance = Double.MAX_VALUE;
        Word croppedSource = new Word();
        int length = Math.min(source.text.length(), target.text.length() + 1);
        for (int i = 0; i <= source.text.length() - length && i <= length; i++) {
            croppedSource.text = source.text.substring(i, length);
            croppedSource.codes = source.codes.subList(i, length + i);
            minDistance = Math.min(minDistance, calculate(croppedSource, target, croppedSource.text.length() == source.text.length()));
        }
        return minDistance;
    }

    /**
     * Метод для вычисления замены символа слова source на *sourcePos* позиции на символ слова target на targetPos позиции
     *
     * @param source    слово из фразы из stringSet
     * @param sourcePos позиция символа из source цену для которого надо вычислить
     * @param target    слово из фразы из строки для сравнения
     * @param targetPos позиция символа из target цену для которого надо вычислить
     * @return целое число - стоимость замены символов
     */
    private int replaceCost(Word source, int sourcePos, Word target, int targetPos) {
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