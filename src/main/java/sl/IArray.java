package sl;

/**
 * The SL_ARRAY module contains array-handling routines.
 * 
 * @author Iulian Enache
 */
public interface IArray {

	/**
	 * <code><b><i>SL_ARRAY.Insert</i></b></code> - Function (procedure) that inserts an element into an array
	 * @param args (array, index, element)
	 * 				</br>The <code><b><i>array</i></b></code> parameter is the array to insert the <code><b><i>element</i></b></code> into.
	 * 				</br>The <code><b><i>index</i></b></code> parameter is the position at which to insert the <code><b><i>element</i></b></code>. It can be any integer >= 1.
	 * 				</br>The <code><b><i>element</i></b></code> parameter is the element to insert. It should be of the same type as the elements of array.
	 * 				</br>&emsp;<code>If <b><i>index</i></b> argument is less than or equal to the size of array, all elements that have an index greater than or equal to index are moved up one position, that is, their index is incremented by 1.</code>
	 * 				</br>&emsp;<code>If <b><i>index</i></b> argument is greater than the size of array, no elements are moved up.</code>
	 * @return the modified array.
	 */
	public abstract Object[] insert(Object... args);

	public abstract Object[] append(Object... args);

	public abstract Object[] delete(Object... args);

	/**
	 * <code><b><i>SL_ARRAY.Version</i></b></code> - Function that returns the version number for the SL_ARRAY module as a string
	 * @return the version number for the SL_ARRAY module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

}
