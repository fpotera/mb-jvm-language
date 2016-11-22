// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

import static org.objectweb.asm.Opcodes.ASM5;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import com.axway.jmb.builders.Constructors;
import com.axway.jmb.builders.Fields;
import com.axway.jmb.builders.Modules;

/**
 * Base class for Java MessageBuilder module generator.
 *
 * @author Florin Potera
 */

public class ModuleBuilder extends CheckClassAdapter {
	
	private String classFullyQualifiedName;
	private ConstructorBuilder constructor;
	
	private RecordClassBuilder currentRecordTypeDefinition;
	
	private Map<String, ClassField> fields = new HashMap<String, ClassField>();
	
	private Map<Label,Integer> labels = new HashMap<Label,Integer>();
	
	public ModuleBuilder(int params, String moduleFullyQualifiedName, ClassWriter cw) {
		super(ASM5, new TraceClassVisitor(cw, new PrintWriter (System.out)), false); 
		
		classFullyQualifiedName = Utils.getJavaFullyQualifiedInternalClassName( moduleFullyQualifiedName );
		
		Modules.buildModule(this, classFullyQualifiedName );
		
		defineMainMethod();
		
		Modules.buildGetModuleMethod( this, Type.getType(classFullyQualifiedName), getLabels() );
				
		constructor = Constructors.startDefault( this, Type.getType(JMBModule.class).getInternalName() );
	}

	public void addField (int access, ClassField field) {
		fields.put(field.getName(), field);
		
		Fields.addFieldToClass(access, field, constructor, this, classFullyQualifiedName );
		
	}
	
	public void visitEnded() {
		Constructors.endDefault( constructor, 10, 10 );
		visitEnd();
	}
	
	protected int getClassMembersAccess () {
		return 0;
	}
	
	public RecordClassBuilder beginRecordTypeDefinition ( String messageBuilderRecordName, int access ) {
		currentRecordTypeDefinition = new RecordClassBuilder(0, messageBuilderRecordName, classFullyQualifiedName, access);
		return currentRecordTypeDefinition;
	}
	
	public void endRecordTypeDefinition () {
		currentRecordTypeDefinition.visitEnd();
		
		visitInnerClass( Utils.getInternalFQClassName(currentRecordTypeDefinition.getRecordClassFullyQualifiedName()) , 
				Utils.getInternalFQClassName(classFullyQualifiedName), 
				currentRecordTypeDefinition.getJavaClassRecordName(), currentRecordTypeDefinition.getAccess());		
	}
	
	public MethodBuilder getMainMethod() {
		return null;
	}
		
	public Map<Label, Integer> getLabels() {
		return labels;
	}
	
	public ConstructorBuilder getConstructor() {
		return constructor;
	}	

	public String getClassFullyQualifiedName() {
		return classFullyQualifiedName;
	}

	protected void defineMainMethod () {}
}
