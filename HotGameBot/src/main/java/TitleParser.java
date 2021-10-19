/*
  Класс для парсинга Title-ов из JSON-файла
  @version 1.0
 * @author Вадим
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * наследуем интерфейс JSONParser
 */
public class TitleParser implements JsonParser {
    /**
     * Реализация интерфейса
     * @param filePath путь до JSON-файла
     * @return List<Title> коллекция объектов класса Title
     * @throws IOException - не знаю зачем нужен, но он протащился по всем getData, надо узнать зачем он нужен
     */
    @Override
    public ArrayList<Title> getData(String filePath) throws IOException{
        Gson gson = new Gson(); //создаем новый Gson
        ArrayList<Title> titles; //лист для хранения тайтлов из жсона
        String content = Files.readString(Path.of(filePath)); //читаем весь файл в один стринг
        titles = gson.fromJson(content, new TypeToken<List<Title>>(){}.getType()); //получаем лист тайтлов
        return titles;
    }
}