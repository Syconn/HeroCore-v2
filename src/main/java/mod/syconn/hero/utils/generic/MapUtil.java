package mod.syconn.hero.utils.generic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MapUtil {

    public static <K, V> Map<K, V> of(K key, V value) {
        var map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> add(K key, V value, Map<K, V>... maps) {
        var map = MapUtil.join(maps);
        map.put(key, value);
        return map;
    }
    @SafeVarargs
    public static <K, V> Map<K, V> remove(K key, Map<K, V>... maps) {
        var map = MapUtil.join(maps);
        map.remove(key);
        return map;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> join(Map<K, V>... maps) {
        var joined = new HashMap<K, V>();
        for (var map : maps) if (map != null) joined.putAll(map);
        return joined;
    }

    public static <V, R> Map<V, R> make(Map<V, R> map, Consumer<Map<V, R>> putter) {
        putter.accept(map);
        return map;
    }

    public static <V, R> R makeAndGet(Map<V, R> map, Consumer<Map<V, R>> putter, V key) {
        return MapUtil.make(map, putter).get(key);
    }
}
