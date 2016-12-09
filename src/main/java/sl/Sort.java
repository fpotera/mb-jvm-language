package sl;

import resources.ApplicationProperties;

/**
 * The {@code SL_SORT} module contains routines for sorting arrays.
 * 
 * @author Iulian Enache
 */
public class Sort implements ISort {

	private static Sort instance;

	private Sort() {
		super();
	}

	public static Sort getModule() {
		if (instance == null) {
			synchronized (Sort.class) {
				if (instance == null) {
					instance = new Sort();
				}
			}
		}
		return instance;
	}

	@Override
	public Object heapsort_any(Object... args) {
		java.lang.String[] arrayToSort = (java.lang.String[]) args[0];
		return null;
	}

	@Override
	public Object heapsort_string(Object... args) {
		java.lang.String[] arrayToSort = (java.lang.String[]) args;
		int n = arrayToSort.length;
		for (int k = n / 2; k >= 1; k--)
			this.sink(arrayToSort, k, n);
		while (n > 1) {
			this.exchange(arrayToSort, 1, n--);
			this.sink(arrayToSort, 1, n);
		}
		return arrayToSort;
	}

	@Override
	public Object quicksort_string(Object... args) {
		java.lang.String[] arrayToSort = (java.lang.String[]) args[0];
		Long from = (args[1] != null) ? (Long) args[1] : Long.valueOf(0L);
		Long to = (args[2] != null) ? (Long) args[2] : Long.valueOf(arrayToSort.length - 1);
		int lowerIndex = from.intValue();
		int higherIndex = to.intValue();
		this.quickSort(arrayToSort, lowerIndex, higherIndex);
		return arrayToSort;
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_SORTversion();
	}

	/***************************************************************************
	 * Helper functions to restore the heap invariant.
	 ***************************************************************************/

	private void sink(Comparable[] arrayToSort, int k, int n) {
		while (2 * k <= n) {
			int j = 2 * k;
			if (j < n && this.less(arrayToSort, j, j + 1))
				j++;
			if (!this.less(arrayToSort, k, j))
				break;
			this.exchange(arrayToSort, k, j);
			k = j;
		}
	}

	/***************************************************************************
	 * Helper functions for comparisons and swaps. Indices are "off-by-one" to
	 * support 1-based indexing.
	 ***************************************************************************/
	private boolean less(Comparable[] arrayToSort, int i, int j) {
		return arrayToSort[i - 1].compareTo(arrayToSort[j - 1]) < 0;
	}

	private void exchange(Object[] arrayToSort, int i, int j) {
		Object swap = arrayToSort[i - 1];
		arrayToSort[i - 1] = arrayToSort[j - 1];
		arrayToSort[j - 1] = swap;
	}

	private boolean less(Comparable v, Comparable w) {
		return v.compareTo(w) < 0;
	}

	/**
	 * Recursive quickSort logic
	 * 
	 * @param arrayToSort
	 *            input array
	 * @param lowerIndex
	 *            start index of the array
	 * @param higherIndex
	 *            end index of the array
	 */
	private void quickSort(java.lang.String[] arrayToSort, int lowerIndex, int higherIndex) {
		int index = partition(arrayToSort, lowerIndex, higherIndex);
		// Recursively call quickSort with left part of the partitioned array
		if (lowerIndex < index - 1) {
			quickSort(arrayToSort, lowerIndex, index - 1);
		}
		// Recursively call quickSort with right part of the partitioned array
		if (higherIndex > index) {
			quickSort(arrayToSort, index, higherIndex);
		}
	}

	/**
	 * Divides array from pivot, left side contains elements less than Pivot
	 * while right side contains elements greater than Pivot.
	 * 
	 * @param arrayToSort
	 *            array to partition
	 * @param left
	 *            lower bound of the array
	 * @param right
	 *            upper bound of the array
	 * @return the partition index
	 */
	public int partition(java.lang.String[] arrayToSort, int left, int right) {
		java.lang.String pivot = arrayToSort[left]; // taking first element as
													// pivot
		while (left <= right) {
			// searching number which is greater than pivot, bottom up
			while (arrayToSort[left].compareTo(pivot) < 0) {
				left++;
			}
			// searching number which is less than pivot, top down
			while (arrayToSort[right].compareTo(pivot) > 0) {
				right--;
			}
			// swap the values
			if (left <= right) {
				java.lang.String tmp = arrayToSort[left];
				arrayToSort[left] = arrayToSort[right];
				arrayToSort[right] = tmp;
				// increment left index and decrement right index
				left++;
				right--;
			}
		}
		return left;
	}

}
