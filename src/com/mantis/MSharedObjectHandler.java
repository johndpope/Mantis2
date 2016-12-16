package com.mantis;

import java.util.ArrayList;
import java.util.HashMap;

public class MSharedObjectHandler {

	private static ArrayList<HashMap<String, Object>> _sharedObjects = new ArrayList<HashMap<String, Object>>();

	/**
	 * Register a shared object.
	 * 
	 * @param name
	 *            The name of the object to register.
	 * @param level
	 *            The level in which to store the object.
	 * @param obj
	 *            The object to store.
	 */
	public static <T> void rso(String name, int level, T obj) {
		if (_sharedObjects.size() < (level + 1)) {
			_sharedObjects.add(new HashMap<String, Object>());
			_sharedObjects.get(level).put(name, obj);
		} else if (_sharedObjects.get(level) != null) {
			_sharedObjects.get(level).put(name, obj);
		} else {
			_sharedObjects.add(new HashMap<String, Object>());
			_sharedObjects.get(level).put(name, obj);
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * Gets a shared object.
	 * 
	 * @param name
	 *            The name of the object to fetch.
	 * @param level
	 *            The level the object is stored in.
	 * @return The queried object.
	 */
	public static <T> T gso(String name, int level) {
		return (T) _sharedObjects.get(level).get(name);
	}

	/**
	 * Removes a shared object.
	 * 
	 * @param name
	 *            The name of the object to remove.
	 * @param level
	 *            The level of the object.
	 */
	public static void rmso(String name, int level) {
		_sharedObjects.get(level).remove(name);
	}

	/**
	 * Sets a shared object.
	 * 
	 * @param name
	 *            The name of the object to modify.
	 * @param level
	 *            The object's storage level.
	 * @param obj
	 *            The new object.
	 */
	public static <T> void sso(String name, int level, T obj) {
		_sharedObjects.get(level).put(name, obj);
	}

	/**
	 * Checks for the existence of a shared object.
	 * 
	 * @param name
	 *            The name of the
	 * @param level
	 *            The level the object is stored in.
	 * @return Whether or not the given object is stored in the given level.
	 */
	public static boolean doesExist(String name, int level) {
		if (_sharedObjects.size() == 0)
			return false;
		return _sharedObjects.get(level).containsKey(name);
	}
}
