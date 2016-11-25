package com.axway.jmb.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.axway.jmb.ProcedureParameterIOType;

@Retention(RetentionPolicy.RUNTIME)
public @interface ProcedureParameter {
	ProcedureParameterIOType paramIOType();
	FuncOrProcParameterType paramType();
	FuncOrProcParameterDefaultValue defaulValue();
	ProcParameterNoiseWord noiseWord();
}
