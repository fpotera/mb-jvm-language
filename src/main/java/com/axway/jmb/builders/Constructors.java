// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb.builders;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Bytecode generator for java class constructors.
 *
 * @author Florin Potera
 */

public class Constructors {
	
	public static void buildDefault( ClassVisitor clazz , String superClassInternalFQName) {
		MethodVisitor mv = clazz.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		AdviceAdapter constructor = new AdviceAdapter( ASM5, mv, ACC_PUBLIC, "<init>", "()V") {};
		constructor.visitCode();
		constructor.visitMaxs(4, 1);
		
		callSuperConstructor( constructor, superClassInternalFQName );
		
		constructor.visitInsn(RETURN);
		constructor.visitEnd();		
	}
	
	public static AdviceAdapter startDefault( ClassVisitor clazz, String superClassInternalFQName ) {
		MethodVisitor mv = clazz.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		AdviceAdapter constructor = new AdviceAdapter( ASM5, mv, ACC_PUBLIC, "<init>", "()V") {};
		
		constructor.visitCode();

		callSuperConstructor( constructor, superClassInternalFQName );
				
		return constructor;
	}
	
	public static void endDefault( AdviceAdapter constructor, int maxStacks, int maxLocals ) {
		constructor.visitInsn(RETURN);
		constructor.visitMaxs(maxStacks, maxLocals);
		constructor.visitEnd();	
	}
	
	private static void callSuperConstructor ( AdviceAdapter constructor, String superClassInternalFQName ) {

		constructor.visitVarInsn(ALOAD, 0);
		constructor.visitInsn(DUP);
		constructor.visitMethodInsn(INVOKESPECIAL, superClassInternalFQName, "<init>", "()V", false);	

	}
	
}
