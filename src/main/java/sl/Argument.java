package sl;

/**
 * The SL_ARGUMENT module contains routines for handling command-line arguments.
 * 
 * @author Iulian Enache
 */
import resources.ApplicationProperties;

public class Argument implements IArgument {

	private static Argument instance;

	private Argument() {
		super();
	}

	public static Argument getModule() {
		if (instance == null) {
			synchronized (Argument.class) {
				if (instance == null) {
					instance = new Argument();
				}
			}
		}
		return instance;
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_ARGUMENTversion();
	}

}
