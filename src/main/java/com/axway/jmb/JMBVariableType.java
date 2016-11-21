package com.axway.jmb;

import org.objectweb.asm.Type;

public enum JMBVariableType {
	INTEGER			(Type.getObjectType( "java/lang/Long" )), 
	FLOAT			(Type.getObjectType( "java/lang/Double" )), 
	STRING			(Type.getObjectType( "java/lang/String" )), 
	FIXED_STRING	(Type.getType(char[][].class)),
	DATE			(Type.getObjectType( "java/util/Date" )),
	RECORD			(null);
	
	private final Type jvmType;
	
	JMBVariableType( Type jvmType ) {
		this.jvmType = jvmType;
	}

	public Type getJvmType() {
		return jvmType;
	}
	
	public Type getArrayJvmType(int dimensions) {
		return Type.getObjectType( new String(new char[dimensions]).replace("\0", "[") + jvmType.getDescriptor());
	}	
}
