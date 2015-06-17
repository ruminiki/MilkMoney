package br.com.milksys.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javafx.util.StringConverter;

@SuppressWarnings("rawtypes")
public class CustomStringConverter extends StringConverter {

	String methodGet;
	
	public CustomStringConverter(String methodGet) {
		this.methodGet = methodGet;
	}
	
	@Override
	public String toString(Object object) {
		if (object == null) {
			return null;
		} else {
			Method method;
			try {
				method = object.getClass().getMethod(methodGet);
				return (String) method.invoke(object);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
				return "";
			}
		}
	}

	@Override
	public Object fromString(String string) {
		return null;
	}

}
