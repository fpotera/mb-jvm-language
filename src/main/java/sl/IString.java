package sl;

/**
 * The SL_STRING module contains string-handling routines.
 * 
 * @author Iulian Enache
 */
public interface IString {

	/**
	 * Error if the field-number argument is less than or equal to zero.
	 */
	public static final java.lang.String error_fieldextract_illegalfieldnumber = "Error_FieldExtract_IllegalFieldNumber";

	/**
	 * Error if the field-number argument is greater than the number of fields in string.
	 */
	public static final java.lang.String error_fieldextract_fieldnumberoutsidestring = "Error_FieldExtract_FieldNumberOutsideString";

	/**
	 * Function that converts all characters in the function argument from ASCII (actually ISO8859-1) characters to EBCDIC (code page 500) characters
	 * @param args (string)
	 * 				</br>The <code><b><i>string</i></b></code> parameter is the string to be converted.
	 * @return the resulting EBCDIC string.
	 */
	public abstract Object asciitoebcdic(Object... args);

	/**
	 * Function that converts all characters in the function argument from EBCDIC (code page 500) characters to ASCII (actually ISO8859-1) characters
	 * @param args (string)
	 * 				</br>The <code><b><i>string</i></b></code> parameter is the string to be converted.
	 * @return the resulting ISO8859-1 string.
	 */
	public abstract Object ebcdictoascii(Object... args);

	/**
	 * Function that returns the number of fields in a string as an integer
	 * @param args (string[, separators])
	 * 				</br>The <code><b><i>string</i></b></code> parameter is the string to count the number of fields in. It should be of string type.
	 * 				</br>The <code><b><i>separator</i></b></code> parameter specifies all characters that act as field separators.
	 * 				</br>&emsp;<code>The default separator is a <i>comma</i>.</code>
	 * @return the number of fields in a string as an integer.<code></br>If the string argument is an empty string, zero is returned.</br>Otherwise, the return value is always greater than zero.</code>
	 */
	public abstract Object fieldcount(Object... args);

	/**
	 * Function that returns the number of fields in a string as an integer
	 * @param args (string, field-number[, separators])
	 * 				</br>The <code><b><i>string</i></b></code> parameter is the string to extract the field from. It should be of string type.
	 * 				</br>The <code><b><i>field-number</i></b></code> parameter is the field to extract.</br>&emsp;<code>The first field has number 1.</code>
	 * 				</br>The <code><b><i>separator</i></b></code> parameter specifies all characters that act as field separators.
	 * 				</br>&emsp;<code>The default separator is a <i>comma</i>.</code>
	 * @return the specified field from a string.<code></br>If the string argument is an empty string, zero is returned.</br>Otherwise, the return value is always greater than zero.</code>
	 */
	public abstract Object fieldextract(Object... args);

	/**
	 * Function that extracts the fields from a string
	 * @param args (string[, separators])
	 * 				</br>The <code><b><i>string</i></b></code> parameter is the string to extract fields from. It should be of string type.
	 * 				</br>The <code><b><i>separator</i></b></code> parameter is the character that separates fields. It should be a one-character string.
	 * 				</br>&emsp;<code>The default separator is a <i>comma</i>.</code>
	 * @return a string array, containing all the different fields.<code></br>If the string is empty, an empty array is returned.</code>
	 */
	public abstract Object fieldexplode(Object... args);

	/**
	 * Function that builds and returns a string whose fields are the elements of a string array
	 * @param args (array[, separator])
	 * 				</br>The <code><b><i>array</i></b></code> parameter is the array whose elements correspond to the fields that constitutes the string to be returned. It should be a string array.
	 * 				</br>The <code><b><i>separator</i></b></code> parameter is the character that separates fields. It should be a one-character string.
	 * 				</br>&emsp;<code>The default separator is a <i>comma</i>.</code>
	 * @return a string whose fields are the elements of the array.
	 */
	public abstract Object fieldimplode(Object... args);

	/**
	 * Function that reverses the contents of the string parameter
	 * @param args (string)
	 * 				</br>The <code><b><i>string</i></b></code> parameter is the string to be reversed.
	 * @return the reversed string.
	 */
	public abstract Object reverse(Object... args);

	/**
	 * Function that converts all uppercase letters from a string to lowercase
	 * @param args (string)
	 * 				</br>The <code><b><i>string</i></b></code> parameter is the string to be converted.
	 * @return the converted string.
	 */
	public abstract Object tolower(Object... args);

	/**
	 * Function that converts all lowercase letters from a string to uppercase
	 * @param args (string)
	 * 				</br>The <code><b><i>string</i></b></code> parameter is the string to be converted.
	 * @return the converted string.
	 */
	public abstract Object toupper(Object... args);

	/**
	 * Function that returns the version number for the SL_STRING module as a string
	 * @return the version number for the SL_STRING module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

}
