// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb.support;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Java methods used as support of JMB compiler at execution of generated code.
 *
 * @author Florin Potera
 */

public class JMBCSupport {
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
	
	public static String formatDateDefault ( Date date ) {
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		String str = format.format(date);
		return str;
	}
}
