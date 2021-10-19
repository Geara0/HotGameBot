import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class UserSubsParser implements JsonParser{
    @Override
    public ArrayList<User> getData(String filePath) throws IOException {
        Gson gson = new Gson();
        String content = Files.readString(Path.of(filePath));
        return gson.fromJson(content, new TypeToken<ArrayList<User>>(){}.getType());
    }
}
