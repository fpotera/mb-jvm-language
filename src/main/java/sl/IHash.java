package sl;

/**
 * The SL_HASH module contains hash table routines.
 * 
 * @author Iulian Enache
 */
public interface IHash {

	/**
	 * Error if the hash table contains no value with <code><b><i>key</i></b></code> as a key.
	 */
	public static final java.lang.String error_get_keynotfound = "Error_Get_KeyNotFound";

	/**
	 * <code><b><i>SL_HASH.Initialize</i></b></code> - Function that initializes a new hash table and returns a handle to it
	 * @param args ()
	 * @return an anonymous record of type SL_HASH.Handle.
	 * 				</br>The <code><b><i>handle</i></b></code> is needed when inserting new values into and retrieving values from the hash table.
	 */
	public abstract Object initialize(Object... args);

	/**
	 * <code><b><i>SL_HASH.Put</i></b></code> - Function that inserts a new value into the hash table specified by handle
	 * @param args (handle, key, value)
	 * 				</br>The <code><b><i>handle</i></b></code> parameter is a record of type SL_HASH.Handle previously returned by the SL_HASH.Initialize function.
	 * 				</br>The <code><b><i>key</i></b></code> parameter is the key corresponding to the value. It should be a string.
	 * 				</br>The <code><b><i>value</i></b></code> parameter is the value itself. It should be a string.
	 * 				</br>&emsp;<code>If the specified <b><i>key</i></b> already exists in the hash table, the old <i>value</i> is replaced by <i>value</i>.</code>
	 * @return an updated hash table.
	 */
	public abstract Object put(Object... args);

	/**
	 * <code><b><i>SL_HASH.Get</i></b></code> - Function that retrieves the value corresponding to the key from the hash table specified by handle
	 * @param args (handle, key)
	 * 				</br>The <code><b><i>handle</i></b></code> parameter is a record of type SL_HASH.Handle previously returned by the SL_HASH.Initialize function.
	 * 				</br>The <code><b><i>key</i></b></code> parameter is the key corresponding to the value. It should be a string.
	 * @return the <code><b><i>value</i></b></code> corresponding to the <b><i>key</i></b>.
	 */
	public abstract Object get(Object... args);

	/**
	 * <code><b><i>SL_HASH.Version</i></b></code> - Function that returns the version number for the SL_HASH module as a string
	 * @return the version number for the SL_HASH module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

}
