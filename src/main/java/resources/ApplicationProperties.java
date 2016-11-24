package resources;

import java.util.ResourceBundle;
import java.lang.String;

/**
 * Contains methods to read properties from the "application.properties" file.
 * 
 * @author Iulian Enache
 */
public class ApplicationProperties {

	public static final String RESOURCES = ApplicationProperties.class.getPackage().getName() + ".application";

	private static ResourceBundle resources;

	private static ResourceBundle getResources() {
		if (resources == null)
			try {
				resources = ResourceBundle.getBundle(RESOURCES);
			} catch (Exception e) {
				throw new InternalError(e.getMessage());
			}
		return resources;
	}

	private static String getResource(String key) {
		return ApplicationProperties.getResources().getString(key);
	}

	/**
	 * The version number of the SL_STRING module
	 */
	public static String getSL_STRINGversion() {
		String sl_string_Version = ApplicationProperties.getResource("sl_string_version");
		return sl_string_Version;
	}

	/**
	 * The version number of the SL_ARGUMENT module
	 */
	public static String getSL_ARGUMENTversion() {
		String sl_argument_Version = ApplicationProperties.getResource("sl_argument_version");
		return sl_argument_Version;
	}

	/**
	 * The version number of the SL_CHAR module
	 */
	public static String getSL_CHARversion() {
		String sl_char_Version = ApplicationProperties.getResource("sl_char_version");
		return sl_char_Version;
	}

	/**
	 * The version number of the SL_DATE module
	 */
	public static String getSL_DATEversion() {
		String sl_date_Version = ApplicationProperties.getResource("sl_date_version");
		return sl_date_Version;
	}

	/**
	 * The version number of the SL_ARRAY module
	 */
	public static String getSL_ARRAYversion() {
		String sl_array_Version = ApplicationProperties.getResource("sl_array_version");
		return sl_array_Version;
	}

}
