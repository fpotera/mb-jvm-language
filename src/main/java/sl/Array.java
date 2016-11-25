package sl;

import java.util.ArrayList;
import java.util.List;

import resources.ApplicationProperties;

/**
 * The SL_ARRAY module contains array-handling routines.
 * 
 * @author Iulian Enache
 */
public class Array implements IArray {

	private static Array instance;

	private Array() {
		super();
	}

	public static Array getModule() {
		if (instance == null) {
			synchronized (Array.class) {
				if (instance == null) {
					instance = new Array();
				}
			}
		}
		return instance;
	}

	@Override
	public Object[] insert(Object... args) {
		Long index = Long.parseLong(java.lang.String.valueOf(args[1]));
		Object element = (Object) args[2];

		int arrayLength = java.lang.reflect.Array.getLength(args[0]);

		Object newArray = java.lang.reflect.Array.newInstance(args[0].getClass().getComponentType(), arrayLength + 1);
		int position;
		if (index.intValue() <= 0) {
			position = 0;
		} else if (index.intValue() <= arrayLength + 1) {
			position = index.intValue() - 1;
		} else {
			position = arrayLength;
		}
		java.lang.reflect.Array.set(newArray, position, element);

		for (int i = 0; i < arrayLength; i++) {
			Object value = java.lang.reflect.Array.get(args[0], i);
			if (i < position) {
				java.lang.reflect.Array.set(newArray, i, value);
			} else {
				java.lang.reflect.Array.set(newArray, i + 1, value);
			}
		}
		return (Object[]) newArray;
		/*
		List<? super Object> newArray = new ArrayList<>(arrayLength);
		for (int i = 0; i < arrayLength; i++) {
			newArray.add(java.lang.reflect.Array.get(args[0], i));
		}
		if (index.intValue() <= 0) {
			newArray.add(0, element);
		} else if (index.intValue() <= newArray.size()) {
			newArray.add(index.intValue() - 1, element);
		} else {
			newArray.add(element);
		}
		return newArray.toArray();
		*/
	}

	@Override
	public Object[] append(Object... args) {
		Object element = (Object) args[1];
		int arrayLength = java.lang.reflect.Array.getLength(args[0]);
		return this.insert(args[0], arrayLength + 1, element);
	}

	@Override
	public Object[] delete(Object... args) {
		Long index = Long.parseLong(java.lang.String.valueOf(args[1]));
		int arrayLength = java.lang.reflect.Array.getLength(args[0]);
		/*
		List<? super Object> newArray = new ArrayList<>(arrayLength);
		for (int i = 0; i < arrayLength; i++) {
			newArray.add(java.lang.reflect.Array.get(args[0], i));
		}
		newArray.remove(index.intValue() - 1);
		return newArray.toArray();
		*/
		Object newArray = java.lang.reflect.Array.newInstance(args[0].getClass().getComponentType(), arrayLength - 1);
		int position;
		if (index.intValue() <= 0) {
			position = 0;
		} else if (index.intValue() <= arrayLength) {
			position = index.intValue() - 1;
		} else {
			position = arrayLength - 1;
		}
		for (int i = 0; i < arrayLength; i++) {
			Object value = java.lang.reflect.Array.get(args[0], i);
			if (i < position) {
				java.lang.reflect.Array.set(newArray, i, value);
			} else if (i == position) {
				continue;
			} else {
				java.lang.reflect.Array.set(newArray, i - 1, value);
			}
		}
		return (Object[]) newArray;
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_ARRAYversion();
	}

}
