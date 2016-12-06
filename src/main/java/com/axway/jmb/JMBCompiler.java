// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.

package com.axway.jmb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Compiler startup class.
 *
 * @author Florin Potera
 */

public class JMBCompiler {
	
    private ANTLRInputStream input;
    private JMessageBuilderLexer lexer;
    private CommonTokenStream tokens;
    private JMessageBuilderParser parser; 
    private JMessageBuilderVisitorImpl visitor;
	
	public JMBCompiler(InputStream is) throws IOException {
        input = new ANTLRInputStream(is);
        lexer = new JMessageBuilderLexer(input);
        tokens = new CommonTokenStream(lexer);
        parser = new JMessageBuilderParser(tokens);		
	}
	
	public ParseTree parse() {
		return parser.compilationUnit();
	}
	
	public void visit(ParseTree tree) {
		visitor.visit(tree);
	}
	
	public byte[] buildClass() {
		return visitor.getClassBytes();
	}
	
	public void setVisitor(JMessageBuilderVisitorImpl visitor) {
		this.visitor = visitor;
	}
	
	public static void main (String[] args) throws IOException {
		String destDir = ClassFileWriter.DEFAULT_BASE_DIR;
		String inputFile = null;
		for ( String arg : args ) {
			if ( arg.startsWith("-d:") ) {
				destDir = arg.substring(3);
			}
			else if ( arg.endsWith(".s4h") || arg.endsWith(".s4m") || arg.endsWith(".s4") ) {
				inputFile = arg;
			}
		}
		ClassFileWriter.setBaseDir(destDir);
		
        InputStream is = null;
        if ( inputFile!=null ) {
        	try {
        		is = new FileInputStream( inputFile );
        	}
        	catch ( FileNotFoundException ex ) {
            	ClassLoader classLoader = JMBCompiler.class.getClassLoader();
            	is = classLoader.getResourceAsStream( inputFile );        		
        	}
        }
        else {
        	System.err.println("No input file to compile.");
        	System.exit(1);
        }
        if ( is == null ) {
        	System.err.println("Input file to compile not found.");
        	System.exit(1);        	
        }

        String pakage = Utils.getPackage( inputFile );
        String fileName = ( Utils.isModuleInterface( inputFile ) ? "I" : "" ) + Utils.getClass( inputFile );
        String executableModuleName = Utils.getModuleName( inputFile );
        
        JMBCompiler compiler = new JMBCompiler(is);        
        ParseTree tree = compiler.parse();
        compiler.setVisitor( new JMessageBuilderVisitorImpl( Utils.isExecutableModule(inputFile) ? executableModuleName : null ) );
        compiler.visit(tree);       
        byte[] clazz = compiler.buildClass();
 
        String javaFullyQualifiedClassName = pakage+"."+fileName;
        
        ClassFileWriter cw = new  ClassFileWriter( javaFullyQualifiedClassName);   
        cw.open();
        cw.write(clazz);
        cw.close();
        
        System.out.println("Compiled successfuly.");
	}
	
	

}

