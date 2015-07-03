package br.com.milksys.validation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.milksys.components.FieldRequired;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.AbstractEntity;

public class Validator {

	public static final String VALIDACAO_FORMULARIO = "Validação de formulário";
	public static final String CAMPO_OBRIGATORIO = "Campo Obrigatório";
	public static final String REGRA_NEGOCIO = "Regra Negócio";
	
	public static void validate(AbstractEntity entity) {
		for (Method method : entity.getClass().getDeclaredMethods()) {
			FieldRequired annotation = method.getAnnotation(FieldRequired.class);
			if (annotation != null) {
				try {

					Object result = method.invoke(entity);

					if (result == null || (result instanceof String && ((String) result).isEmpty())) {
						throw new ValidationException(CAMPO_OBRIGATORIO, "Por favor, infome o campo [" + annotation.message() + "] para continuar.");
					}

				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
