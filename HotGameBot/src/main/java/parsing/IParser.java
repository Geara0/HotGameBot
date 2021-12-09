package parsing;

import entities.Title;

import java.util.ArrayList;

/**
 * Интерфейс парсера
 */
public interface IParser {
    /**
     * Метод, возвращающий подборку тайтлов по заданным параметрам
     * @param params параметры для подборки. Список всех возможных аргументов:<br>
     *               price=[0-39250],[0-39250], второе число больше первого, пример: price=100,999<br>
     *               <br>
     *               year=[1950-2022],[1950-2022], второе число больше первого, пример: year=2019,2020<br>
     *               <br>
     *               mode=[mmo|multiplayer|singleplayer] можно писать любой из вариантов, если несколько - через запятую<br>
     *               <br>
     *               platforms=[linux|mac|nintendo|nintendo_3ds|nintendo_switch|nintendo_wii|nintendo_wiiu|pc|ps|ps_3|ps_4|ps_5|ps_p|ps_vita|xbox|xbox_360|xbox_one|xbox_sx] можно писать любой из вариантов, если несколько - через запятую<br>
     *               <br>
     *               genres=[action|adventure|dlc|early-access|fighting|horror|mmog|platformer|puzzle|race|rpg|shooter|simulator|sport|strategy|survival|vr|casual] можно писать любой из вариантов, если несколько - через запятую<br>
     *               <br>
     *               tags=[battle-net|cross-play|epic-games|gog,indie|origin|steam|uplay] можно писать любой из вариантов, если несколько - через запятую
     * @return коллекция тайтлов по заданным параметрам
     */
    ArrayList<Title> getRecommendations(String... params);

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
