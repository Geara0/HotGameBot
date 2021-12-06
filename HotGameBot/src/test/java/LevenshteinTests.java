import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Levenshtein.LevenshteinCalculator;
import entities.Title;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LevenshteinTests {

    private final String titlesPath = ".\\JSONs\\Titles";

    @Test
    public void justRun() throws IOException {
        LevenshteinCalculator levenshtein = new LevenshteinCalculator();
        HashMap<String, Title> titles = new Gson().fromJson(Files.readString(Path.of(titlesPath)), new TypeToken<HashMap<String, Title>>() {
        }.getType());
        ArrayList<TestType> l = new Gson().fromJson(Files.readString(Path.of("C:\\Users\\Вадим\\Downloads\\finally.json")),new TypeToken<ArrayList<TestType>>(){}.getType());
        var set = l.stream().map(a -> a.title).toList();
        var results  = levenshtein.search(new HashSet<>(set),"ghost");
        for(var result : results)
            System.out.println(result.toText());
    }
}

class TestType{
    String title;
}
