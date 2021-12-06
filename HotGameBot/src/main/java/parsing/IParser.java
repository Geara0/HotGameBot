package parsing;

import entities.Title;

import java.util.ArrayList;

public interface IParser {
    //TODO: Получить рекомендации с парсера
    String[] getRecommendations(String... params);

    Title parseTitleByLink(String link);

    ArrayList<Title> parseTitlesByName(String name);

    ReportState getReport();
}
