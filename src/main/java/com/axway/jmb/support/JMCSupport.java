package com.axway.jmb.support;

public class JMCSupport {
	public static char[] createFixedString ( int length ) {
		return new String ( new char[length] ).replace("\0", " ").toCharArray();
	}
	
	public static char[] createFixedString ( int length, Object ... objs ) {
		String str = "";
		for ( Object obj : objs ) {
			String crtStr;
			if ( ! ( obj instanceof String ) ) {
				crtStr = obj.toString();
			}
			else {
				crtStr = (String) obj;
			}
			if ( str.length()+crtStr.length() <= length  ) {
				str += crtStr;
			}
			else {
				str += crtStr.substring(0, length-str.length());
				break;
			}
		}
		
		if ( str.length() < length ) {
			str = str + new String ( createFixedString( length - str.length() ) );
		}
		return str.toCharArray();
	}
	
	
}
