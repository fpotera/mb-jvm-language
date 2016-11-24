package sl;

/**
 * The SL_DATE module contains date handling routines.
 * 
 * @author Iulian Enache
 */
public interface IDate {

	/**
	 * The <code><b><i>$DateFormat</i></b></code> reserved variable.
	 */
//	public static final java.lang.String dateformat = "%y%m%d";

	/**
	 * Error if the content of string is not compatible with the format.
	 */
	public static final java.lang.String error_stringtodate_convert = "Error_StringToDate_Convert";

	/**
	 * <code><b><i>SL_DATE.StringToDate</i></b></code> - Function that converts a string containing a date of a specified format to a date value
	 * @param args (string, format)
	 * 				</br>The <code><b><i>string</i></b></code> parameter is the string to convert to a date value. It should be a string.
	 * 				</br>The <code><b><i>format</i></b></code> parameter is the format string. It should be a string.
	 * 				</br>&emsp;<code>The <b><i>format</i></b> argument should be the same as for the <i>$DateFormat</i> reserved variable.</code>
	 * @return a date value.
	 */
	public abstract Object stringtodate(Object... args);

	/**
	 * <code><b><i>SL_DATE.Version</i></b></code> - Function that returns the version number for the SL_DATE module as a string
	 * @return the version number for the SL_DATE module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

}
