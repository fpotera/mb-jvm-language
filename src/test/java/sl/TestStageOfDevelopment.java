package sl;

import java.nio.charset.Charset;
import java.util.Arrays;

import sl.Argument;
import sl.Array;
import sl.Char;
import sl.Date;
import sl.IArgument;
import sl.IArray;
import sl.IChar;
import sl.IDate;
import sl.String;

public class TestStageOfDevelopment {

//	private static final java.lang.String[] FUNCTION_ARGUMENTS = { "field A:field B:field C:field D", ":" };
//	private static final java.lang.String[] FUNCTION_ARGUMENTS = { "field A:field B,field C:field D", ":," };
//	private static final java.lang.String[] FUNCTION_ARGUMENTS = { "field A:field B,field C:field D", "3", ":," };
//	private static final java.lang.Object[] FUNCTION_ARGUMENTS = { new java.lang.String[] { "field A", "field B", "field C", "field D" }, ":" };

	public static void main(java.lang.String[] args) {
//		test_SL_STRING_Module();
//		test_SL_ARGUMENT_Module();
//		test_SL_CHAR_Module();
//		test_SL_DATE_Module();
		test_SL_ARRAY_Module();
	}
	
	public static void test_SL_STRING_Module() {
		String testString = String.getModule();

//		int numberOfFields = ((Long) testString.fieldcount(FUNCTION_ARGUMENTS[0], FUNCTION_ARGUMENTS[2])).intValue();
//		int numberOfFields = ((Long) testString.fieldcount(FUNCTION_ARGUMENTS)).intValue();
		int numberOfFields = ((Long) testString.fieldcount("field A:field B:field C:field D", ":")).intValue();
		System.out.println("Number of fields = " + numberOfFields);

//		java.lang.String[] fields = (java.lang.String[]) testString.fieldexplode(FUNCTION_ARGUMENTS[0], FUNCTION_ARGUMENTS[2]);
//		java.lang.String[] fields = (java.lang.String[]) testString.fieldexplode(FUNCTION_ARGUMENTS);
		java.lang.String[] fields = (java.lang.String[]) testString.fieldexplode("field A:field B,field C:field D", ":,");
		System.out.println("Fields : " + Arrays.asList(fields));

//		java.lang.String field = (java.lang.String) testString.fieldextract(FUNCTION_ARGUMENTS[0], "3", FUNCTION_ARGUMENTS[1]);
//		java.lang.String field = (java.lang.String) testString.fieldextract(FUNCTION_ARGUMENTS);
		java.lang.String field = (java.lang.String) testString.fieldextract("field A:field B,field C:field D", "3", ":,");
		System.out.println("Field : " + field);

//		java.lang.String elements = (java.lang.String) testString.fieldimplode(FUNCTION_ARGUMENTS);
		java.lang.String elements = (java.lang.String) testString.fieldimplode(new java.lang.String[] { "field A", "field B", "field C", "field D" }, ":");
		System.out.println("Elements : " + elements);

		java.lang.String reversedString = (java.lang.String) testString.reverse("reverse it")[0];
		System.out.println("Reversed String : " + reversedString);

		java.lang.String convertedStringToLower = (java.lang.String) testString.tolower("STANDARD LIBRARY");
		System.out.println("converted String toLower : " + convertedStringToLower);

		java.lang.String convertedStringToUpper = (java.lang.String) testString.toupper("standard library");
		System.out.println("converted String toUpper : " + convertedStringToUpper);

		java.lang.String version = (java.lang.String) testString.version();
		System.out.println("Version : " + version);

		System.out.println("====================================================================================================");
		System.out.println(Charset.availableCharsets().keySet());
		System.out.println(Charset.forName("IBM500").aliases());

		java.lang.String asciitoebcdic = (java.lang.String) testString.asciitoebcdic("ASCII to EBCDIC");
		System.out.println("converted String - EBCDIC (IBM500) : " + asciitoebcdic);

		java.lang.String ebcdictoascii = (java.lang.String) testString.asciitoebcdic("EBCDIC to ASCII");
		System.out.println("converted String - ASCII (ISO-8859-1) : " + ebcdictoascii);
		System.out.println("====================================================================================================");
	}

	public static void test_SL_ARGUMENT_Module() {
		IArgument testArgument = Argument.getModule();
		java.lang.String version = (java.lang.String) testArgument.version();
		System.out.println("Version : " + version);
	}

