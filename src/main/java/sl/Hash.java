package sl;

import java.util.Hashtable;

import resources.ApplicationProperties;

/**
 * The SL_HASH module contains hash table routines.
 * 
 * @author Iulian Enache
 */
public class Hash implements IHash {

	private static Hash instance;

	private Hash() {
		super();
	}

	public static Hash getModule() {
		if (instance == null) {
			synchronized (Hash.class) {
				if (instance == null) {
					instance = new Hash();
				}
			}
		}
		return instance;
	}

	@Override
	public Object initialize(Object... args) {
		return new Hashtable<java.lang.String, java.lang.String>();
	}

	@Override
	public Object put(Object... args) {
		Hashtable<java.lang.String, java.lang.String> map = (Hashtable<java.lang.String, java.lang.String>) args[0];
		java.lang.String key = (java.lang.String) args[1];
		java.lang.String value = (java.lang.String) args[2];
		map.put(key, value);
		return map;
	}

	@Override
	public Object get(Object... args) {
		Hashtable<java.lang.String, java.lang.String> map = (Hashtable<java.lang.String, java.lang.String>) args[0];
		java.lang.String key = (java.lang.String) args[1];
		java.lang.String value = map.get(key);
		return value;
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_HASHversion();
	}

}
