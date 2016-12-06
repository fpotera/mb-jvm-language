package sl;

/**
 * The SL_PATH module contains path name handling routines.
 * 
 * @author Iulian Enache
 */
public interface IPath {

	/**
	 * <code><b><i>SL_PATH.Filter</i></b></code> - Function that removes redundant sequences of characters from the path argument
	 * @param args (path)
	 * 				</br>The <code><b><i>path</i></b></code> parameter is a sequence of characters denoting the path. It should be a string.
	 * 				</br>&emsp;<code>On a <b><i>POSIX file system (Unix-like systems)</i></b>, forward slashes are expected to separate path name components.</code>
	 * 				</br>&emsp;<code>On <b><i>Windows systems</i></b>, backward slashes separate path name components.</code>
	 * @return the path argument with the redundant sequences of characters removed.
	 * 				</br>&emsp;The returned value is a string.
	 */
	public abstract Object filter(Object... args);

	/**
	 * <code><b><i>SL_PATH.Compare</i></b></code> - Function that compares two path names
	 * @param args (path1, path2)
	 * 				</br>The <code><b><i>path1</i></b></code> parameter is a sequence of characters denoting the first path. It should be a string.
	 * 				</br>The <code><b><i>path2</i></b></code> parameter is a sequence of characters denoting the second path. It should be a string.
	 * 				</br>&emsp;<code>On a <b><i>POSIX file system (Unix-like systems)</i></b>, this function just do an ordinary string compare.</code>
	 * 				</br>&emsp;<code>On <b><i>Windows systems</i></b>, lower case letters in both path names are converted to upper case letters before the path names are compared.</code>
	 * @return <code>nonzero</code> if the path names are equal.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object compare(Object... args);

	/**
	 * <code><b><i>SL_PATH.CompareFilter</i></b></code> - Function that filters and compares two path names
	 * 			</br>&emsp;&emsp;It is equivalent to calling: <code>SL_PATH.Compare(SL_PATH.Filter(path1), SL_PATH.Filter(path2))</code>.
	 * @param args (path1, path2)
	 * 				</br>The <code><b><i>path1</i></b></code> parameter is a sequence of characters denoting the first path. It should be a string.
	 * 				</br>The <code><b><i>path2</i></b></code> parameter is a sequence of characters denoting the second path. It should be a string.
	 * 				</br>&emsp;<code>On a <b><i>POSIX file system (Unix-like systems)</i></b>, forward slashes are expected to separate path name components and this function just do an ordinary string compare.</code>
	 * 				</br>&emsp;<code>On <b><i>Windows systems</i></b>, backward slashes separate path name components and lower case letters in both path names are converted to upper case letters before the path names are compared.</code>
	 * @return <code>nonzero</code> if the path names are equal.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object comparefilter(Object... args);

	/**
	 * <code><b><i>SL_PATH.Version</i></b></code> - Function that returns the version number for the SL_PATH module as a string
	 * @return the version number for the SL_PATH module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

}
