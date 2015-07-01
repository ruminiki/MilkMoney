package br.com.milksys.validation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.TipoCobertura;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class CoberturaValidation extends Validator {
	
	@Before("execution(* br.com.milksys.service.CoberturaService.save(..))")
	private void validate(JoinPoint joinPoint) {
		if ( joinPoint.getArgs()[0] != null ){
			
			Cobertura cobertura = (Cobertura)joinPoint.getArgs()[0];
			
			super.validate(cobertura);
			
			if ( cobertura.getTipoCobertura() == null ){
				CustomAlert.campoObrigatorio("tipo de cobertura");
			}
			
			if ( cobertura.getTipoCobertura().equals(TipoCobertura.MONTA_NATURAL) ){
				if ( cobertura.getTouro() == null ){
					CustomAlert.campoObrigatorio("reprodutor");
				}
			}
			
			if ( cobertura.getTipoCobertura().equals(TipoCobertura.ENSEMINACAO_ARTIFICIAL) ){
				if ( cobertura.getSemen() == null ){
					CustomAlert.campoObrigatorio("sêmen");
				}
				
				if ( cobertura.getQuantidadeDosesSemen() <= 0 ){
					CustomAlert.campoObrigatorio("quantidade doses utilizadas");
				}
			}
			
			if ( cobertura.getNomeResponsavel() == null || cobertura.getNomeResponsavel().isEmpty() ){
				CustomAlert.campoObrigatorio("responsável pela enseminação");
			}
		
		}
		
	}
	
}
