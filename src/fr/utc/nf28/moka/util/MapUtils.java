package fr.utc.nf28.moka.util;

import java.util.HashMap;

public final class MapUtils {
	public static <K, V> MapBuilder<K, V> asMap(K key, V value) {
		return new MapBuilder<K, V>().entry(key, value);
	}

	public static final class MapBuilder<K, V> extends HashMap<K, V> {
		public MapBuilder<K, V> entry(K key, V value) {
			this.put(key, value);
			return this;
		}
	}
}