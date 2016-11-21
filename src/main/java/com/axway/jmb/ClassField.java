// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

import org.objectweb.asm.Type;

/**
 * Field of class support class for parsing stage.
 *
 * @author Florin Potera
 */

public class ClassField {
	
	private String name;
	private JMBVariableType type;
	private int fixedStringLength;
	private Type recordType;
	private int arrayDimension = 0;
	private boolean initializationAvailable = false;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public JMBVariableType getType() {
		return type;
	}
	
	public void setType(JMBVariableType type) {
		this.type = type;
	}
	
	public int getFixedStringLength() {
		return fixedStringLength;
	}
	
	public void setFixedStringLength(int fixedStringLength) {
		this.fixedStringLength = fixedStringLength;
	}
	
	public int getArrayDimension() {
		return arrayDimension;
	}
	
	public void setArrayDimension(int arrayDimension) {
		this.arrayDimension = arrayDimension;
	}
	
	public boolean isArray() {
		return arrayDimension != 0;
	}
	
	public boolean isInitializationAvailable() {
		return initializationAvailable;
	}
	
	public void setInitializationAvailable(boolean initializationAvailable) {
		this.initializationAvailable = initializationAvailable;
	}

	public Type getRecordType() {
		return recordType;
	}

	public void setRecordType(Type recordType) {
		this.recordType = recordType;
	}
	
	
}
