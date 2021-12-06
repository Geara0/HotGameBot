package entities.Levenshtein;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.event.KeyValuePair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Word{
    public String text;
    public List<Integer> codes = new ArrayList<>();
    public HashMap<Character,Integer> keyCodes = new HashMap<>();

    public Word(){}

    public Word(String text){
        Gson gson = new Gson();
        try {
            keyCodes.putAll(gson.fromJson(Files.readString(Path.of(".\\JSONs\\CodeKeysEng.json")),new TypeToken<HashMap<Character,Integer>>(){}.getType()));
            keyCodes.putAll(gson.fromJson(Files.readString(Path.of(".\\JSONs\\CodeKeysRus.json")),new TypeToken<HashMap<Character,Integer>>(){}.getType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.text = text;
        this.codes = getKeyCodes(text);
    }

    private ArrayList<Integer> getKeyCodes(String word){
        ArrayList<Integer> result = new ArrayList<>();
        for(char c : word.toCharArray())
            result.add(keyCodes.getOrDefault(c,63));
        return result;
    }
}
