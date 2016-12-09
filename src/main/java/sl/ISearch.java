package sl;

/**
 * The SL_SEARCH module contains routines for searching arrays.
 * 
 * @author Iulian Enache
 */
public interface ISearch {

	/**
	 * <code><b><i>SL_SEARCH.BinarySearch</i></b></code> - Function that uses binary search to search the string array for an occurrence of the string "search"
	 * @param args (array, search)
	 * 				</br>The <code><b><i>array</i></b></code> parameter is the array to search into. It should be a string array.
	 * 				</br>&emsp;<code>The array must be sorted.</code>
	 * 				</br>The <code><b><i>search</i></b></code> parameter is the string to search for. It should be a string.
	 * @return the <code>index for the matching element</code> if <i>search</i> is found.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object binarysearch(Object... args);

	/**
	 * <code><b><i>SL_SEARCH.Version</i></b></code> - Function that returns the version number for the SL_SEARCH module as a string
	 * @return the version number for the SL_SEARCH module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

}
