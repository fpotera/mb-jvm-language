// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.

package com.axway.jmb;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import com.axway.jmb.JMessageBuilderParser.AdhocModuleBodyDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.AssignmentContext;
import com.axway.jmb.JMessageBuilderParser.BuiltinFunctionCallContext;
import com.axway.jmb.JMessageBuilderParser.CloseFileStatementContext;
import com.axway.jmb.JMessageBuilderParser.CompilationUnitContext;
import com.axway.jmb.JMessageBuilderParser.ConcatStringsContext;
import com.axway.jmb.JMessageBuilderParser.FieldDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.FloatingPointLiteralContext;
import com.axway.jmb.JMessageBuilderParser.FunctionInvocationContext;
import com.axway.jmb.JMessageBuilderParser.IntegerLiteralContext;
import com.axway.jmb.JMessageBuilderParser.ModuleDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.OpenFileStatementContext;
import com.axway.jmb.JMessageBuilderParser.PrimaryContext;
import com.axway.jmb.JMessageBuilderParser.PrintStatementContext;
import com.axway.jmb.JMessageBuilderParser.ProcedureCallContext;
import com.axway.jmb.JMessageBuilderParser.ProcedureDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.ProcedureRealParameterContext;
import com.axway.jmb.JMessageBuilderParser.ReadStatementContext;
import com.axway.jmb.JMessageBuilderParser.RecordFieldContext;
import com.axway.jmb.JMessageBuilderParser.RecordTypeDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.SingleTypeIncludeDeclarationContext;
import com.axway.jmb.JMessageBuilderParser.StringLiteralContext;
import com.axway.jmb.JMessageBuilderParser.UnannTypeContext;
import com.axway.jmb.JMessageBuilderParser.VariableIdentifierContext;
import com.axway.jmb.annotations.ProcParameterNoiseWord;
import com.axway.jmb.annotations.ProcedureParameter;
import com.axway.jmb.annotations.ProcedureParameters;
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
	private ConstructorBuilder currentConstructor;
	private ConstructorBuilder staticInitialiser;
	private MethodBuilder mainMethod;
	private ClassWriter currentInnerClass;
	private ClassWriter currentClassWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
	
	private boolean isInterface = false;	
	private String executableModuleName;
		
	private boolean classCreated;
	
	// for statement calls
	boolean isStatementCall = false;
	int statementCallCurrentParameterIndex = 0;
	
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
		
		if ( mainMethod != null ) {
			mainMethod.returnValue();
			mainMethod.visitMaxs(10, 5);		
			mainMethod.visitEnd();	
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

	///////////////////////////////////////////////////////////////////////////////
	//////   NON EXECUTABLE MODULE / MODULE INTERFACE DECLARATION		
	
	@Override
	public Void visitModuleDeclaration(ModuleDeclarationContext ctx) {
		if ( ctx.INTERFACE() != null )
			isInterface = true;	
		
		if ( isInterface ) {
			debug("Generate module interface:"+ctx.moduleIdentifier().getText());
			currentModule = new ModuleInterfaceBuilder(0, ctx.moduleIdentifier().getText(), currentClassWriter);
		}
		else {
			debug("Generate module:"+ctx.moduleIdentifier().getText());
			currentModule = new ModuleBuilder(0, ctx.moduleIdentifier().getText(), currentClassWriter);			
		}
		currentConstructor = currentModule.getConstructor();
		staticInitialiser = currentModule.getStaticInitialiser();
		
		return super.visitModuleDeclaration(ctx);
	}

	///////////////////////////////////////////////////////////////////////////////
	//////   EXECUTABLE MODULE DECLARATION
	
	@Override
	public Void visitAdhocModuleBodyDeclaration(AdhocModuleBodyDeclarationContext ctx) {		
		if ( !classCreated ) {
			debug("Generate executable module:"+executableModuleName);
	
			currentModule = new ExecutableModuleBuilder(0, executableModuleName, currentClassWriter);
			
			mainMethod = currentModule.getMainMethod();
			currentConstructor = currentModule.getConstructor();
			staticInitialiser = currentModule.getStaticInitialiser();
			
			classCreated = true;
		}

		return super.visitAdhocModuleBodyDeclaration(ctx);
	}

	///////////////////////////////////////////////////////////////////////////////
	//////   FIELD/VARIABLE DECLARATION
	
	@Override
	public Void visitFieldDeclaration(FieldDeclarationContext ctx) {
		LocalVariable newVariable = new LocalVariable();
		
		super.visitFieldDeclaration(ctx);
		
		debug(" visitFieldDeclaration() "+ctx.variableDeclarator().variableDeclaratorId().variableIdentifier().getText());
		
		int fieldAccess = getFieldAccess( ctx.PUBLIC() );
		if ( ctx.constOnce() != null && ctx.constOnce().CONSTANT() != null ) {
			fieldAccess += ACC_FINAL;
			if ( isInterface ) {
				fieldAccess += ACC_STATIC;
			}
		}
		
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
				((RecordClassBuilder) currentInnerClass).addField( fieldAccess, newVariable );				
			}
			else {
				currentModule.addField( fieldAccess, newVariable );
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
		
        ClassFileWriter cw = new  ClassFileWriter( javaFullyQualifiedClassName );   
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
	//////   STATEMENT (PROCEDURE) DECLARATION
	
	@Override
	public Void visitProcedureDeclaration(ProcedureDeclarationContext ctx) {
		debug(" visitProcedureDeclaration()");

		if ( ctx.procedureFormalParameters() != null && ctx.procedureFormalParameters().procedureFormalParameterList() != null ) {
			// have formal parameters
//			debug(" visitProcedureDeclaration()"+ctx.procedureFormalParameters().procedureFormalParameterList());
//			debug(" visitProcedureDeclaration()"+ctx.procedureFormalParameters().procedureFormalParameterList().procedureFormalParameter().size());		
		}
		else {
			String procedureName = "";
			if ( ctx.Identifier() != null ) {
				procedureName = ctx.Identifier().getText();
			}
			if ( ctx.START() != null ) {
				procedureName = "START";
			}
			currentMethod = Methods.buildProcedureWithoutParameters(currentModule, procedureName, 
					getMethodAccess(ctx.PUBLIC()), currentModule.getLabels());
		}
		
		super.visitProcedureDeclaration(ctx);
		
		currentMethod.returnValue();
		currentMethod.visitMaxs(40, 2);	
		currentMethod.visitEnd();

		currentMethod = null;
		
		return null;
	}	
	
	///////////////////////////////////////////////////////////////////////////////
	//////   STATEMENT (PROCEDURE) CALL	

	@Override
	public Void visitProcedureCall(ProcedureCallContext ctx) {		
//		super.visitProcedureCall(ctx);
		debug("visitProcedureCall():"+currentModule.getClassFullyQualifiedName());
		
		String procedureCall = "";
		if ( ctx.START() != null ) {
			procedureCall = "START";
		}
		if ( ctx.SL_STRING_REVERSE() != null ) {
			procedureCall = "SL_STRING.REVERSE";
		}
		if ( ctx.SOCKET_DICONNECT() != null ) {
			procedureCall = "SOCKET.DISCONNECT";
		}
		
		String procedureName = procedureCall;
		String moduleName = "";
		if ( procedureCall.contains(".") ) {
			// call to external procedure
			moduleName = procedureCall.split("\\.")[0];
			procedureName = procedureCall.split("\\.")[1];
		}
		
		try {
			if ( currentMethod == null ) {
				if ( moduleName.equals("") ) {
					Type classType = Type.getObjectType( Utils.getInternalFQClassName(currentModule.getClassFullyQualifiedName()) );
					Methods.callLocalProcedure( mainMethod, classType,
							new Method("getModule", classType, new Type[0]),
							new Method(Utils.getJavaMethodName(procedureName), Type.VOID_TYPE, new Type[0]));
				}
				else {
					callRemoteProcedure(moduleName, procedureName, ctx);
				}
			}
			else {
				if ( moduleName.equals("") ) {
					Type classType = Type.getObjectType( Utils.getInternalFQClassName(currentModule.getClassFullyQualifiedName()) );
					Methods.callLocalProcedure( currentMethod, classType,
							new Method("getModule", classType, new Type[0]),
							new Method(Utils.getJavaMethodName(procedureName), Type.VOID_TYPE, new Type[0]));
				}
				else {
					callRemoteProcedure(moduleName, procedureName, ctx);
				}			
			}
		} catch (CompileException e) {
			e.printStackTrace();
			System.exit(1);
		}
	
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

	///////////////////////////////////////////////////////////////////////////////
	//////   PUSH LITERALS ON STACK	
	
	@Override
	public Void visitIntegerLiteral(IntegerLiteralContext ctx) {
		saveLiteral( new Long ( ctx.getText() ) );
			
		return super.visitIntegerLiteral(ctx);
	}	

	@Override
	public Void visitFloatingPointLiteral(FloatingPointLiteralContext ctx) {
		saveLiteral( new Double( ctx.getText() ) );

		return super.visitFloatingPointLiteral(ctx);
	}
	
	@Override
	public Void visitStringLiteral(StringLiteralContext ctx) {
		String str = ctx.getText().substring(1, ctx.getText().length()-1);
		if ( str.equals("nl") ) {
			str = "\n";
		}
		if ( str.contains("dp") ) {
			str = str.replaceAll("dp", ":");
		}
		saveLiteral( str );		
		return super.visitStringLiteral(ctx);
	}

	///////////////////////////////////////////////////////////////////////////////
	//////   ASSIGNMENTS		

	@Override
	public Void visitAssignment(AssignmentContext ctx) {
		super.visitAssignment(ctx);
		debug(" visitAssignment() :"+ctx.leftHandSide().getText());	
		
		try {
			String varName = convertVariableName ( ctx.leftHandSide().getText() );
			if ( currentMethod != null ) {
				if ( currentMethod.isLocalVariableDefined( varName ) ) {
					currentMethod.storeInLocalVar( varName, false, 0 );
				}
				else if ( currentModule.isFieldDefined(varName) ) {
					currentMethod.storeInField( currentModule, varName, false, 0 );
				}
				else {
					throw new CompileException("Variable "+varName+" used, but not defined.");
				}
			}
			else {
				if ( currentConstructor.isLocalVariableDefined( varName ) ) {
					currentConstructor.storeInLocalVar( varName, false, 0 );
				}
				else if ( currentModule.isFieldDefined(varName) ) {
					currentConstructor.storeInField( currentModule, varName, false, 0 );
				}
				else {
					throw new CompileException("Variable "+varName+" used, but not defined.");
				}
			}
		} catch (CompileException e) {
			e.printStackTrace();
			System.exit(1);
		}		
			
		return null; 
	}

	///////////////////////////////////////////////////////////////////////////////
	//////   FUNCTION CALL
	
	@Override
	public Void visitFunctionInvocation(FunctionInvocationContext ctx) {
		debug("visitFunctionInvocation()");
		String functionFullName = ctx.functionName().getText();
		String moduleName = "";
		String functionName = functionFullName;
		
		isStatementCall = true;
		statementCallCurrentParameterIndex = 0;
		
		if ( functionFullName.contains(".") ) {
			moduleName = functionFullName.split("\\.")[0];
			functionName = functionFullName.split("\\.")[1];
		}
		debug("call function :"+Utils.getJavaFullyQualifiedClassName(moduleName)+"."+Utils.getJavaMethodName(functionName)+"(...)");
		
		Type moduleType = Utils.getJavaFullyQualifiedClassType( moduleName );
		
		Method getModuleMethod = new Method("getModule", moduleType, new Type[0]);
		Method calledFunction = new Method(Utils.getJavaMethodName(functionName), Type.getObjectType("java/lang/Object"), new Type[]{Type.getObjectType("[Ljava/lang/Object;")});
		int arraySize = ctx.argumentList() != null ? ctx.argumentList().expression().size() : 0; 
		
		if ( "".equals(moduleName) ) {
			// internal module in an executable module
		}
		else {
			if ( currentMethod != null ) {			
				currentMethod.invokeStatic(moduleType, getModuleMethod);
				currentMethod.visitInsn(Opcodes.ICONST_0+arraySize);
				currentMethod.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
			}
			else {
				currentConstructor.invokeStatic(moduleType, getModuleMethod);
				currentConstructor.visitInsn(Opcodes.ICONST_0+arraySize);
				currentConstructor.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
			}
		}
		
		super.visitFunctionInvocation(ctx);

		if ( "".equals(moduleName) ) {
			// internal module in an executable module
		}
		else {
			if ( currentMethod != null ) {
				currentMethod.invokeVirtual(moduleType, calledFunction);
			}
			else {
				currentConstructor.invokeVirtual(moduleType, calledFunction);
			}
		}
		
		isStatementCall = false;
		
		return null;
	}	
	
	
	///////////////////////////////////////////////////////////////////////////////
	//////   VARIABLE IDENTIFIER 
	
	@Override
	public Void visitVariableIdentifier(VariableIdentifierContext ctx) {
		super.visitVariableIdentifier(ctx);
		
		if ( isStatementCall ) {
			try {
				debug("visitVariableIdentifier() for statementCallParameter");
				String varName = convertVariableName ( ctx.getText() );
				if ( currentMethod != null ) {
					currentMethod.visitInsn(Opcodes.DUP);
					currentMethod.visitInsn(Opcodes.ICONST_0+statementCallCurrentParameterIndex);					
					
					if ( currentMethod.isLocalVariableDefined( varName ) ) {
						currentMethod.loadFromLocalVar( varName, false, 0 );
					}
					else if ( currentModule.isFieldDefined(varName) ) {
						currentMethod.loadFromField( currentModule, varName, false, 0 );
					}
					else {
						throw new CompileException("Variable "+varName+" used, but not defined.");
					}

					currentMethod.visitInsn(Opcodes.AASTORE);					
				}
				else {
					currentConstructor.visitInsn(Opcodes.DUP);
					currentConstructor.visitInsn(Opcodes.ICONST_0+statementCallCurrentParameterIndex);
					
					if ( currentConstructor.isLocalVariableDefined( varName ) ) {
						currentConstructor.loadFromLocalVar( varName, false, 0 );
					}
					else if ( currentModule.isFieldDefined(varName) ) {
						currentConstructor.loadFromField( currentModule, varName, false, 0 );
					}
					else {
						throw new CompileException("Variable "+varName+" used, but not defined.");
					}
					
					currentConstructor.visitInsn(Opcodes.AASTORE);
				}

				statementCallCurrentParameterIndex++;
			} catch (CompileException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return null;
	}	
	
	///////////////////////////////////////////////////////////////////////////////
	//////   CALL BUILTIN PRINT PROCEDURE		
	
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
							if ( str.equals("nl") ) {
								str = "\n";
							}
							if ( str.contains("dp") ) {
								str = str.replaceAll("dp", ":");
							}
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

	///////////////////////////////////////////////////////////////////////////////
	//////   CALL BUILTIN OPEN FILE PROCEDURE	
	
	@Override
	public Void visitOpenFileStatement(OpenFileStatementContext ctx) {
		debug(" visitOpenFileStatement()");
		super.visitOpenFileStatement(ctx);
		
		if ( currentMethod != null )  {
			currentMethod.addCallToOpenFile( ctx.INPUT() != null );
		}
		else {
			currentConstructor.addCallToOpenFile( ctx.INPUT() != null );
		}
		
		return null;
	}	

	///////////////////////////////////////////////////////////////////////////////
	//////   CALL BUILTIN CLOSE FILE PROCEDURE	

	@Override
	public Void visitCloseFileStatement(CloseFileStatementContext ctx) {
		debug(" visitCloseFileStatement()");
		super.visitCloseFileStatement(ctx);
		
		if ( currentMethod != null )  {
			currentMethod.addCallToCloseFile( ctx.INPUT() != null );
		}
		else {
			currentConstructor.addCallToCloseFile( ctx.INPUT() != null );
		}		
		
		return null;
	}	

	///////////////////////////////////////////////////////////////////////////////
	//////   CALL BUILTIN READ PROCEDURE		

	@Override
	public Void visitReadStatement(ReadStatementContext ctx) {
		debug(" visitReadStatement()");
		super.visitReadStatement(ctx);
		
		debug(" visitReadStatement()" + ctx.primary().literal() );
		
		try {
			if ( currentMethod != null )  {
				currentMethod.addCallToRead( currentModule, convertVariableName( ctx.variableIdentifier().getText()) );
			}
			else {
				currentConstructor.addCallToRead( currentModule, convertVariableName( ctx.variableIdentifier().getText() ) );
			}
		}
		catch (CompileException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}	
	
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

	private void saveLiteral ( Object obj ) {
		if ( !isStatementCall ) {
			if ( currentMethod != null ) {
				currentMethod.pushOnStack( obj );
			}
			else {
				currentConstructor.pushOnStack( obj );
			}
		}
		else {
			if ( currentMethod != null ) {
				currentMethod.visitInsn(Opcodes.DUP);
				currentMethod.visitInsn(Opcodes.ICONST_0+statementCallCurrentParameterIndex);
				currentMethod.pushOnStack( obj );
				currentMethod.visitInsn(Opcodes.AASTORE);
			}
			else {
				currentConstructor.visitInsn(Opcodes.DUP);
				currentConstructor.visitInsn(Opcodes.ICONST_0+statementCallCurrentParameterIndex);
				currentConstructor.pushOnStack( obj );
				currentConstructor.visitInsn(Opcodes.AASTORE);
			}
			statementCallCurrentParameterIndex++;
		}			
	}
	
	private void callRemoteProcedure ( String moduleName, String procedureName, ProcedureCallContext ctx ) throws CompileException {
		debug("callRemoteProcedure():"+moduleName+"."+procedureName);
		String moduleAsJavaClass = Utils.getJavaFullyQualifiedClassName( moduleName );
		String procName = Utils.getJavaMethodName( procedureName );
		
		ProcedureParameters params = (ProcedureParameters) Reflections.getAnnotationOfMethod(moduleAsJavaClass, procName, ProcedureParameters.class);
		if ( params == null ) {
			throw new CompileException("Call remote statement "+moduleName+"."+procedureName+"  doesn't have defined necesary annotations.");
		}
		
		Type moduleType = Utils.getJavaFullyQualifiedClassType( moduleName );
		Method getModuleMethod = new Method("getModule", moduleType, new Type[0]);
		Method calledProcedure = new Method(Utils.getJavaMethodName(procName), Type.getObjectType("[Ljava/lang/Object;"), new Type[]{Type.getObjectType("[Ljava/lang/Object;")});
		
		int arraySizeIn = 0;
		int arraySizeOut = 0;
		for ( ProcedureParameter param : params.value() ) {
			if ( param.paramIOType() == ProcedureParameterIOType.IN ||
					param.paramIOType() == ProcedureParameterIOType.INOUT ) {
				arraySizeIn++;
			}
			if ( param.paramIOType() == ProcedureParameterIOType.OUT ||
					param.paramIOType() == ProcedureParameterIOType.INOUT ) {
				arraySizeOut++;
			}
		}
		MethodBuilder crtMet = currentMethod!=null?currentMethod:currentConstructor;
		Methods.beginProcedureCall(crtMet, moduleType, getModuleMethod, arraySizeIn);

		int indexInParams = 0;
		boolean workWithNoiseWords = false;
		int indexInValues = 0;
		int indexOfInParametersInCallArray = 0;
		// cycle for input before method call
		for ( ProcedureParameter param : params.value() ) {
			if ( param.paramIOType() == ProcedureParameterIOType.IN ||
					param.paramIOType() == ProcedureParameterIOType.INOUT ) {
				if ( indexInParams == 1 && param.noiseWord().value() == ProcParameterNoiseWord.Word.DEFINED ) {
					workWithNoiseWords = true;
				}
				if ( !workWithNoiseWords ) {
					if (ctx.procedureRealParameterList() != null) {
						ProcedureRealParameterContext rp = ctx.procedureRealParameterList().procedureRealParameter().get( indexInValues );
						if ( param.paramIOType() == ProcedureParameterIOType.INOUT && rp.expression().primary().variableIdentifier() == null ) {
							throw new CompileException("Call remote statement "+moduleName+"."+procedureName+"  with OUT parameter " + indexInParams + " as a primitive and not as a variable.");
						}
						// put reference on stack
						String varName = convertVariableName( rp.expression().primary().variableIdentifier().getText() );						
						varName = "y";
						if ( crtMet.isLocalVariableDefined(varName) ) {
							crtMet.loadFromLocalVar( varName, true, indexOfInParametersInCallArray );
						}
						else {
							crtMet.loadFromField( currentModule, varName, true, indexOfInParametersInCallArray );
						}
						indexOfInParametersInCallArray++;
					}
					else {
					}
				}
				else {
					
				}
			}

			indexInParams++;
			indexInValues++;
		}
		
		Methods.doProcedureCall(crtMet, moduleType, calledProcedure);
		
		indexInParams = 0;
		workWithNoiseWords = false;
		indexInValues = 0;
		int indexOfInParametersInResultsArray = 0;
		// cycle for output after method call
		for ( ProcedureParameter param : params.value() ) {
			if ( param.paramIOType() == ProcedureParameterIOType.OUT ||
					param.paramIOType() == ProcedureParameterIOType.INOUT ) {
				if ( indexInParams == 1 && param.noiseWord().value() == ProcParameterNoiseWord.Word.DEFINED ) {
					workWithNoiseWords = true;
				}				
				if ( !workWithNoiseWords ) {
					if (ctx.procedureRealParameterList() != null) {
						ProcedureRealParameterContext rp = ctx.procedureRealParameterList().procedureRealParameter().get( indexInValues );
						// get reference on stack
						String varName = convertVariableName( rp.expression().primary().variableIdentifier().getText() );
						varName = "y";						
						if ( crtMet.isLocalVariableDefined(varName) ) {
							crtMet.storeInLocalVar( varName, true, indexOfInParametersInResultsArray );
						}
						else {
							crtMet.storeInField( currentModule, varName, true, indexOfInParametersInResultsArray );
						}
						indexOfInParametersInResultsArray++;						

					}
				}

			
			}
			
		}
		
		Methods.endProcedureCall(crtMet);
	}
}








