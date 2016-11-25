package com.axway.jmb.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FuncOrProcParameterType {
	public enum Type {
		PRIMITIVE, RECORD
	}
	
	Type type();
	String recordType() default "";
}
