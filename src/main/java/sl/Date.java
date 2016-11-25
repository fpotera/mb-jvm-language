package sl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import resources.ApplicationProperties;

/**
 * The SL_DATE module contains date handling routines.
 * 
 * @author Iulian Enache
 */
public class Date implements IDate {

	private static Date instance;

	private Date() {
		super();
	}

	public static Date getModule() {
		if (instance == null) {
			synchronized (Date.class) {
				if (instance == null) {
					instance = new Date();
				}
			}
		}
		return instance;
	}

	@Override
	public Object stringtodate(Object... args) {
		java.lang.String stringToConvert = (java.lang.String) args[0];
		java.lang.String format = (java.lang.String) args[1];

		java.lang.String convertedFormat = this.convertDateFormat_MessageBuilder_to_Java(format);
		java.util.Date date = null;
		try {
			SimpleDateFormat parameterDateFormat = new SimpleDateFormat(convertedFormat);
			date = parameterDateFormat.parse(stringToConvert);
		} catch (ParseException e) {
			System.out.println(error_stringtodate_convert);
			e.printStackTrace();
		}
		return date;
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_DATEversion();
	}

	private java.lang.String convertDateFormat_MessageBuilder_to_Java(java.lang.String formatMessageBuilder) {
		StringBuilder result = new StringBuilder();
		int indexStart = 0;
		java.lang.String pattern = "%[aAbBdefHIjmMpSUwWyYst]";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(formatMessageBuilder);
		while (m.find()) {
			result.append(formatMessageBuilder.substring(indexStart, m.start()));
			result.append(this.transformSequence(m.group()));
			indexStart = m.start() + 2;
		}
		return result.toString();
	}

	private static java.lang.String transformSequence(java.lang.String value) {
		java.lang.String sequence = null;
		switch (value) {
		case "%%":
			// The % character.
			sequence = "%";
			break;
		case "%a":
			// Abbreviated weekday name (Sun, Mon, Tue, Wed, Thu, Fri or Sat). When converting from a string to a date value, the case does not matter.
			sequence = "E";
			break;
		case "%A":
			// Full weekday name (Sunday, Monday, Tuesday, Wednesday, Thursday, Friday or Saturday). When converting from a string to a date value, the case does not matter.
			sequence = "E";
			break;
		case "%b":
			// Abbreviated month name (Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov or Dec). When converting from a string to a date value, the case does not matter.
			sequence = "MMM";
			break;
		case "%B":
			// Full month name (January, February, March, April, May, June, July, August, September, October, November or December). When converting from a string to a date value, the case does not matter.
			sequence = "MMM";
			break;
		case "%d":
			// Day of month represented as two digits (01 31).
			sequence = "d";
			break;
		case "%e":
			// Day of month represented as one space and one digit, or, two digits (" 1" "31").
			sequence = "d";
			break;
		case "%f":
			// Fractions of a second represented as two digits.
			sequence = "S";
			break;
		case "%H":
			// Hour represented as two digits (00 23).
			sequence = "H";
			break;
		case "%I":
			// Hour represented as two digits (00 11).
			sequence = "K";
			break;
		case "%j":
			// Day number of year represented as three digits (001 366).
			sequence = "D";
			break;
		case "%m":
			// Month number represented as two digits (01 12).
			sequence = "M";
			break;
		case "%M":
			// Minute represented as two digits (00 59)
			sequence = "m";
			break;
		case "%p":
			// Ante meridian or post meridian (AM or PM). When converting from a string to a date value, the case does not matter.
			sequence = "a";
			break;
		case "%S":
			// Seconds represented as two digits (00 59).
			sequence = "s";
			break;
		case "%U":
			// Week number of the year represented as two digits (01 53). Sunday is the first day of the week.
			sequence = "w";
			break;
		case "%w":
			// Weekday number represented as one digit (0 6). Sunday is weekday number 0.
			sequence = "u";
			break;
		case "%W":
			// Week number of the year represented as two digits (01 53). Monday is the first day of the week.
			sequence = "w";
			break;
		case "%y":
			// Year within the 20th century represented as two digits (00 99), that is, the two-digit year yy is interpreted as 19yy. Due to backward compatibility reasons the interpretation of two-digit years cannot be changed. It is recommended that two-digit years are avoided.
			sequence = "y";
			break;
		case "%Y":
			// Year including century represented as four digits, for example, 2001. The year must be 1900 or later.
			sequence = "y";
			break;
		case "%s":
			// Optional seconds represented as two digits (00 59).
			sequence = "s";
			break;
		case "%t":
			// Optional fractions of a second represented as one or more digits. Two fractional digits are significant.
			sequence = "S";
			break;
		default:
			break;
		}
		return sequence;
	}

}
