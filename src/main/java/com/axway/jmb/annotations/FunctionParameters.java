package com.axway.jmb.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionParameters {
	FunctionParameter[] value = new FunctionParameter[] {};
}
