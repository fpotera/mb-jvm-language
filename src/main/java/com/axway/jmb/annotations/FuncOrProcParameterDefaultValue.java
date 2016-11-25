package com.axway.jmb.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FuncOrProcParameterDefaultValue {
	public enum Value {
		EXISTS, NONE
	}
	
	Value value();
	String defaultValue() default "";
}
