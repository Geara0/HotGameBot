package HotGameParsing;

import Entities.Title;

public interface Parser {
    /**
     * Метод чтобы по уже известной ссылке получать данные об игре с сайта
     * @param link - https://hot-game.info/game/.... ссылка на страницу которую хотим распарсить
     * @return экземпляр Title с данными со страницы
     */
    Title parseTitle(String link);
}
