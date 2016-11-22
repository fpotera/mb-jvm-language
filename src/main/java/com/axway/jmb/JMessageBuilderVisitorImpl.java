// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.

package com.axway.jmb;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.V1_7;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import com.axway.jmb.JMessageBuilderParser.AdhocModuleBodyDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.BuiltinFunctionCallContext;
import com.axway.jmb.JMessageBuilderParser.CompilationUnitContext;
import com.axway.jmb.JMessageBuilderParser.ConcatStringsContext;
import com.axway.jmb.JMessageBuilderParser.FieldDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.FloatingPointLiteralContext;
import com.axway.jmb.JMessageBuilderParser.IntegerLiteralContext;
import com.axway.jmb.JMessageBuilderParser.ModuleDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.ModuleIdentifierContext;
import com.axway.jmb.JMessageBuilderParser.PrimaryContext;
import com.axway.jmb.JMessageBuilderParser.PrintStatementContext;
import com.axway.jmb.JMessageBuilderParser.ProcedureDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.RecordFieldContext;
import com.axway.jmb.JMessageBuilderParser.RecordTypeDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.SingleTypeIncludeDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.StringLiteralContext;
import com.axway.jmb.JMessageBuilderParser.UnannTypeContext;
import com.axway.jmb.builders.Methods;
import com.axway.jmb.builtin.Builtin;

/**
 * Visitor for Message Builder source files.
 *
 * @author Florin Potera
 */

public class JMessageBuilderVisitorImpl extends JMessageBuilderBaseVisitor<Void> {
	private ModuleBuilder currentModule;
	private MethodBuilder currentMethod;
	private ClassWriter currentInnerClass;
	private ClassWriter currentClassWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
	
	private boolean isInterface = false;	
	private String executableModuleName;
		
	private boolean classCreated;
	
	private Map<String, String> includeTypes = new HashMap<String, String>();
	
	public JMessageBuilderVisitorImpl ( String executableModuleName ) {
		this.executableModuleName = executableModuleName;
	}
	
	public byte[] getClassBytes() {
		return currentClassWriter.toByteArray();
	}
	
	@Override
	public Void visitCompilationUnit(CompilationUnitContext ctx) {
		debug("visitCompilationUnit()");
		super.visitCompilationUnit(ctx);
		
		if ( currentMethod != null ) {
			currentMethod.returnValue();
			currentMethod.visitMaxs(10, 5);
//			currentMethod.endMethod();			
			currentMethod.visitEnd();	
		}
		if ( currentModule != null ) {
			currentModule.visitEnded();
		}
		
		return null;
	}
		
	@Override
	public Void visitSingleTypeIncludeDeclaration(SingleTypeIncludeDeclarationContext ctx) {
		super.visitSingleTypeIncludeDeclaration(ctx);
		
		String moduleName = ctx.typeName().Identifier().getText().toLowerCase(); 
		String javaModule = Utils.getPackage( moduleName ) + "." + Utils.getClass( moduleName );		
		includeTypes.put( moduleName , javaModule );
		return null;
	}

	@Override
	public Void visitModuleDeclaration(ModuleDeclarationContext ctx) {
		if ( ctx.INTERFACE() != null )
			isInterface = true;		
		return super.visitModuleDeclaration(ctx);
	}

	///////////////////////////////////////////////////////////////////////////////
	//////   EXECUTABLE MODULE DECLARATION
	
	@Override
	public Void visitAdhocModuleBodyDeclaration(AdhocModuleBodyDeclarationContext ctx) {		
		if ( !classCreated ) {
			debug("Generate executable module:"+executableModuleName);
	
			currentModule = new ExecutableModuleBuilder(0, executableModuleName, currentClassWriter);
			
			currentMethod = currentModule.getMainMethod();
			
			classCreated = true;
		}

		return super.visitAdhocModuleBodyDeclaration(ctx);
	}

	///////////////////////////////////////////////////////////////////////////////
	//////   SIMPLE MODULE DECLARATION	
	
	@Override
	public Void visitModuleIdentifier(ModuleIdentifierContext ctx) {
		
        String pakage = Utils.getPackage( ctx.getText() );
        String clazz = ( isInterface ? "I" : "" ) + Utils.getClass( ctx.getText() ); 
        String classFullQualifiedName = pakage + "." + clazz;
        String interfaceFullQualifiedName = pakage + "." + "I" + clazz;
		
		int generate = isInterface ? ACC_INTERFACE : 0 ;
		String[] interfaces = isInterface ? null : new String[] { interfaceFullQualifiedName.replace(".", "/") };
		
		currentModule.visit(V1_7, ACC_PUBLIC + generate,
				classFullQualifiedName.replace(".", "/"), null, Type.getType(Object.class).getInternalName(), interfaces);
		
		debug("Generate class:"+classFullQualifiedName);
		
//		if ( !isInterface ) {
//			JMBModule mb = new JMBModule(mainClass, classFullQualifiedName);
//			mb.addBaseCode();
//		}
		
		return super.visitModuleIdentifier(ctx);
	}

