package API;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parsing.HotGameParser;
import parsing.IParser;
import parsing.ReportState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class APIWorker implements IAPI {
    final String APIUrl = "https://api.hot-game.info/games_top.json";
    private static final Logger logger = LogManager.getLogger("API");

    @Override
    public ArrayList<Title> getData() {
        List<APITitle> rawTitles = getTitlesFromApi();
        ArrayList<Title> result = new ArrayList<>();
        for (APITitle rawTitle : rawTitles)
            result.add(convert(rawTitle));
        return result;
    }

    private Title convert(APITitle title) {
        IParser parser = new HotGameParser();
        Title result = parser.parseTitleByLink(title.link);
        if (!parser.getReport().equals(ReportState.OK))
            logger.warn("something went wrong, parser report: {}", parser.getReport());
        return result;
    }

    private List<APITitle> getTitlesFromApi() {
        String json = getDataFromAPI();
        Map<String, List<APITitle>> parsedJson = new Gson().fromJson(json, new TypeToken<Map<String, List<APITitle>>>() {
        }.getType());
        return parsedJson.get("response");
    }

    private String getDataFromAPI() {
        StringBuilder content = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(APIUrl).openConnection();
            connection.setRequestMethod("GET");
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = input.readLine()) != null)
                content.append(inputLine);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
