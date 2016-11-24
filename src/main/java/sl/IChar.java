package sl;

/**
 * The SL_CHAR module contains character classification routines.
 * 
 * @author Iulian Enache
 */
public interface IChar {

	/**
	 * <code><b><i>SL_CHAR.IsAlpha</i></b></code> - Function that checks if the first character in a character string is upper case or lower case letter
	 * @param args (character)
	 * 				</br>The <code><b><i>character</i></b></code> parameter is the character string. It should be a string.
	 * @return <code>nonzero</code> if the first character in the character string argument is a lower case or upper case letter.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object isalpha(Object... args);

	/**
	 * <code><b><i>SL_CHAR.IsAlphaUnderscore</i></b></code> - Function that checks if the first character in a character string is upper case letter, lower case letter or an underscore
	 * @param args (character)
	 * 				</br>The <code><b><i>character</i></b></code> parameter is the character string. It should be a string.
	 * @return <code>nonzero</code> if the first character in the character string argument is a lower case letter, an upper case letter or an underscore.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object isalphaunderscore(Object... args);

	/**
	 * <code><b><i>SL_CHAR.IsAlphaDigit</i></b></code> - Function that checks if the first character in a character string is upper case letter, lower case letter or a decimal digit
	 * @param args (character)
	 * 				</br>The <code><b><i>character</i></b></code> parameter is the character string. It should be a string.
	 * @return <code>nonzero</code> if the first character in the character string argument is a lower case letter, an upper case letter or a decimal digit.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object isalphadigit(Object... args);

	/**
	 * <code><b><i>SL_CHAR.IsAlphaDigitUnderscore</i></b></code> - Function that checks if the first character in a character string is upper case letter, lower case letter, a decimal digit or an underscore
	 * @param args (character)
	 * 				</br>The <code><b><i>character</i></b></code> parameter is the character string. It should be a string.
	 * @return <code>nonzero</code> if the first character in the character string argument is a lower case letter, an upper case letter, a decimal digit or an underscore.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object isalphadigitunderscore(Object... args);

	/**
	 * <code><b><i>SL_CHAR.IsDigit</i></b></code> - Function that checks if the first character in a character string is a decimal digit
	 * @param args (character)
	 * 				</br>The <code><b><i>character</i></b></code> parameter is the character string. It should be a string.
	 * @return <code>nonzero</code> if the first character in the character string argument is a decimal digit.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object isdigit(Object... args);

	/**
	 * <code><b><i>SL_CHAR.IsLower</i></b></code> - Function that checks if the first character in a character string is a lower case letter
	 * @param args (character)
	 * 				</br>The <code><b><i>character</i></b></code> parameter is the character string. It should be a string.
	 * @return <code>nonzero</code> if the first character in the character string argument is a lower case letter.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object islower(Object... args);

	/**
	 * <code><b><i>SL_CHAR.IsPrintable</i></b></code> - Function that checks if the first character in a character string is printable (space is a printable character)
	 * @param args (character)
	 * 				</br>The <code><b><i>character</i></b></code> parameter is the character string. It should be a string.
	 * @return <code>nonzero</code> if the first character in the character string argument is printable.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object isprintable(Object... args);

	/**
	 * <code><b><i>SL_CHAR.IsUpper</i></b></code> - Function that checks if the first character in a character string is an upper case letter
	 * @param args (character)
	 * 				</br>The <code><b><i>character</i></b></code> parameter is the character string. It should be a string.
	 * @return <code>nonzero</code> if the first character in the character string argument is an upper case letter.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object isupper(Object... args);

	/**
	 * <code><b><i>SL_CHAR.ClassCreate</i></b></code> - Function that creates and returns a new character class
	 * @param args (characters)
	 * 				</br>The <code><b><i>characters</i></b></code> parameter contains the characters that initially shall be included in the character class. It should be a string.
	 * @return a string representing the new character class.
	 */
	public abstract Object classcreate(Object... args);

	/**
	 * <code><b><i>SL_CHAR.ClassAdd</i></b></code> - Function that adds characters to an existing character class
	 * @param args (characters, class)
	 * 				</br>The <code><b><i>characters</i></b></code> parameter contains the characters to add. It should be a string.
	 * 				</br>The <code><b><i>class</i></b></code> parameter is the character class to add characters to.
	 * @return a string representing the modified character class.
	 */
	public abstract Object classadd(Object... args);

	/**
	 * <code><b><i>SL_CHAR.ClassRemove</i></b></code> - Function that removes characters from an existing character class
	 * @param args (characters, class)
	 * 				</br>The <code><b><i>characters</i></b></code> parameter contains the characters to remove. It should be a string.
	 * 				</br>The <code><b><i>class</i></b></code> parameter is the character class to remove characters from.
	 * @return a string representing the modified character class.
	 */
	public abstract Object classremove(Object... args);

	/**
	 * <code><b><i>SL_CHAR.IsInClass</i></b></code> - Function that checks if a character is included in a character class
	 * @param args (character, class)
	 * 				</br>The <code><b><i>character</i></b></code> parameter is the character string. It should be a string.
	 * 				</br>The <code><b><i>class</i></b></code> parameter is the character class to search into. It should be a string.
	 * 				</br>&emsp;<code>The <b><i>class</i></b> argument should previously have been returned from one of the <i>SL_CHAR.ClassCreate</i>, <i>SL_CHAR.ClassAdd</i>, or <i>SL_CHAR.ClassRemove</i> functions.</code>
	 * @return <code>nonzero</code> if the first character in the character string argument is included in the character class specified by <code><b><i>class</i></b></code>.
	 * 			</br>Otherwise, it returns <code>zero</code>.
	 */
	public abstract Object isinclass(Object... args);

	/**
	 * <code><b><i>SL_CHAR.Version</i></b></code> - Function that returns the version number for the SL_CHAR module as a string
	 * @return the version number for the SL_CHAR module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

}
