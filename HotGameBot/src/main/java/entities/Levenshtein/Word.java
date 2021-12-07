package entities.Levenshtein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Word {
    /**
     * Текст слова
     */
    public String text;
    /**
     * Коллекция кодов, которым соответствуют символы в слове
     */
    public List<Integer> codes = new ArrayList<>();
    /**
     * Все пары "символ"-"код на клавиатуре" //надо убрать отсюда, занимает чудовищно много времени и места
     */
    public HashMap<Character, Integer> keyCodes = new HashMap<>();

    /**
     * Пустой конструктор для нужд трудящихся
     */
    public Word() {
    }

    /**
     * Конструктор слова, принимает на вход текст слова, заполняет коллекцию codes
     *
     * @param text текст слова
     */
    public Word(String text, HashMap<Character, Integer> keyCodes) {
        this.text = text;
        this.keyCodes = keyCodes;
        this.codes = getKeyCodes(text);
    }

    /**
     * Метод для заполнения codes
     *
     * @param word текст слова
     * @return коллекция целых чисел - кодов символов
     */
    private ArrayList<Integer> getKeyCodes(String word) {
        ArrayList<Integer> result = new ArrayList<>();
        for (char c : word.toCharArray())
            result.add(keyCodes.getOrDefault(c, 63));
        return result;
    }
}
