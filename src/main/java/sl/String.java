package sl;

public class String implements IString {
	
	private static String instance;
	
	public Object fieldexplode (Object ... args) {
		java.lang.String[] result = new java.lang.String[0];
		java.lang.String separator = ",";
		if ( args.length > 1 ) {
			if( args[1] instanceof java.lang.String ) {
				separator = (java.lang.String) args[1];
			}
		}
		if ( args.length >= 1 ) {
			if( args[0] instanceof java.lang.String ) {
				result = ( (java.lang.String) args[0] ).split( separator );
			}
			if( args[0] instanceof char[] ) {
				result = ( new java.lang.String( (char[]) args[0] ) ).split( separator );
			}
		}		
		return result;
	}
	
	public static String getModule () {
		if ( instance == null ) {
			instance = new String();
		}
		return instance;
	}
}
