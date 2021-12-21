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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class APIWorker implements IAPI {
    final String APIUrl = "https://api.hot-game.info/games_top.json";
    private static final Logger logger = LogManager.getLogger("API");

    /**
     * Реализация интерфейса
     *
     * @return лист тайтлов из апи
     */
    @Override
    public ArrayList<Title> getData() {
        List<APITitle> rawTitles = getTitlesFromApi();
        ArrayList<Title> result = new ArrayList<>();
        for (APITitle rawTitle : rawTitles) {
            var parsedTitle = convertParse(rawTitle);
            if (parsedTitle.getName() != null)
                result.add(convertParse(rawTitle));
        }
        logger.info("{} titles received from api", rawTitles.size());
        return result;
    }

    /**
     * //TODO: Если api обновят
     * Метод для преобразования АПИтайтла в обычный тайтл
     *
     * @param title АПИ тайтл для преобразования
     * @return объект тайтла, соответствующий АПИтайтлу
     */
    private Title convert(APITitle title) {
        IParser parser = new HotGameParser();
        Title result = new Title();
        return result;
    }

    /**
     * Метод для преобразования АПИтайтла в обычный тайтл через парсинг, тк HotGame не добавили в апи нужные ссылки
     *
     * @param title АПИ тайтл для преобразования
     * @return объект тайтла, соответствующий АПИтайтлу
     */
    private Title convertParse(APITitle title) {
        IParser parser = new HotGameParser();
        Title result = parser.parseTitleByLink(title.link);
        result.setId(Long.valueOf(title.id));
        if (!parser.getReport().equals(ReportState.OK))
            logger.warn("something went wrong, parser report with title {}, parser report: {}", title.title, parser.getReport());
        return result;
    }

    /**
     * Получает тайтлы из апи
     *
     * @return лист АПИтайтлов
     */
    private List<APITitle> getTitlesFromApi() {
        String json = getDataFromAPI();
        Map<String, List<APITitle>> parsedJson = new Gson().fromJson(json, new TypeToken<Map<String, List<APITitle>>>() {
        }.getType());
        return parsedJson.get("response");
    }

    /**
     * Получает json документ с апи
     *
     * @return json-строка
     */
    private String getDataFromAPI() {
        StringBuilder content = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(APIUrl).openConnection();
            connection.setRequestMethod("GET");
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            logger.debug("request response message: {}", connection.getResponseMessage());
            String inputLine;
            while ((inputLine = input.readLine()) != null)
                content.append(inputLine);
            connection.disconnect();
            input.close();
        } catch (IOException e) {
            logger.warn("something went wrong: {}", Arrays.toString(e.getStackTrace()));
        }
        return content.toString();
    }
}
