import java.io.IOException;
import java.util.ArrayList;

/**
 * Интерфейс класса, который парсит JSON
 */
public interface JsonParser{
    /**
     * Метод, получаюищий данные из JSON-файла
     * @param filePath путь до JSON-файла
     * @return коллекция данных о сущностях из JSON-файла
     */
     ArrayList getData(String filePath) throws IOException;
}
