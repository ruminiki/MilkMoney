package br.com.milkmoney.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javafx.util.StringConverter;

@SuppressWarnings("rawtypes")
public class CustomStringConverter extends StringConverter {

	String attrToGet;
	
	public CustomStringConverter(String attrToGet) {
		this.attrToGet = attrToGet;
	}
	
	@Override
	public String toString(Object object) {
		if (object == null) {
			return null;
		} else {
			Method method;
			
			String methodName;
			if ( !attrToGet.startsWith("get")){
				methodName = "get"+attrToGet.substring(0, 1).toUpperCase()+attrToGet.substring(1, attrToGet.length());
			}else{
				methodName = attrToGet;
			}
			
			try {
				method = object.getClass().getMethod(methodName);
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
