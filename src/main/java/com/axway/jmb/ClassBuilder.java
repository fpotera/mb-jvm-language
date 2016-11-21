// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_7;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.AdviceAdapter;

import com.axway.jmb.builders.Constructors;
import com.axway.jmb.builders.Fields;

/**
 * Base class for Java MessageBuilder module generator.
 *
 * @author Florin Potera
 */

public class ClassBuilder extends ClassWriter {
	
	private String classFullyQualifiedName;
	private AdviceAdapter constructor;
	
	private RecordClassBuilder currentRecordTypeDefinition;
	
	private Map<String, ClassField> fields = new HashMap<String, ClassField>();
	
	public ClassBuilder(int params, String moduleFullyQualifiedName) {
		super(params);
		
		classFullyQualifiedName = Utils.getJavaFullyQualifiedInternalClassName( moduleFullyQualifiedName );
		
		visit(V1_7, ACC_PUBLIC, classFullyQualifiedName, null, "java/lang/Object", null);
		
		constructor = Constructors.startDefault( this );
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
		
		visitInnerClass( Utils.getInternalFullyQualifiedClassName(currentRecordTypeDefinition.getRecordClassFullyQualifiedName()) , 
				Utils.getInternalFullyQualifiedClassName(classFullyQualifiedName), 
				currentRecordTypeDefinition.getJavaClassRecordName(), currentRecordTypeDefinition.getAccess());		
	}
}
