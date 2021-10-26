import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GenericParser <T> {
    //TODO: Почему не работает?
    public static <T> T parse(String path) throws IOException {
        return new Gson().fromJson(Files.readString(Path.of(path)), new TypeToken<T>(){}.getType());
    }
}
