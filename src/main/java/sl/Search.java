package sl;

import resources.ApplicationProperties;

/**
 * The SL_SEARCH module contains routines for searching arrays.
 * 
 * @author Iulian Enache
 */
public class Search implements ISearch {

	private static Search instance;

	private Search() {
		super();
	}

	public static Search getModule() {
		if (instance == null) {
			synchronized (Search.class) {
				if (instance == null) {
					instance = new Search();
				}
			}
		}
		return instance;
	}

	@Override
	public Object binarysearch(Object... args) {
		java.lang.String[] arrayToSearch = (java.lang.String[]) args[0];
		java.lang.String toFind = (java.lang.String) args[1];
		int indexOrInsertionPoint = java.util.Arrays.binarySearch(arrayToSearch, toFind);
		return indexOrInsertionPoint < 0 ? 0 : indexOrInsertionPoint + 1;
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_SEARCHversion();
	}

}
