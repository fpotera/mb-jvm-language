// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

import static org.objectweb.asm.Opcodes.V1_7;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import com.axway.jmb.builders.Constructors;
import com.axway.jmb.builders.Fields;

/**
 * Class generator for Message Builder's Record type.
 *
 * @author Florin Potera
 */

public class RecordClassBuilder extends ClassWriter {
	private String javaClassRecordName;
	private String javaFullyQuallifiedOuterClass;
	private int access;
	private AdviceAdapter constructor;
	
	public RecordClassBuilder( int params, String messageBuilderRecordName, String javaFullyQuallifiedOuterClass, int access) {
		super(params);

		javaClassRecordName = Utils.getJavaRecordTypeName(messageBuilderRecordName);
		this.javaFullyQuallifiedOuterClass = javaFullyQuallifiedOuterClass;
		this.access = access;
		
		visit(V1_7, access, 
				Utils.getInternalFQClassName( getRecordClassFullyQualifiedName() ), 
				null, "java/lang/Object", null);
		
		visitOuterClass( Utils.getInternalFQClassName( javaFullyQuallifiedOuterClass ), 
				null, null);//de vazut pentru recordori definite in metode       -  TODO
		
		constructor = Constructors.startDefault( this, Type.getType(Object.class).getInternalName() );			
	}
	
	
	public String getRecordClassFullyQualifiedName () {
		return javaFullyQuallifiedOuterClass + "$" + javaClassRecordName;
	}
	
	public String getJavaClassRecordName() {
		return javaClassRecordName;
	}


	public int getAccess() {
		return access;
	}
	
	public void addField (int access, ClassField field) {
		Fields.addFieldToClass(access, field, constructor, this, getRecordClassFullyQualifiedName () );
	}
	
	public void visitEnded() {
		Constructors.endDefault( constructor, 10, 10 );
		visitEnd();
	}
}




