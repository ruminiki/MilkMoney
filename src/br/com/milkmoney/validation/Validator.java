package br.com.milkmoney.validation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.AbstractEntity;

public class Validator {

	public static final String VALIDACAO_FORMULARIO = "Opss!! Alguma coisa n�o est� certa!";
	public static final String CAMPO_OBRIGATORIO = "Opss!! Existem campos obrigat�rios!";
	public static final String REGRA_NEGOCIO = "Opss!! Encontramos uma restri��o!";
	
	public static void validate(AbstractEntity entity) {
		for (Method method : entity.getClass().getDeclaredMethods()) {
			FieldRequired annotation = method.getAnnotation(FieldRequired.class);
			if (annotation != null) {
				try {

					Object result = method.invoke(entity);

					if (result == null || (result instanceof String && ((String) result).isEmpty())) {
						throw new ValidationException(CAMPO_OBRIGATORIO, "Por favor, informe o campo [" + annotation.message() + "] para continuar.");
					}

				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
