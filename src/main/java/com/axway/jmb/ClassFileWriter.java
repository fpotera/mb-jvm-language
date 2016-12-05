// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Java class file writer.
 *
 * @author Florin Potera
 */

public class ClassFileWriter {
	
	public static final String DEFAULT_BASE_DIR = "target/classes";
	
	private static String baseDir = DEFAULT_BASE_DIR;
	private String javaFullyQualifiedName;
	private OutputStream stream;
	
	public ClassFileWriter(String javaFullyQualifiedName) {
		super();
		this.javaFullyQualifiedName = javaFullyQualifiedName;
	}
	
	public void open () throws FileNotFoundException {
		String pakage = "";
		String className = javaFullyQualifiedName;
		if( javaFullyQualifiedName.contains(".") ) {
			pakage = javaFullyQualifiedName.substring(0, javaFullyQualifiedName.lastIndexOf("."));
			className = javaFullyQualifiedName.substring(javaFullyQualifiedName.lastIndexOf(".")+1);
		}
 	
        File pathToClassFile = new File ( baseDir + File.separator + pakage.replaceAll("\\.", File.separator) );
        pathToClassFile.mkdirs();
        pathToClassFile = new File ( pathToClassFile.getAbsolutePath() + File.separator + className + ".class");		
        stream = new FileOutputStream( pathToClassFile );   
	}
	
	public void write (byte[] clazz) throws IOException {
		stream.write(clazz);
	}
	
	public void close () throws IOException {
		stream.close();
	}

	public static void setBaseDir(String baseDir) {
		ClassFileWriter.baseDir = baseDir;
	}
	
}
