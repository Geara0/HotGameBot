package entities.Levenshtein;

import org.springframework.lang.NonNull;

public class Pair implements Comparable<Pair> {
    public String text;
    public Double number;

    public Pair(String text, Double number){
        this.text = text;
        this.number = number;
    }

    public String toText(){
        return toString();
    }

    @Override
    public int compareTo(@NonNull Pair o) {
        return this.number.compareTo(o.number);
    }

    @Override
    public String toString(){
        return text+"---- weight = "+number;
    }
}
