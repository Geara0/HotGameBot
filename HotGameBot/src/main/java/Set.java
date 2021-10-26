import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Как HashSet, но может возвращать значение
 * @param <T> дженерик параметр
 */
public class Set<T> extends AbstractSet<T> {
    /**
     * HashMap, который и хранит все элементы
     */
    private final HashMap<T, T> map;

    /**
     * Пустой конструктор
     */
    public Set() {
        map = new HashMap<>();
    }

    /**
     * Конструктор, принимающий итерируемую коллекцию
     * @param iterable итерируемая коллекция для клонирования
     */
    public Set(Iterable<T> iterable) {
        map = new HashMap<>();
        for (var el : iterable)
            map.put(el, el);
    }

    /**
     * get, которого так нехватало в HashSet
     * @param value что получить
     * @return запрошенный элемент
     */
    public T get(T value) {
        return map.get(value);
    }

    @Override
    public boolean add(T t) {
        if (map.containsKey(t)) return false;
        map.put(t,t);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }
}
