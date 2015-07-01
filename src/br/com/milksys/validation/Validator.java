package br.com.milksys.validation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.FieldRequired;
import br.com.milksys.model.AbstractEntity;

public class Validator {

	public void validate(AbstractEntity entity) {
		for (Method method : entity.getClass().getDeclaredMethods()) {
			FieldRequired annotation = method.getAnnotation(FieldRequired.class);
			if (annotation != null) {
				try {

					Object result = method.invoke(entity);

					if (result == null || (result instanceof String && ((String) result).isEmpty())) {
						CustomAlert.campoObrigatorio(annotation.message());
					}

				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
