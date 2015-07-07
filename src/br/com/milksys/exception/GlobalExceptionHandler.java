package br.com.milksys.exception;

import javax.transaction.RollbackException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.milksys.components.CustomAlert;

@ControllerAdvice
class GlobalExceptionHandler
{
    @ExceptionHandler(TransactionSystemException.class)
    public void handleError(final TransactionSystemException tse){       
    	if(tse.getCause() != null && tse.getCause() instanceof RollbackException){
            final RollbackException re = (RollbackException) tse.getCause();

            if(re.getCause() != null && re.getCause() instanceof ConstraintViolationException)
            {
            	CustomAlert.mensagemAlerta("ERRO BANCO DE DADOS", re.getMessage());
            }
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleError(final ConstraintViolationException cve){
    	CustomAlert.mensagemAlerta("RESTRIÇÃO CHAVE", cve.getMessage());
    }
    
    @ExceptionHandler(ValidationException.class)
    public void handleError(final ValidationException vld){
    	CustomAlert.mensagemAlerta(vld.getTipo(), vld.getMessage());
    }
}