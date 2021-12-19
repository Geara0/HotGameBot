package API;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Title;
import parsing.HotGameParser;
import parsing.IParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HotGameApiWorker implements APIWorker{
    final String APIUrl = "https://api.hot-game.info/games_top.json";

    @Override
    public ArrayList<Title> getData() {
        List<APITitle> rawTitles = getTitlesFromApi();
        ArrayList<Title> result = new ArrayList<>();
        for (APITitle rawTitle : rawTitles)
            result.add(convert(rawTitle));
        return result;
    }

    private Title convert(APITitle title){
        IParser parser = new HotGameParser();
        System.out.println(title.title);
        return parser.parseTitleByLink(title.link);
    }

    private List<APITitle> getTitlesFromApi(){
        String json = getDataFromAPI();
        Map<String, List<APITitle>> parsedJson = new Gson().fromJson(json,new TypeToken<Map<String,List<APITitle>>>(){}.getType());
        return parsedJson.get("response");
    }

    private String getDataFromAPI(){
        StringBuilder content = new StringBuilder();
        try{
            HttpURLConnection connection = (HttpURLConnection) new URL(APIUrl).openConnection();
            connection.setRequestMethod("GET");
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while((inputLine=input.readLine())!=null)
                content.append(inputLine);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static void main(String[] args){
        var apiWorker = new HotGameApiWorker();
        var start = Instant.now();
        var titles = apiWorker.getData();
        var stop = Instant.now();
        System.out.println(Duration.between(start,stop).toMillis());
    }
}
