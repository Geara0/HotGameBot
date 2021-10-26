import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Set<T> extends AbstractSet<T> {
    private final HashMap<T, T> map;

    public Set() {
        map = new HashMap<>();
    }

    public Set(HashSet<T> hashSet) {
        map = new HashMap<>();
        for (var el : hashSet)
            map.put(el, el);
    }

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
