// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb.builders;

import static org.objectweb.asm.Opcodes.ASM5;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import com.axway.jmb.MethodBuilder;
import com.axway.jmb.Utils;

/**
 * Bytecode generator for java class methods.
 *
 * @author Florin Potera
 */

public class Methods {
	
	public static MethodBuilder buildProcedureWithoutParameters ( ClassWriter clazz, String messageBuilderStatementName, int accessType ) {
		MethodVisitor mv = clazz.visitMethod(accessType, Utils.getJavaMethodName(messageBuilderStatementName), "()V", null, null);
		
		return new MethodBuilder( ASM5, mv, accessType, Utils.getJavaMethodName(messageBuilderStatementName), "()V" );				
	}
	
	
}
