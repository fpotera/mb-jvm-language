// Copyright (c) Axway Inc. All Rights Reserved.
// Please refer to the file "LICENSE" for further important copyright
// and licensing information.  Please also refer to the documentation
// for additional copyright notices.
package com.axway.jmb;

import static org.objectweb.asm.Opcodes.ACC_STATIC;

/**
 * Class for Java MessageBuilder executable module generator.
 *
 * @author Florin Potera
 */

public class ExecutableClassBuilder extends ClassBuilder {

	public ExecutableClassBuilder(int params, String moduleFullyQualifiedName) {
		super(params, moduleFullyQualifiedName);
	}

	@Override
	protected int getClassMembersAccess() {
		return ACC_STATIC;
	}

	
}
