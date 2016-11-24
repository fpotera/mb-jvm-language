package sl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
		java.util.Date date = null;
		try {
			SimpleDateFormat parameterDateFormat = new SimpleDateFormat(format);
			date = parameterDateFormat.parse(stringToConvert);
		} catch (ParseException e) {
			System.out.println(error_stringtodate_convert);
			e.printStackTrace();
		}
		return date;
//		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
//		return dateFormat.format(date);
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_DATEversion();
	}

}
