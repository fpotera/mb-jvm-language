// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.

package com.axway.jmb;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

import com.axway.jmb.builtin.Builtin;
import com.axway.jmb.support.JMBCSupport;

/**
 * Visitor for java method's build.
 *
 * @author Florin Potera
 */

public class MethodBuilder extends AdviceAdapter {
	private Map<String, LocalVariable> localVars = new HashMap<String, LocalVariable>();
		
	public MethodBuilder(int api, MethodVisitor mv, int access, String name, String desc) {
		super(api, mv, access, name, desc);
		visitCode();
	}

	public void addLocalVariable ( LocalVariable var ) {
		int varPos;
		debug(" addLocalVariable():"+var.getName()+" "+this);
		if ( var.isArray() ) {
			varPos = newLocal( var.getType().getArrayJvmType( var.getArrayDimension() ) );
			push( var.getArrayDimension() );
			newArray( var.getType().getArrayJvmType( var.getArrayDimension() ) );
			storeLocal( varPos, var.getType().getArrayJvmType( var.getArrayDimension() ) );
		}
		else {
			if ( var.getType() == JMBVariableType.RECORD ) {
				varPos = newLocal( var.getRecordType() );
				newInstance( var.getRecordType() );
				dup();
				invokeConstructor( var.getRecordType(), Method.getMethod("void <init>()") );
				storeLocal( varPos, var.getRecordType() );
			}
			else {
				varPos = newLocal( var.getType().getJvmType() );
			}
			if ( var.isInitializationAvailable() ) {
				storeLocal( varPos, var.getType().getJvmType() );
			}
		}
				
		var.setArrayPosition(varPos);
		localVars.put(var.getName(), var);
		
		if ( var.getType() == JMBVariableType.STRING ) {
//			push( "Salut!" );
//			invokeConstructor( var.getType().getJvmType(), Method.getMethod("void <init>(java.lang.String)") );
			
//			getStatic(Type.getType(System.class), "out", Type.getType(PrintStream.class));
//			push( "Salut!" );
//			invokeVirtual(Type.getType(PrintStream.class),
//			         Method.getMethod("void println (String)"));
			
		}
		if ( var.getType() == JMBVariableType.FIXED_STRING ) {
			
		}
		debug(" varPos: "+varPos);
	}
	
	public LocalVariable getLocalVariable ( String name ) {
		return localVars.get( name );
	}
	
//	public void assignValue( LocalVariable var, Object val ) {
//
//		if ( var.getType() == JMBVariableType.STRING ) {
//			push( val instanceof String ? (String) val : null ); // if not string must throw an error - TODO
//			invokeConstructor( var.getType().getJvmType(), Method.getMethod("void <init>(java.lang.String)") );
//			storeLocal( var.getArrayPosition(), var.getType().getJvmType() );			
//		}		
//	}
	
	public void pushOnStack( Object val ) {
		debug( "## push value "+val );
		if( val instanceof Long ) {
			newInstance(  JMBVariableType.INTEGER.getJvmType() );
			dup();
			push( ((Long)val).longValue() );
			invokeConstructor( JMBVariableType.INTEGER.getJvmType(), Method.getMethod("void <init>(long)") );
			return; 
		}
		if( val instanceof Double ) {
			newInstance(  JMBVariableType.FLOAT.getJvmType() );
			dup();
			push( ((Double)val).doubleValue() );
			invokeConstructor( JMBVariableType.FLOAT.getJvmType(), Method.getMethod("void <init>(double)") );
			return; 
		}		
		if( val instanceof String ) {
			newInstance(  JMBVariableType.STRING.getJvmType() );
			dup();
			push( (String) val );
			invokeConstructor( JMBVariableType.STRING.getJvmType(), Method.getMethod("void <init>(java.lang.String)") );
			return; 
		}			
	}
	
