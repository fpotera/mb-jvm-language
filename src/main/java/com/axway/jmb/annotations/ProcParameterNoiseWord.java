package com.axway.jmb.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ProcParameterNoiseWord {
	public enum Word {
		DEFINED, DEFAULT
	}
	
	Word value();
	String noiseWord() default "";
}
