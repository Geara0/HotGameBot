package parsing;

import entities.Title;

import java.util.ArrayList;

/**
 * Интерфейс парсера
 */
public interface IParser {
    //TODO: Получить рекомендации с парсера
    String[] getRecommendations(String... params);

    /**
     * Получает экземпляр тайтла по переданной ссылке
     *
     * @param link ссылка hot-game.info/game/* на тайтл
     * @return экземпляр тайтла, на страницу которого ведет ссылка
     */
    Title parseTitleByLink(String link);

    /**
     * Получает результат поискового запроса на сайте hot-game.info/ по заданному имени
     *
     * @param name имя тайтла для поиска
     * @return коллекция тайтлов - первая страница результатов поиска
     */
    ArrayList<Title> parseTitlesByName(String name);

    /**
     * Геттер для состояния парсера
     *
     * @return енам с текущим состоянием парсера
     */
    ReportState getReport();
}
