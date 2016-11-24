package com.axway.jmb;

import org.objectweb.asm.MethodVisitor;

public class ConstructorBuilder extends MethodBuilder {
	public ConstructorBuilder(int api, MethodVisitor mv, String desc) {
		super(api, mv, ACC_PUBLIC, "<init>", desc);

	}
}
