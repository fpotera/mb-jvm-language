package com.axway.jmb;

import org.objectweb.asm.ClassWriter;

import com.axway.jmb.builders.Modules;

public class ModuleInterfaceBuilder extends ModuleBuilder {
	private static String classFullyQualifiedName;
	
	public ModuleInterfaceBuilder(int params, String moduleFullyQualifiedName, ClassWriter cw) {
		super(params, buildModuleInterfaceName ( moduleFullyQualifiedName ), cw);
	}
	
	@Override
	public void addField (int access, ClassField field) {
		super.addField(access, field);
	}

	@Override
	protected void buildModule () {
		Modules.buildInterfaceModule(this, classFullyQualifiedName );	
	}
	
	private static String buildModuleInterfaceName( String moduleFullyQualifiedName ) {
		classFullyQualifiedName = Utils.getJavaFullyQualifiedClassName( moduleFullyQualifiedName );
		classFullyQualifiedName = Utils.getInterfaceModuleName(classFullyQualifiedName).replace(".", "/");
		return classFullyQualifiedName;
	}
}
