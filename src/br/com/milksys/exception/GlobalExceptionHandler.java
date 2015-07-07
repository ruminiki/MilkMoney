package br.com.milksys.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;

import br.com.milksys.components.CustomAlert;

public class GlobalExceptionHandler implements UncaughtExceptionHandler  
{
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		
		if ( e.getCause() instanceof InvocationTargetException  ){
			InvocationTargetException exception = (InvocationTargetException) e.getCause();
			
			if ( exception.getTargetException() instanceof ValidationException ){
				CustomAlert.mensagemAlerta(((ValidationException)e).getTipo(), ((ValidationException)e).getMessage());	
			}else{
				e.printStackTrace();
			}
			
			//....
			
		}else{
			//CustomAlert.mensagemAlerta("ERRO INESPERADO", e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}