	public void storeInLocalVar( String varName, boolean isStatementCall, int statementCallCurrentParameterIndex ) throws CompileException {
		LocalVariable lv = localVars.get(varName);
		if ( lv==null )
			throw new CompileException("Local variable "+varName+" used, but not defined.");

		if ( isStatementCall ) {
			visitInsn(Opcodes.DUP);
			visitInsn(Opcodes.ICONST_0+statementCallCurrentParameterIndex);
			visitInsn(AALOAD);
		}
		
		if ( lv.isArray() ) {
			visitTypeInsn(CHECKCAST, lv.getType().getArrayJvmType( lv.getArrayDimension()).toString() );
			storeLocal( lv.getArrayPosition(), lv.getType().getArrayJvmType( lv.getArrayDimension() ) );
		}
		else {
			if ( lv.getType() == JMBVariableType.FIXED_STRING ) {
				invokeVirtual(Type.getType(String.class),
				         Method.getMethod("char[] toCharArray()"));
				push( (int) lv.getFixedStringLength() );
				invokeStatic(Type.getType(Arrays.class), Method.getMethod("char[] copyOf(char[], int)"));
			}
			else if ( lv.getType() == JMBVariableType.STRING ) {
				invokeVirtual(Type.getType(Object.class),
				         Method.getMethod("String toString()"));			
			} else if (lv.getType() == JMBVariableType.INTEGER) {
				visitTypeInsn(CHECKCAST, "java/lang/Long");
			} else if (lv.getType() == JMBVariableType.FLOAT) {
				visitTypeInsn(CHECKCAST, "java/lang/Double");
			} else if (lv.getType() == JMBVariableType.DATE) {
				visitTypeInsn(CHECKCAST, "java/util/Date");
			}			
			
			storeLocal( lv.getArrayPosition(), lv.getType().getJvmType() );
		}
		
		debug( "## store _"+lv.getArrayPosition() );
	}
	
	public void loadFromLocalVar( String varName, boolean isStatementCall, int statementCallCurrentParameterIndex ) throws CompileException {
		LocalVariable lv = localVars.get(varName);
		if ( lv==null )
			throw new CompileException("Local variable "+varName+" used, but not defined.");
		
		if ( isStatementCall ) {
			visitInsn(Opcodes.DUP);
			visitInsn(Opcodes.ICONST_0+statementCallCurrentParameterIndex);			
		}
		
		if ( lv.isArray() ) {
			loadLocal( lv.getArrayPosition(), lv.getType().getArrayJvmType( lv.getArrayDimension() ) );
		}
		else {
			loadLocal( lv.getArrayPosition(), lv.getType().getJvmType() );
		}
		
		if ( isStatementCall ) {
			visitInsn(Opcodes.AASTORE);
		}
	}
	
	public void loadFromField ( ModuleBuilder mb, String varName, boolean isStatementCall, int statementCallCurrentParameterIndex ) throws CompileException {
		ClassField field = mb.getField(varName);
		if (field == null)
			throw new CompileException("Class field " + varName + " used, but not defined.");
		
		if ( isStatementCall ) {
			visitInsn(Opcodes.DUP);
			visitInsn(Opcodes.ICONST_0+statementCallCurrentParameterIndex);			
		}		
		
		if (field.isArray()) {
			getField(Type.getObjectType(mb.getClassFullyQualifiedName()), varName, field.getType().getArrayJvmType(field.getArrayDimension()));
		} else {
			getField(Type.getObjectType(mb.getClassFullyQualifiedName()), varName, field.getType().getJvmType());
		}
		
		if ( isStatementCall ) {
			visitInsn(Opcodes.AASTORE);
		}
	}
	
	public void storeInField(ModuleBuilder mb, String varName, boolean isStatementCall, int statementCallCurrentParameterIndex) throws CompileException {
		ClassField field = mb.getField(varName);
		if (field == null)
			throw new CompileException("Class field " + varName + " used, but not defined.");
		
		if ( isStatementCall ) {
			visitInsn(Opcodes.DUP);
			visitInsn(Opcodes.ICONST_0+statementCallCurrentParameterIndex);
			visitInsn(AALOAD);
		}		
		
		if (field.getType() == JMBVariableType.FIXED_STRING) {
			invokeVirtual(Type.getType(String.class), Method.getMethod("char[] toCharArray()"));
			push((int) field.getFixedStringLength());
			invokeStatic(Type.getType(Arrays.class), Method.getMethod("char[] copyOf(char[], int)"));
		} else if (field.getType() == JMBVariableType.STRING) {
			invokeVirtual(Type.getType(Object.class), Method.getMethod("String toString()"));
		} else if (field.getType() == JMBVariableType.INTEGER) {
			visitTypeInsn(CHECKCAST, "java/lang/Long");
		} else if (field.getType() == JMBVariableType.FLOAT) {
			visitTypeInsn(CHECKCAST, "java/lang/Double");
		} else if (field.getType() == JMBVariableType.DATE) {
			visitTypeInsn(CHECKCAST, "java/util/Date");
		}
		loadThis();
		swap();
		if (field.isArray()) {
			putField(Type.getObjectType(mb.getClassFullyQualifiedName()), varName, field.getType().getArrayJvmType(field.getArrayDimension()));
		} else {
			putField(Type.getObjectType(mb.getClassFullyQualifiedName()), varName, field.getType().getJvmType());
		}

		debug("## put field " + field.getName());
	}
	