	public static void test_SL_CHAR_Module() {
		IChar testChar = Char.getModule();

		Long characterLong = (Long) testChar.isalpha("4Ab!_\n");
		System.out.println("SL_CHAR.IsAlpha(4Ab!_\\n) : " + characterLong);

		characterLong = (Long) testChar.isalphaunderscore("_4Ab!_\n");
		System.out.println("SL_CHAR.IsAlphaUnderscore(_4Ab!_\\n) : " + characterLong);

		characterLong = (Long) testChar.isalphadigit("4Ab!_\n");
		System.out.println("SL_CHAR.IsAlphaDigit(4Ab!_\\n) : " + characterLong);

		characterLong = (Long) testChar.isalphadigitunderscore("_4Ab!_\n");
		System.out.println("SL_CHAR.IsAlphaDigitUnderscore(_4Ab!_\\n) : " + characterLong);

		characterLong = (Long) testChar.isdigit("4Ab!_\n");
		System.out.println("SL_CHAR.IsDigit(4Ab!_\\n) : " + characterLong);

		characterLong = (Long) testChar.islower("4Ab!_\n");
		System.out.println("SL_CHAR.IsLower(4Ab!_\\n) : " + characterLong);

		characterLong = (Long) testChar.isupper("Ab!_\n");
		System.out.println("SL_CHAR.IsUpper(Ab!_\\n) : " + characterLong);

		char[] charLowerCaseArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
				'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '\u00DF', '\u00E0', '\u00E1', '\u00E2', '\u00E3',
				'\u00E4', '\u00E5', '\u00E6', '\u00E7', '\u00E8', '\u00E9', '\u00EA', '\u00EB', '\u00EC', '\u00ED',
				'\u00EE', '\u00EF', '\u00F0', '\u00F1', '\u00F2', '\u00F3', '\u00F4', '\u00F5', '\u00F6', '\u00F8',
				'\u00F9', '\u00FA', '\u00FB', '\u00FC', '\u00FD', '\u00FE', '\u00FF' };

		System.out.print("Lower Case letters : ");
		for (char c : charLowerCaseArray) {
			System.out.print(c + " , ");
		}

		char[] charUpperCaseArray = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
				'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '\u00C0', '\u00C1', '\u00C2', '\u00C3', '\u00C4',
				'\u00C5', '\u00C6', '\u00C7', '\u00C8', '\u00C9', '\u00CA', '\u00CB', '\u00CC', '\u00CD', '\u00CE',
				'\u00CF', '\u00D0', '\u00D1', '\u00D2', '\u00D3', '\u00D4', '\u00D5', '\u00D6', '\u00D8', '\u00D9',
				'\u00DA', '\u00DB', '\u00DC', '\u00DD', '\u00DE' };

		System.out.print("\nUpper Case letters : ");
		for (char c : charUpperCaseArray) {
			System.out.print(c + " , ");
		}
		System.out.print("\n");

		characterLong = (Long) testChar.isprintable(" Ab!_\n");
		System.out.println("SL_CHAR.IsPrintable( Ab!_\\n) : " + characterLong);

		java.lang.String characterString = (java.lang.String) testChar.classcreate("4!");
		System.out.println("SL_CHAR.ClassCreate(\"4!\") : " + characterString);

		characterString = (java.lang.String) testChar.classadd("Ab", "4!");
		System.out.println("SL_CHAR.ClassAdd(\"Ab\", \"4!\") : " + characterString);

		characterString = (java.lang.String) testChar.classremove("A!", "4Ab!\t_");
		System.out.println("SL_CHAR.ClassRemove(\"A!\", \"4Ab!\\t_\") : " + characterString);

		characterLong = (Long) testChar.isinclass("\tBA!", "4Ab!\t_");
		System.out.println("SL_CHAR.IsInClass(\"\\tBA!\", \"4Ab!\\t_\") : " + characterLong);

		java.lang.String version = (java.lang.String) testChar.version();
		System.out.println("SL_CHAR.Version() : " + version);
	}

	public static void test_SL_DATE_Module() {
		IDate testDate = Date.getModule();

		java.lang.String stringToConvert = "2016.11.25 11.30 PM";
		java.lang.String format = "%Y.%m.%d %I.%M %p";
//		java.lang.String format = "%Y.%m.%d";
		java.util.Date stringDate = (java.util.Date) testDate.stringtodate(stringToConvert, format);
		System.out.println("SL_DATE.StringToDate(\"" + stringToConvert + "\", \"" + format + "\") : " + stringDate);

		java.lang.String version = (java.lang.String) testDate.version();
		System.out.println("SL_DATE.Version() : " + version);
	}

	public static void test_SL_ARRAY_Module() {
		IArray testArray = Array.getModule();

		/*int[] array = { 1, 2, 3, 4 };
		int position = 4;
		int value = 5;*/
		/*Long[] array = { 1L, 2L, 3L, 4L };
		Integer position = Integer.valueOf(-4);
		Long value = 5L;*/
		Double[] array = { 1D, 2D, 3D, 4D };
//		int position = 7;
		int position = 3;
//		java.lang.String value = "un text";
//		Long value = 5L;
		Double value = 5D;
		Object[] result = (Object[]) testArray.insert(array, position, value);
		for (int i = 0; i < result.length; i++) {
			if (i < result.length - 1) {
				System.out.print(result[i] + ", ");
			} else {
				System.out.print(result[i]);
			}
		}
		System.out.println();
		
		Long[] arrayAppend = { 1L, 2L, 3L, 4L };
		Long valueAppend = 5L;
		result = (Object[]) testArray.append(arrayAppend, valueAppend);
		for (int i = 0; i < result.length; i++) {
			if (i < result.length - 1) {
				System.out.print(result[i] + ", ");
			} else {
				System.out.print(result[i]);
			}
		}
		System.out.println();
		
		Integer[] arrayDelete = { 1, 2, 3, 4, 5 };
		result = (Object[]) testArray.delete(arrayDelete, 2);
		for (int i = 0; i < result.length; i++) {
			if (i < result.length - 1) {
				System.out.print(result[i] + ", ");
			} else {
				System.out.print(result[i]);
			}
		}
		System.out.println();

		java.lang.String version = (java.lang.String) testArray.version();
		System.out.println("SL_ARRAY.Version() : " + version);
		
	}

}
