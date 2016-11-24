// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM5;

import java.util.HashMap;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.CheckMethodAdapter;

/**
 * Class for Java MessageBuilder executable module generator.
 *
 * @author Florin Potera
 */

public class ExecutableModuleBuilder extends ModuleBuilder {
	
	private MethodBuilder mainMethod;

	public ExecutableModuleBuilder(int params, String moduleFullyQualifiedName, ClassWriter cw) {
		super(params, moduleFullyQualifiedName, cw);
	}

	@Override
	protected int getClassMembersAccess() {
		return ACC_STATIC;
	}

	@Override
	protected void defineMainMethod () {
		MethodVisitor mv = visitMethod(ACC_PUBLIC+ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		
		mv = new CheckMethodAdapter(ACC_PUBLIC + ACC_STATIC, "main","([Ljava/lang/String;)V", mv, getLabels());
		
		mainMethod = new MethodBuilder( ASM5, mv, ACC_PUBLIC+ACC_STATIC, "main", "([Ljava/lang/String;)V" );		
	}
	
	@Override
	public void visitEnded() {
		
		super.visitEnded();
	}

	@Override
	public MethodBuilder getMainMethod() {
		return mainMethod;
	}

}