	///////////////////////////////////////////////////////////////////////////////
	//////   FIELD/VARIABLE DECLARATION
	
	@Override
	public Void visitFieldDeclaration(FieldDeclarationContext ctx) {
		LocalVariable newVariable = new LocalVariable();
		
		super.visitFieldDeclaration(ctx);
		
		debug(" visitFieldDeclaration() "+ctx.variableDeclarator().variableDeclaratorId().variableIdentifier().getText());
		
		newVariable.setName( convertVariableName ( ctx.variableDeclarator().variableDeclaratorId().variableIdentifier().getText() ) );
		if ( ctx.variableDeclarator().variableDeclaratorId().dims() != null ) {
			newVariable.setArrayDimension( ctx.variableDeclarator().variableDeclaratorId().dims().Dim().size() );
		}
		if ( ctx.variableDeclarator().ASSIGN() != null ) {
			newVariable.setInitializationAvailable(true);
		}
		
		if ( ctx.unannType().unannRecordType() != null ) {
			newVariable.setType( JMBVariableType.RECORD );
			newVariable.setRecordType(Utils.getJavaRecordType( ctx.unannType().unannRecordType().expressionName().getText() )); 
			debug("rec type: "+ctx.unannType().unannRecordType().expressionName().getText());
		}
		else {
			if ( ctx.unannType().unannPrimitiveType().numericType() != null ) {
				if ( ctx.unannType().unannPrimitiveType().numericType().integralType() != null ) {
					newVariable.setType( JMBVariableType.INTEGER );
				}
				else {
					newVariable.setType( JMBVariableType.FLOAT );
				}
			}
			if ( ctx.unannType().unannPrimitiveType().dateType() != null ) {
				newVariable.setType( JMBVariableType.DATE );
			}
			if ( ctx.unannType().unannPrimitiveType().stringType() != null ) {
				if ( ctx.unannType().unannPrimitiveType().stringType().variableLengthStringType() != null ) {
					newVariable.setType( JMBVariableType.STRING );
				}
				else {
					newVariable.setType( JMBVariableType.FIXED_STRING );
					int len = Integer.parseInt( ctx.unannType().unannPrimitiveType().stringType().fixedLengthStringType().fixedLengthDimension().getText() );
					newVariable.setFixedStringLength( len );
				}			
			}
		}
		if ( currentMethod != null ) {
			currentMethod.addLocalVariable( newVariable );
		}
		else {
			if ( currentInnerClass != null ) {
				((RecordClassBuilder) currentInnerClass).addField( getFieldAccess( ctx.PUBLIC() ), newVariable );				
			}
			else {
				currentModule.addField( getFieldAccess( ctx.PUBLIC() ), newVariable );
			}
		}

		return null;
	}

	///////////////////////////////////////////////////////////////////////////////
	//////   RECORD DECLARATION	
	
	@Override
	public Void visitRecordTypeDeclaration(RecordTypeDeclarationContext ctx) {
		debug(" visitRecordTypeDeclaration()");

		currentInnerClass = currentModule.beginRecordTypeDefinition(ctx.recordIdentifier().getText(), getTypeAccess(ctx.PUBLIC()));
				
		debug("RECORD :"+ctx.recordIdentifier().getText());
		
		super.visitRecordTypeDeclaration(ctx);
		
		currentModule.endRecordTypeDefinition();
		
		String javaFullyQualifiedClassName = ((RecordClassBuilder)currentInnerClass).getRecordClassFullyQualifiedName();
		
        ClassFileWriter cw = new  ClassFileWriter( ClassFileWriter.BASE_DIR, javaFullyQualifiedClassName);   
        try {
			cw.open();
	        cw.write(currentInnerClass.toByteArray());
	        cw.close();	
		} catch ( IOException e1) {
			e1.printStackTrace();
		}
	            
		currentInnerClass = null;
		
		return null;
	}
	
	
	@Override
	public Void visitRecordField(RecordFieldContext ctx) {
		debug(" visitRecordField() " + ctx.variableIdentifier().getText());
		
		String fieldName = ctx.variableIdentifier().getText().toLowerCase();
		fieldName = fieldName.substring(1);	
		Type fieldType = getFieldType( ctx.unannType() ).getJvmType();
		
		currentInnerClass.visitField(getFieldAccess( ctx.PUBLIC() ), fieldName, fieldType.getDescriptor(), null, null);
		
		return super.visitRecordField(ctx);
	}	

	///////////////////////////////////////////////////////////////////////////////
	//////   STATEMENT DECLARATION
	
