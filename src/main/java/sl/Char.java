package sl;

import resources.ApplicationProperties;

/**
 * The SL_CHAR module contains character classification routines.
 * 
 * @author Iulian Enache
 */
public class Char implements IChar {

	private static Char instance;

	private Char() {
		super();
	}

	public static Char getModule() {
		if (instance == null) {
			synchronized (Char.class) {
				if (instance == null) {
					instance = new Char();
				}
			}
		}
		return instance;
	}

	@Override
	public Object isalpha(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		if (Character.isLetter(character.charAt(0))) {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object isalphaunderscore(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		if (Character.isLetter(character.charAt(0)) || character.charAt(0) == '_') {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object isalphadigit(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		if (Character.isLetterOrDigit(character.charAt(0))) {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object isalphadigitunderscore(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		if (Character.isLetterOrDigit(character.charAt(0)) || character.charAt(0) == '_') {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object isdigit(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		if (Character.isDigit(character.charAt(0))) {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object islower(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		if (Character.isLetter(character.charAt(0)) && Character.isLowerCase(character.charAt(0))) {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object isprintable(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		int codePoint = character.codePointAt(0);
		switch (Character.getType(codePoint)) {
			// case Character.COMBINING_SPACING_MARK:
			// case Character.CONNECTOR_PUNCTUATION:
			case Character.CONTROL:
				return Long.valueOf(0L);
			// case Character.CURRENCY_SYMBOL:
			// case Character.DASH_PUNCTUATION:
			// case Character.DECIMAL_DIGIT_NUMBER:
			// case Character.ENCLOSING_MARK:
			// case Character.END_PUNCTUATION:
			// case Character.FINAL_QUOTE_PUNCTUATION:
			case Character.FORMAT:
				return Long.valueOf(0L);
			// case Character.INITIAL_QUOTE_PUNCTUATION:
			// case Character.LETTER_NUMBER:
			case Character.LINE_SEPARATOR:
				return Long.valueOf(0L);
			// case Character.LOWERCASE_LETTER:
			// case Character.MATH_SYMBOL:
			// case Character.MODIFIER_LETTER:
			// case Character.MODIFIER_SYMBOL:
			// case Character.NON_SPACING_MARK:
			// case Character.OTHER_LETTER:
			// case Character.OTHER_NUMBER:
			// case Character.OTHER_PUNCTUATION:
			// case Character.OTHER_SYMBOL:
			case Character.PARAGRAPH_SEPARATOR:
				return Long.valueOf(0L);
			case Character.PRIVATE_USE:
				return Long.valueOf(0L);
			// case Character.SPACE_SEPARATOR:
			// case Character.START_PUNCTUATION:
			case Character.SURROGATE:
				return Long.valueOf(0L);
			// case Character.TITLECASE_LETTER:
			case Character.UNASSIGNED:
				return Long.valueOf(0L);
			// case Character.UPPERCASE_LETTER:
			default:
				return Long.valueOf(1L);
		}
	}

	@Override
	public Object isupper(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		if (Character.isLetter(character.charAt(0)) && Character.isUpperCase(character.charAt(0))) {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object classcreate(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		return character;
	}

	@Override
	public Object classadd(Object... args) {
		java.lang.String characters = (java.lang.String) args[0];
		StringBuilder classToAddCharactersTo = new StringBuilder((java.lang.String) args[1]);
		return classToAddCharactersTo.append(characters).toString();
	}

	@Override
	public Object classremove(Object... args) {
		StringBuilder patternForSplitting = new StringBuilder();
		patternForSplitting.append("[");
		patternForSplitting.append((java.lang.String) args[0]);
		patternForSplitting.append("]");
		java.lang.String characters = patternForSplitting.toString();
		java.lang.String classToRemoveCharactersFrom = (java.lang.String) args[1];
		return classToRemoveCharactersFrom.replaceAll(characters, "");
	}

	@Override
	public Object isinclass(Object... args) {
		java.lang.String character = (java.lang.String) args[0];
		java.lang.String classToSearchCharactersInto = (java.lang.String) args[1];
		if (classToSearchCharactersInto.contains(Character.toString(character.charAt(0)))) {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_CHARversion();
	}

}
