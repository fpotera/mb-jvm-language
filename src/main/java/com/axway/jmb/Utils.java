// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

import java.util.Arrays;

import org.objectweb.asm.Type;

/**
 * Utility methods.
 *
 * @author Florin Potera
 */

public class Utils {
	
	public static final String DOTREGEX = "\\.";
	
	public static String getPackage ( String pathToS4File ) {
		String[] pathElem = pathToS4File.split("/");
		String fileNameAndExtension;
		if ( pathElem.length != 0 ) {
			fileNameAndExtension = pathElem[pathElem.length-1];
		}
		else {
			fileNameAndExtension = pathToS4File;
		}		
		
		String[] pakage = fileNameAndExtension.split("_");
		pakage = Arrays.copyOfRange( pakage, 0, pakage.length-1);
		StringBuffer sb = new StringBuffer();
		for ( String dir : pakage ) {
			if( sb.length() != 0 ) {
				sb.append(".");
			}
			sb.append(dir.toLowerCase());
		}
		
		return sb.toString();
	}
	
	public static String getClass ( String pathToS4File ) {
		return getJavaSimpleClassName( getFileName( pathToS4File ) );
	}
	
	public static String getFileName ( String pathToS4File ) {
		String moduleNameFullyQualified = getModuleName( pathToS4File );
		if ( moduleNameFullyQualified.contains("_") ) {
			moduleNameFullyQualified = moduleNameFullyQualified.substring(moduleNameFullyQualified.lastIndexOf("_")+1);
		}
		return moduleNameFullyQualified;
	}
	
	public static String getModuleName ( String pathToS4File ) {
		String[] pathElem = pathToS4File.split("/");
		String fileNameAndExtension;
		if ( pathElem.length != 0 ) {
			fileNameAndExtension = pathElem[pathElem.length-1];
		}
		else {
			fileNameAndExtension = pathToS4File;
		}
		
		int extensionIndex = fileNameAndExtension.lastIndexOf(".");
		extensionIndex = extensionIndex != -1 ? extensionIndex : fileNameAndExtension.length();
		
		String fullyQualifiedModuleName = fileNameAndExtension.substring( 0, extensionIndex );
	
		return fullyQualifiedModuleName;
	}	
	
	public static boolean isModuleInterface ( String fileName ) {
		return fileName.endsWith(".s4h");
	}
	
	public static boolean isModuleImplementation ( String fileName ) {
		return fileName.endsWith(".s4m");
	}
	
	public static boolean isExecutableModule ( String fileName ) {
		return fileName.endsWith(".s4");
	}
	
	public static String getJavaRecordTypeName ( String messageBuilderRecordName ) {
		return "R"+messageBuilderRecordName.toLowerCase();
	}
	
	public static Type getJavaRecordType ( String recordTypeNameFullyQualifiedFromMessageBuilder ) {
		if ( !recordTypeNameFullyQualifiedFromMessageBuilder.contains(".") ) {
			// is internal defined - but it must be fully qualified here
			return null;
		}

		String[] identifiers = recordTypeNameFullyQualifiedFromMessageBuilder.split( DOTREGEX );
		
		String className = getJavaFullyQualifiedClassName( identifiers[0] );
		String recordName = getJavaRecordTypeName( identifiers[1] );
		String recordNameFullyQualified = className + "$" + recordName;
		
		return Type.getType( recordNameFullyQualified.replace(".", "/") );
	}
	
	public static String getJavaFullyQualifiedInternalClassName ( String moduleNameOfMessageBuilder ) {
		return  getJavaFullyQualifiedClassName( moduleNameOfMessageBuilder ).replace(".", "/");
	}
	
	public static String getJavaFullyQualifiedClassName ( String moduleNameOfMessageBuilder ) {
		String packageName = "";
		if ( moduleNameOfMessageBuilder.contains("_") ) {
			// is fully qualified
			packageName = moduleNameOfMessageBuilder.substring(0, moduleNameOfMessageBuilder.lastIndexOf("_")+1);
			packageName = packageName.replace("_", ".").toLowerCase();
		}
		return packageName + getJavaSimpleClassName( moduleNameOfMessageBuilder );
	}
	
	
	public static String getJavaSimpleClassName ( String moduleNameOfMessageBuilder ) {
		String className = moduleNameOfMessageBuilder;
		if ( moduleNameOfMessageBuilder.contains("_") ) {
			// is fully qualified
			className = moduleNameOfMessageBuilder.substring( moduleNameOfMessageBuilder.lastIndexOf("_")+1);
		}

		return className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase();
	}
	
	public static String getJavaMethodName ( String messageBuilderStatementName ) {
		return messageBuilderStatementName.toLowerCase();
	}
	
	public static String getInternalFQClassName ( String fullyQualifiedClassName ) {
		return fullyQualifiedClassName.replace(".", "/");
	}
}
