// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

/**
 * Support for local variables definition in parsing stage.
 *
 * @author Florin Potera
 */

public class LocalVariable extends ClassField {

	private int arrayPosition; // position in class file
	
	public int getArrayPosition() {
		return arrayPosition;
	}
	
	public void setArrayPosition(int arrayPosition) {
		this.arrayPosition = arrayPosition;
	}
	
}
