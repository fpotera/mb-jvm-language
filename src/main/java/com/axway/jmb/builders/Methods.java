// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb.builders;

import static org.objectweb.asm.Opcodes.ASM5;

import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.util.CheckMethodAdapter;

import com.axway.jmb.CompileException;
import com.axway.jmb.MethodBuilder;
import com.axway.jmb.Reflections;
import com.axway.jmb.Utils;
import com.axway.jmb.annotations.ProcedureParameter;
import com.axway.jmb.annotations.ProcedureParameters;

/**
 * Bytecode generator for java class methods.
 *
 * @author Florin Potera
 */

public class Methods {
	
	public static MethodBuilder buildProcedureWithoutParameters ( ClassVisitor clazz, String messageBuilderStatementName, 
			int accessType, Map<Label,Integer> labels ) {
		debug("buildProcedureWithoutParameters("+clazz+","+messageBuilderStatementName+","+accessType+")");
		
		MethodVisitor mv = clazz.visitMethod(accessType, Utils.getJavaMethodName(messageBuilderStatementName), "()V", null, null);
		
		mv = new CheckMethodAdapter(accessType, Utils.getJavaMethodName(messageBuilderStatementName), "()V", mv, labels);
		
		return new MethodBuilder( ASM5, mv, accessType, Utils.getJavaMethodName(messageBuilderStatementName), "()V" );				
	}
	
	public static void callLocalProcedure(MethodBuilder mainMethod, Type currentClass, Method getInstanceMethod, Method procedureMethod) {
		debug("callLocalProcedure()");
		mainMethod.invokeStatic(currentClass, getInstanceMethod);
		mainMethod.invokeVirtual(currentClass, procedureMethod);
	}
	
	public static void beginProcedureCall( MethodBuilder method, Type clazz, Method getInstanceMethod, int arraySize) {
		method.invokeStatic(clazz, getInstanceMethod);
		method.visitInsn(Opcodes.ICONST_0+arraySize);
		method.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
	}
	
	public static void doProcedureCall( MethodBuilder method, Type clazz, Method procedureMethod ) {
		method.invokeVirtual(clazz, procedureMethod);
	}
	
	public static void endProcedureCall( MethodBuilder method ) {
		method.visitInsn(Opcodes.POP);
	}
	
	private static void debug ( String str ) {
		System.out.println( str );;
	}
}
