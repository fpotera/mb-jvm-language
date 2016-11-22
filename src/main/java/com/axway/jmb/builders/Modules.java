package com.axway.jmb.builders;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.V1_7;

import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckMethodAdapter;

import com.axway.jmb.Utils;

public class Modules {
	private static final String JAVA_BASE_MODULE_FQ_CLASS_NAME = "com.axway.jmb.JMBModule";
	
	public static void buildModule ( ClassVisitor cv, String classFullyQualifiedName ) {
		System.out.println( " build module for: " +classFullyQualifiedName);
		cv.visit(V1_7, ACC_PUBLIC, classFullyQualifiedName, null, Utils.getInternalFQClassName(JAVA_BASE_MODULE_FQ_CLASS_NAME), null);
	}
	
	public static void buildGetModuleMethod( ClassVisitor cv, Type classType, Map<Label,Integer> labels) {
		
		cv.visitField( ACC_PUBLIC + ACC_STATIC, "instance", "L"+classType.getInternalName()+";", null, null ).visitEnd();
		
		MethodVisitor mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC, "getModule", "()L"+classType.getInternalName()+";", null, null);	
		
		mv = new CheckMethodAdapter(ACC_PUBLIC + ACC_STATIC, "getModule", "()L"+classType.getInternalName()+";", mv, labels);
		
		mv.visitCode();
		mv.visitFieldInsn(GETSTATIC, classType.getInternalName(), "instance", "L"+classType.getInternalName()+";");
		Label nonNull = new Label();
		mv.visitJumpInsn(IFNONNULL, nonNull);
		mv.visitTypeInsn(NEW, classType.getInternalName());
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, classType.getInternalName(), "<init>", "()V", false);
		mv.visitFieldInsn(PUTSTATIC, classType.getInternalName(), "instance", "L"+classType.getInternalName()+";");
		mv.visitLabel(nonNull);
		mv.visitFieldInsn(GETSTATIC, classType.getInternalName(), "instance", "L"+classType.getInternalName()+";");
		mv.visitInsn(ARETURN);
		mv.visitMaxs(2, 0);
		mv.visitEnd();
	}
}
