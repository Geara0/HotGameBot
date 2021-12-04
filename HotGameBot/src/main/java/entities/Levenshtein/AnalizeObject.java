package entities.Levenshtein;

import java.util.ArrayList;
import java.util.List;

public class AnalizeObject {
    public String original;
    public List<Word> words = new ArrayList<>();

    public AnalizeObject(){
        new AnalizeObject("");
    }

    public AnalizeObject(String text){
        this.original = text;
        var splitted = text.split(" ");
        for (String word : splitted)
            words.add(new Word(word));
    }
}
