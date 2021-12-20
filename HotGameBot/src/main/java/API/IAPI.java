package API;

import entities.Title;

import java.util.ArrayList;
/**
 * Интерфейс обработчика апи
 */
public interface IAPI {
    /**
     * Метод для получения данных с апи
     * @return лист тайтлов из апи
     */
    ArrayList<Title> getData();
}
