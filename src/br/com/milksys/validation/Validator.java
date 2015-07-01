package br.com.milksys.validation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.FieldRequired;
import br.com.milksys.model.AbstractEntity;

@Aspect
public class Validator {

	@Before("execution(* br.com.milksys.service.IService.save(..))")
	public void validate(JoinPoint joinPoint) {
		System.out.println("ASPECT GERAL");

		if (joinPoint.getArgs()[0] != null) {

			AbstractEntity entity = (AbstractEntity) joinPoint.getArgs()[0];

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

}
