package sl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import resources.ApplicationProperties;

/**
 * The SL_STRING module contains string-handling routines.
 * 
 * @author Iulian Enache
 */
public class String implements IString {

	private static String instance;

	private java.lang.String error;

	private String() {
		super();
	}

	public static String getModule() {
		if (instance == null) {
			synchronized (String.class) {
				if (instance == null) {
					instance = new String();
				}
			}
		}
		return instance;
	}

	private java.lang.String convert(java.lang.String stringToConvert, java.lang.String in, java.lang.String out) {
		try {
			Charset charset_in = Charset.forName(out);
			Charset charset_out = Charset.forName(in);

			CharsetEncoder encoder = charset_in.newEncoder();
			CharsetDecoder decoder = charset_out.newDecoder();

			CharBuffer charBufferEncoding = CharBuffer.wrap(stringToConvert);

			ByteBuffer byteBuffer = encoder.encode(charBufferEncoding);

			CharBuffer charBufferDecoding = decoder.decode(byteBuffer);

			java.lang.String s = charBufferDecoding.toString();
			// System.out.println("Original String is: " + s);
			return s;
		} catch (CharacterCodingException e) {
			// System.out.println("Character Coding Error: " + e.getMessage());
			return "";
		}
	}

	@Override
	public Object asciitoebcdic(Object... args) {
		java.lang.String stringToConvert = (java.lang.String) args[0];
		return convert(stringToConvert, "ISO-8859-1", "IBM500");
	}

	@Override
	public Object ebcdictoascii(Object... args) {
		java.lang.String stringToConvert = (java.lang.String) args[0];
		return convert(stringToConvert, "IBM500", "ISO-8859-1");
	}

	@Override
	public Object fieldexplode(Object... args) {
		StringBuilder separators = new StringBuilder();
		separators.append("[");
		if (args.length <= 1) {
			separators.append(",");
		} else {
			separators.append(args[1]);
		}
		separators.append("]");
		java.lang.String patternForSplitting = separators.toString();

		java.lang.String[] result = new java.lang.String[0];
		if (args.length >= 1) {
			if (args[0] instanceof java.lang.String) {
				result = ((java.lang.String) args[0]).split(patternForSplitting);
			}
			if (args[0] instanceof char[]) {
				result = java.lang.String.valueOf(((char[]) args[0])).split(patternForSplitting);
			}
		}
		return result;
	}

	@Override
	public Object fieldcount(Object... args) {
		if ("".equals(args[0])) {
			return Long.valueOf(0L);
		}
		Long result = Long.valueOf(0L);
		java.lang.String[] fields = (java.lang.String[]) fieldexplode(args);
		result = Long.valueOf((long) fields.length);
		return result;
	}

	@Override
	public Object fieldextract(Object... args) {
		long fieldNumber = 0L;
		if (args[1] instanceof java.lang.String) {
			fieldNumber = Long.parseLong((java.lang.String) args[1]);
		}
		if (args[1] instanceof char[]) {
			fieldNumber = Long.parseLong(java.lang.String.valueOf((char[]) args[1]));
		}
		if (fieldNumber <= 0L) {
			error = error_fieldextract_illegalfieldnumber;
			return null;
		}
		if (fieldNumber > ((Long) fieldcount(args[0], args[2])).longValue()) {
			error = error_fieldextract_fieldnumberoutsidestring;
			return null;
		}
		java.lang.String[] fields = (java.lang.String[]) fieldexplode(args[0], args[2]);
		return fields[Long.valueOf(fieldNumber).intValue() - 1];
	}

	@Override
	public Object fieldimplode(Object... args) {
		java.lang.String separator;
		if (args.length <= 1) {
			separator = ",";
		} else {
			separator = (java.lang.String) args[1];
		}
		java.lang.String[] fields = (java.lang.String[]) args[0];
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < fields.length; i++) {
			if (i < fields.length - 1) {
				result.append(fields[i]);
				result.append(separator);
			} else {
				result.append(fields[i]);
			}
		}
		return result.toString();
	}

	@Override
	public Object reverse(Object... args) {
		StringBuilder result = new StringBuilder((java.lang.String) args[0]);
		return result.reverse().toString();
	}

	@Override
	public Object tolower(Object... args) {
		java.lang.String result = (java.lang.String) args[0];
		return result.toLowerCase();
	}

	@Override
	public Object toupper(Object... args) {
		java.lang.String result = (java.lang.String) args[0];
		return result.toUpperCase();
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_STRINGversion();
	}

}
