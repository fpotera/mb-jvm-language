package sl;

/**
 * The {@code SL_SORT} module contains routines for sorting arrays.
 * 
 * @author Iulian Enache
 */
public interface ISort {

	/**
	 * <code><b><i>SL_SORT.HeapSort_Any</i></b></code> - Function that sorts an array using the heap-sort algorithm
	 * @param args (array, compare-function)
	 * 				</br>The <code><b><i>array</i></b></code> parameter is the array to be sorted.
	 * 				</br>The <code><b><i>compare-function</i></b></code> parameter is the name of the function used to compare two elements from array.
	 * 				</br>&emsp;<code>It should take two arguments (the array elements to compare) and return minus one if the first element is less than the second, zero if the elements are equal, or plus one if the first element is greater than the second one.</code>
	 * @return the sorted array.
	 */
	public abstract <T> T[] heapsort_any(Object... args);

	/**
	 * <code><b><i>SL_SORT.HeapSort_String</i></b></code> - Function that sorts an array of strings using the heap-sort algorithm
	 * @param args (array)
	 * 				</br>The <code><b><i>array</i></b></code> parameter is the array to be sorted. It should be a one-dimensional string array.
	 * @return the sorted array.
	 */
	public abstract Object heapsort_string(Object... args);

	/**
	 * <code><b><i>SL_SORT.QuickSort_String</i></b></code> - Function that sorts an array of strings, or a part of it, using the quick-sort algorithm
	 * @param args (array, [from], [to])
	 * 				</br>The <code><b><i>array</i></b></code> parameter is the array to be sorted. It should be a one-dimensional string array.
	 * 				</br>The <code><b><i>from</i></b></code> parameter is the index of the first element to include in the sort. It should be an integer.
	 * 				</br>&emsp;<code>The default for <b><i>from</i></b> argument is <i>one</i>.</code>
	 * 				</br>The <code><b><i>to</i></b></code> parameter is the index of the last element to include in the sort. It should be an integer.
	 * 				</br>&emsp;<code>The default for <b><i>to</i></b> argument is <i>the size of the array</i>.</code>
	 * @return the sorted array.
	 */
	public abstract Object quicksort_string(Object... args);

	/**
	 * <code><b><i>SL_SORT.Version</i></b></code> - Function that returns the version number for the SL_SORT module as a string
	 * @return the version number for the SL_SORT module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

}
