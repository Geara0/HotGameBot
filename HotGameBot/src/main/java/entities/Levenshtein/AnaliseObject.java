package entities.Levenshtein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс объекта для анализа в LevenshteinCalculator
 */
class AnaliseObject {

    /**
     * Коллекция слов, на которые разбивается строка
     */
    public List<Word> words = new ArrayList<>();
    /**
     * Оригинал строки для анализа
     */
    private String original;

    /**
     * Пустой конструктор для нужд трудящихся
     */
    public AnaliseObject() {
        original = "";
        words = new ArrayList<>();
    }

    /**
     * Нормальный конструктор, принимает строку для анализа и разбивает её на слова
     *
     * @param text строка для анализа
     */
    public AnaliseObject(String text, HashMap<Character, Integer> keyCodes) {
        this.original = text;
        var splitted = text.split(" ");
        for (String word : splitted)
            words.add(new Word(word, keyCodes));
    }

    /**
     * Возвращает длину исходной строки для аналзиа
     *
     * @return целое число - длина строки
     */
    public int getLength() {
        return original.length();
    }
}