	public void loadFromArray(String loadFromVarName, int parseInt) {
		push(parseInt);
		LocalVariable lv = localVars.get( loadFromVarName );
		arrayLoad( lv.getType().getJvmType() );
	}
	
	public void printStatement ( List<Object> objs ) {
		StringBuffer sb = new StringBuffer();
		Integer tempLocalVar = null;
		for (Object obj : objs ) {
			if ( obj instanceof String ) {
				sb.append( obj );
			}
			else if ( obj instanceof LocalVariable ) {
				LocalVariable lv = (LocalVariable) obj;
				
				if ( tempLocalVar != null ) {
					loadLocal(tempLocalVar, Type.getType(String.class));
					push( sb.toString() );
					invokeVirtual(Type.getType(String.class),
					         Method.getMethod("String concat(String)"));						
				}				
				else {
					push( sb.toString() );
				}
								
				if ( !lv.isArray() ) {
					loadLocal(lv.getArrayPosition(), lv.getType().getJvmType());
					if ( lv.getType() == JMBVariableType.INTEGER ) {
						invokeVirtual(Type.getType(Long.class), Method.getMethod("String toString()"));
					}
					if ( lv.getType() == JMBVariableType.FLOAT ) {
						invokeVirtual(Type.getType(Double.class), Method.getMethod("String toString()"));
					}
					if ( lv.getType() == JMBVariableType.FIXED_STRING ) {
						invokeStatic(Type.getType(String.class), Method.getMethod("String valueOf(char[])"));
					}
					if ( lv.getType() == JMBVariableType.DATE) {
						invokeStatic(Type.getType(JMBCSupport.class), Method.getMethod("String formatDateDefault(java.util.Date)"));
					}					
				}
				else {
					loadLocal(lv.getArrayPosition(), lv.getType().getArrayJvmType( lv.getArrayDimension() ));
					push(lv.getArrayAccess()-1 );
					arrayLoad( lv.getType().getJvmType() );
//					visitTypeInsn(CHECKCAST, "java/lang/String");
//					invokeStatic(Type.getType(Arrays.class), Method.getMethod("String toString(Object[])"));
				}
				invokeVirtual(Type.getType(String.class),
				         Method.getMethod("String concat(String)"));				
				sb = new StringBuffer();
				if ( tempLocalVar == null ) {
					tempLocalVar = newLocal(Type.getType(String.class));
				}
				storeLocal(tempLocalVar, Type.getType(String.class));
			}
		}
		
		if ( tempLocalVar == null ) {
			tempLocalVar = newLocal(Type.getType(String.class));
			push( sb.toString() );
			storeLocal(tempLocalVar, Type.getType(String.class));
		}		
				
		loadLocal(tempLocalVar, Type.getType(String.class));
		invokeStatic(Type.getType(Builtin.class), Method.getMethod("void print ( Object )"));
	}
	
	public void addCallToOpenFile( boolean forRead ) {
		push ( forRead );
		invokeStatic(Type.getType(Builtin.class), Method.getMethod("void openFile ( String , boolean )"));
	}

	public void addCallToCloseFile( boolean forRead ) {
		push ( forRead );
		invokeStatic(Type.getType(Builtin.class), Method.getMethod("void closeFile ( boolean )"));
	}
	
	public void addCallToRead( ModuleBuilder mb, String variableName ) throws CompileException {
		debug(" addCallToRead( " + variableName + ")");
		invokeStatic(Type.getType(Builtin.class), Method.getMethod("String read ( Object )"));
		if ( isLocalVariableDefined( variableName ) ) {
			storeInLocalVar( variableName, false, 0 );
		}
		else {
			storeInField( mb, variableName, false, 0 );
		}
	}
	
	public LocalVariable getVariable( String varName ) throws CompileException {
		debug(" getVariable():"+varName+" "+this);
		LocalVariable lv = localVars.get(varName);
		if ( lv==null )
			throw new CompileException("Local variable "+varName+" used, but not defined.");
		
		return lv;
	}
	
	public boolean isLocalVariableDefined ( String varName ) {
		LocalVariable lv = localVars.get(varName);
		if ( lv != null ) {
			return true;
		}
		return false;
	}
	
	private void debug( String str ) {
		System.out.println( str );
	}



}