	@Override
	public Void visitProcedureDeclaration(ProcedureDeclarationContext ctx) {
		debug(" visitProcedureDeclaration()");
		MethodBuilder previewMethod = currentMethod;
		if ( ctx.procedureFormalParameters().procedureFormalParameterList() != null ) {
			// have formal parameters

//			debug(" visitProcedureDeclaration()"+ctx.procedureFormalParameters().procedureFormalParameterList());
//			debug(" visitProcedureDeclaration()"+ctx.procedureFormalParameters().procedureFormalParameterList().procedureFormalParameter().size());		
		}
		else {
			currentMethod = Methods.buildProcedureWithoutParameters(currentModule, ctx.Identifier().getText(), getMethodAccess(ctx.PUBLIC()));
		}
		
		super.visitProcedureDeclaration(ctx);
		
		currentMethod.returnValue();
		currentMethod.endMethod();
		currentMethod.visitMaxs(12, 2);	

		currentMethod = previewMethod;
		
		return null;
	}	
	
		
	///////////////////////////////////////////////////////////////////////////////
	//////   BUILTIN FUNCTION CALL	

	@Override
	public Void visitBuiltinFunctionCall(BuiltinFunctionCallContext ctx) {
		super.visitBuiltinFunctionCall(ctx);
		debug(" visitBuiltinFunctionCall()");
		if ( ctx.builtinFunction().CURRENTDATE() != null ) {
			currentMethod.invokeStatic(Type.getType(Builtin.class), Method.getMethod("java.util.Date currentDate ()") );
		}
		
		return null;
	}

	@Override
	public Void visitIntegerLiteral(IntegerLiteralContext ctx) {
		currentMethod.pushOnStack( new Long ( ctx.getText() ) );
		return super.visitIntegerLiteral(ctx);
	}	

	@Override
	public Void visitFloatingPointLiteral(FloatingPointLiteralContext ctx) {
		currentMethod.pushOnStack( new Double( ctx.getText() ) );
		return super.visitFloatingPointLiteral(ctx);
	}
	
	@Override
	public Void visitStringLiteral(StringLiteralContext ctx) {
		currentMethod.pushOnStack( ctx.getText().substring(1, ctx.getText().length()-1) );
		return super.visitStringLiteral(ctx);
	}
	
	@Override
	public Void visitPrintStatement(PrintStatementContext ctx) {
		super.visitPrintStatement(ctx);
		
		List<Object> lst = new ArrayList<Object>();
		for (ConcatStringsContext cs : ctx.concatStrings()) {
			for (PrimaryContext lc : cs.primary()) {
				if( lc.literal() != null ) {
					for ( ParseTree node : lc.literal().children ) {
						String str = node.getText();
						if ( node instanceof StringLiteralContext ) {
							str = str.substring(1, str.length()-1);
						}
						lst.add( str );
					}
				}
				else {
					try {
						LocalVariable lv = currentMethod.getVariable(lc.variableIdentifier().getText().substring(1).toLowerCase());
						lst.add( lv );
					} catch (CompileException e) {
						e.printStackTrace();
						System.exit(1);;
					}
				}
			}
		}
		
		currentMethod.printStatement( lst );
		
		return null;
	}
/*
	@Override
	public Void visitAssignment(AssignmentContext ctx) {
		super.visitAssignment(ctx);
		debug(" visitAssignment() ");	
		
		try {
			currentMethod.storeInLocalVar( convertVariableName ( ctx.leftHandSide().getText() ) );
		} catch (CompileException e) {
			e.printStackTrace();
			System.exit(1);
		}		
			
		return null; 
	}
*/
	private void debug ( String str ) {
		System.out.println( str );
	}
	
	private String convertVariableName ( String varName ) {
		return varName.substring(1).toLowerCase();
	}
	
	private String convertMethodName ( String methodName ) {
		return methodName.toLowerCase();
	}
	
	private JMBVariableType getFieldType ( UnannTypeContext ctx ) {
		if ( ctx.unannPrimitiveType().numericType() != null ) {
			if ( ctx.unannPrimitiveType().numericType().integralType() != null ) {
				return JMBVariableType.INTEGER;
			}
			else {
				return JMBVariableType.FLOAT;
			}
		}
		if ( ctx.unannPrimitiveType().dateType() != null ) {
			return JMBVariableType.DATE;
		}
		if ( ctx.unannPrimitiveType().stringType() != null ) {
			if ( ctx.unannPrimitiveType().stringType().variableLengthStringType() != null ) {
				return JMBVariableType.STRING;
			}
			else {
				return JMBVariableType.FIXED_STRING;
			}			
		}
		return null;
	}
	
	private int getFieldAccess(Object PUBLIC) {
		if ( PUBLIC != null ) 
			return ACC_PUBLIC;
		else
			return ACC_PRIVATE;
	}
	
	private int getTypeAccess(Object PUBLIC) {
		if ( PUBLIC != null ) 
			return ACC_PUBLIC;
		else
			return ACC_PRIVATE;
	}
	
	private int getMethodAccess(Object PUBLIC) {
		if ( PUBLIC != null ) 
			return ACC_PUBLIC;
		else
			return ACC_PRIVATE;
	}
	
	private boolean isExecutableModule () {
		return executableModuleName != null;
	}
}