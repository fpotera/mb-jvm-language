// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb.builders;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

import com.axway.jmb.ClassField;
import com.axway.jmb.JMBVariableType;

/**
 * Bytecode generator for java class fields.
 *
 * @author Florin Potera
 */

public class Fields {
	public static void addFieldToClass(int access, ClassField field, AdviceAdapter constructor, ClassWriter clazz, String classFullyQualifiedName ){
		if ( field.isArray() ) {
			clazz.visitField( access, field.getName(), field.getType().getArrayJvmType( field.getArrayDimension() ).getDescriptor(), null, null);	

			constructor.push( field.getArrayDimension() );
			constructor.newArray( field.getType().getArrayJvmType( field.getArrayDimension() ) );			
			constructor.putField(Type.getType( classFullyQualifiedName ), field.getName(), 
					field.getType().getArrayJvmType( field.getArrayDimension() ));
		}
		else {
			if ( field.getType() == JMBVariableType.RECORD ) {
				 clazz.visitField( access, field.getName(), field.getRecordType().getDescriptor(), null, null);	

				constructor.newInstance( field.getRecordType() );
				constructor.dup();
				constructor.invokeConstructor( field.getRecordType(), Method.getMethod("void <init>()") );
				
				constructor.putField(Type.getType( classFullyQualifiedName ), field.getName(), 
						field.getRecordType());
			}
			else {
				 clazz.visitField( access, field.getName(), field.getType().getJvmType().getDescriptor(), null, null);
			}
			if ( field.isInitializationAvailable() ) {
				constructor.putField(Type.getType( classFullyQualifiedName ), field.getName(), 
						field.getType().getJvmType());
			}
		}
	}
}
