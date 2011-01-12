package org.jfugue.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MapUtils {

	/**
	 * No instances.
	 */
	private MapUtils() {}
	
	public static <T> Map<T, T> convertArrayToImutableMap(T[][] array) {
		Map<T, T> map = new HashMap<T, T>(array.length);
		for (T[] ts : array) {
			map.put(ts[0], ts[1]);
		}
		return Collections.unmodifiableMap(map);
	}
}
