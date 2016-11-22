// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * Base class for Message Builder modules converted to java.
 *
 * @author Florin Potera
 */

@SuppressWarnings("rawtypes")
public abstract class JMBModule<T extends JMBModule> {
	
/*	private ClassWriter cw;
	private String clazz;// in fully qualified name (ex: java.lang.String)

	public JMBModule ( ClassWriter cw, String clazz ) {
		super();
		this.cw = cw;
		this.clazz = clazz;
	}
	
	public void addBaseCode () {
		cw.visitField( ACC_PRIVATE + ACC_STATIC, "instance", "L"+clazz.replace(".", "/")+";", null, null ).visitEnd();
		
		cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null).visitEnd();
		
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "getModule", "()L"+clazz.replace(".", "/")+";", null, null);		
		mv.visitFieldInsn(GETSTATIC, clazz.replace(".", "/"), "instance", "L"+clazz.replace(".", "/")+";");
		Label nonNull = new Label();
		mv.visitJumpInsn(IFNONNULL, nonNull);
		mv.visitTypeInsn(NEW, clazz.replace(".", "/"));
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, clazz.replace(".", "/"), "<init>", "()V", false);
		mv.visitFieldInsn(PUTSTATIC, clazz.replace(".", "/"), "instance", "L"+clazz.replace(".", "/")+";");
		mv.visitLabel(nonNull);
		mv.visitInsn(ARETURN);
		mv.visitEnd();
	}
*/	
}
