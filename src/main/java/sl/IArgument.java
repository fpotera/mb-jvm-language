package sl;

/**
 * The SL_ARGUMENT module contains routines for handling command-line arguments.
 * 
 * @author Iulian Enache
 */
public interface IArgument {

	/**
	 * Function that returns the next command-line option that matches a letter in the format parameter
	 * @param args (format OPTION option [ARGUMENT argument])
	 * 				</br>The <code><b><i>array</i></b></code> parameter is the array whose elements correspond to the fields that constitutes the string to be returned. It should be a string array.
	 * 				</br>The <code><b><i>separator</i></b></code> parameter is the character that separates fields. It should be a one-character string.
	 * 				</br>&emsp;<code>The default separator is a <i>comma</i>.</code>
	 * @return a string denoting the next command-line option that matches a letter in the format parameter.
	 */
//	public abstract Object getoption(Object... args);

	/**
	 * Function that retrieves the remaining non-option command arguments (when all options have been parsed using SL_ARGUMENT.GetOption)
	 * @param args (argument-number)
	 * 				</br>The <code><b><i>argument-number</i></b></code> parameter specifies the command argument to retrieve. It should be an integer.
	 * 				</br>&emsp;<code>The first non-option command argument has <i>number one</i> and the number for the last is the same as the value returned by <i>SL_ARGUMENT.GetArgumentCount</i>.</code>
	 * 				</br>&emsp;<code>The specified non-option command argument is returned as a <i>string</i>.</code>
	 * @return a string denoting the specified non-option command argument.
	 */

//	public abstract Object getargument(Object... args);

//	public abstract Object getargumentcount(Object... args);

//	public abstract Object getinterpreterargument(Object... args);

//	public abstract Object getinterpreterargumentcount(Object... args);

//	public abstract Object getinterpreterfiles(Object... args);

	/**
	 * Function that returns the version number for the SL_ARGUMENT module as a string
	 * @return the version number for the SL_ARGUMENT module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

}
