package entities.Levenshtein;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Объект, представляющий пару "строка"-"расстояние до искомой строки", должен использоваться только в LevenshteinCalculator
 */
public class StringDoublePair implements Comparable<StringDoublePair> {
    /**
     * строка для которой было проведено сравнение
     */
    private final String text;
    /**
     * расстояние от данной строки до другой, с коотрой провоидилось сравнение
     */
    private final Double number;

    /**
     * Конструктор пары
     *
     * @param text   строка для которой было проведено сравнение
     * @param number расстояние для данной строки
     */
    public StringDoublePair(String text, Double number) {
        this.text = text;
        this.number = number;
    }

    public String getTextWithWeight() {
        return toString();
    }

    public String getText() {
        return text;
    }

    public Double getWeight() {
        return number;
    }

    /**
     * реализация интерфейса Comparable
     *
     * @param o - элемент для сравнения
     * @return результат сравнения
     */
    @Override
    public int compareTo(@NonNull StringDoublePair o) {
        return this.number.compareTo(o.number);
    }

    @Override
    public String toString() {
        return text + "---- weight = " + number;
    }
}
