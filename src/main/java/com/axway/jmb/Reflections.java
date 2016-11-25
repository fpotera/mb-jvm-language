package com.axway.jmb;

import java.lang.annotation.Annotation;

public class Reflections {
	public static Annotation getAnnotationOfMethod ( String javaClass, String methodName, Class<? extends Annotation> annotation ) throws CompileException {
		try {
			Class<?> clazz = Class.forName( javaClass );
			java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
			java.lang.reflect.Method foundMethod = null;
			for ( java.lang.reflect.Method method : methods ) {
				System.out.println("method:"+method.getName());
				if ( method.getName().equals(methodName) ) {
					foundMethod = method;
					break;
				}
			}
			if ( foundMethod != null ) {
				return foundMethod.getAnnotation( annotation );
			}
			else {
				throw new CompileException("Call remote statement "+javaClass+"."+methodName+"  not defined.");
			}
			
		} catch (ClassNotFoundException e) {
			throw new CompileException("Call remote statement "+javaClass+"."+methodName+"  not defined.");
		} 
	}
}
