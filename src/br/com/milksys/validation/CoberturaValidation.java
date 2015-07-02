package br.com.milksys.validation;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.TipoCobertura;
import br.com.milksys.util.DateUtil;

public class CoberturaValidation extends Validator {
	
	public static void validate(Cobertura cobertura) {
	
		Validator.validate(cobertura);
		
		//===============F�MEA========================
		if ( !cobertura.getFemea().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"O animal selecionado para a cobertura deve ser uma f�mea.");
		}
		
		if ( cobertura.getFemea().getIdade() < 18 ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A f�mea selecionada possui apenas " + cobertura.getFemea().getIdade() + " meses de idade. " +
					"No entanto � necess�rio que tenha pelo menos 18 meses. " +
					"Verifique se existe um erro no cadastro do animal.");
		}
		
		if ( cobertura.getFemea().getCoberturas() != null && cobertura.getFemea().getCoberturas().size() > 0 ){
			/*
			* Uma vaca n�o pode ter a cobertura cadastrada se:
			* 1. Foi coberta a menos de 30 dias;
			* 2. Tiver uma cobertura com situa��o: PRENHA, ou INDEFINIDA; 
			* Obs: Sempre que a vaca repetir de cio o usu�rio deve marcar a situa��o da cobertura como VAZIA;
			* No caso de registro de parto a situa��o muda para PARIDA.
			*/
			for ( Cobertura c : cobertura.getFemea().getCoberturas() ){
				if ( c.getId() != cobertura.getId() && 
						(c.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ||
						c.getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA)) ){
					
					throw new ValidationException(CAMPO_OBRIGATORIO,
							"A f�mea [" + c.getFemea().getNumeroNome()+"] possui uma cobertura registrada no dia " + 
							DateUtil.format(c.getData()) +	" com situa��o " + c.getSituacaoCobertura() + ". " +
							"� necess�rio finalizar aquela cobertura, indicando se houve repeti��o de cio ou parto, para ent�o registrar uma nova cobertura.");
					
				}
			}
			
		}
		//===============TIPO COBERTURA===============
		if ( cobertura.getTipoCobertura() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome o campo [tipo de cobertura] para continuar.");
		}
		
		//===============MONTA NATURAL===============
		if ( cobertura.getTipoCobertura().equals(TipoCobertura.MONTA_NATURAL) ){
			//TOURO N�O PODE SER NULO
			if ( cobertura.getTouro() == null ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"Por favor, infome o campo [reprodutor] para continuar.");
			}
			//DEVE SER DO SEXO MACHO
			if ( !cobertura.getTouro().getSexo().equals(Sexo.MACHO) ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"O reprodutor selecionado para a cobertura deve ser um macho.");
			}
			if ( cobertura.getTouro().getIdade() < 18 ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"O reprodutor selecionado possui apenas " + cobertura.getTouro().getIdade() + " meses de idade. " +
						"No entanto � necess�rio que tenha pelo menos 18 meses. " +
						"Verifique se existe um erro no cadastro do animal.");
			}
		}
		
		//===============ENSEMINA��O ARTIFICIAL========
		if ( cobertura.getTipoCobertura().equals(TipoCobertura.ENSEMINACAO_ARTIFICIAL) ){
			//SEMEN N�O PODE SER NULO
			if ( cobertura.getSemen() == null ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"Por favor, infome o campo [s�men] para continuar.");
			}
			//DEVE SER INFORMADA A QUANTIDADE DE DOSES PARA FAZER O CONTROLE SOBRE O ESTOQUE
			if ( cobertura.getQuantidadeDosesSemen() <= 0 ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"Por favor, infome o campo [quantidade doses utilizadas] para continuar.");
			}
		}
		
		//===============RESPONS�VEL COBERTURA===========
		if ( cobertura.getNomeResponsavel() == null || cobertura.getNomeResponsavel().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome o campo [respons�vel pela ensemina��o] para continuar.");
		}
		
	}
	
	
	public static void validateRegistroPrimeiroToque(Cobertura cobertura){
		//===============PREENCHIMENTO DOS CAMPOS===========
		if ( cobertura.getResultadoPrimeiroToque() == null || cobertura.getResultadoPrimeiroToque().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome a situa��o da cobertura para continuar.");
		}
		
		if ( cobertura.getDataPrimeiroToque() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, informe a data do primeiro toque para continuar.");
		}
		
		if ( cobertura.getDataPrimeiroToque().before(cobertura.getData()) ||
				DateUtil.isSameDate(cobertura.getDataPrimeiroToque(), cobertura.getData())){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do primeiro toque n�o pode ser menor ou igual a data da cobertura.");
		}
		
		if ( cobertura.getDataReconfirmacao() != null && !cobertura.getResultadoReconfirmacao().isEmpty() &&
				(cobertura.getDataPrimeiroToque().after(cobertura.getDataReconfirmacao()) || DateUtil.isSameDate(cobertura.getDataPrimeiroToque(), cobertura.getDataReconfirmacao())) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do primeiro toque n�o pode ser maior ou igual a data da reconfirma��o.");
		}
		
		if ( cobertura.getDataRepeticaoCio() != null  &&
				cobertura.getDataPrimeiroToque().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do primeiro toque n�o pode ser maior que a data da repeti��o do cio.");
		}
		
	}
	
	public static void validateRegistroReconfirmacao(Cobertura cobertura){
		//===============PREENCHIMENTO DOS CAMPOS===========
		if ( cobertura.getResultadoReconfirmacao() == null || cobertura.getResultadoReconfirmacao().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome a situa��o da cobertura para continuar.");
		}
		
		if ( cobertura.getDataReconfirmacao() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, informe a data da reconfirma��o para continuar.");
		}
		
		if ( cobertura.getDataReconfirmacao().before(cobertura.getData()) ||
				DateUtil.isSameDate(cobertura.getDataReconfirmacao(), cobertura.getData())){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da reconfirma��o n�o pode ser menor ou igual a data da cobertura.");
		}
		
		if ( cobertura.getDataPrimeiroToque() == null || 
				cobertura.getResultadoPrimeiroToque() == null || cobertura.getResultadoPrimeiroToque().isEmpty() ){ 
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, registre o primeiro toque antes de registrar a reconfirma��o.");
		}
		
		if ( cobertura.getDataPrimeiroToque() != null && !cobertura.getResultadoPrimeiroToque().isEmpty() &&
				(cobertura.getDataPrimeiroToque().after(cobertura.getDataReconfirmacao()) || DateUtil.isSameDate(cobertura.getDataPrimeiroToque(), cobertura.getDataReconfirmacao())) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da reconfirma��o n�o pode ser menor ou igual a data do primeiro toque.");
		}
		
		if ( cobertura.getDataRepeticaoCio() != null  &&
				cobertura.getDataReconfirmacao().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da reconfirma��o n�o pode ser maior que a data da repeti��o do cio.");
		}
		
	}
	
	public static void validateRegistroRepeticaoCio(Cobertura cobertura){
		
		//===============PREENCHIMENTO DOS CAMPOS===========
		if ( cobertura.getDataRepeticaoCio() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, informe a data da repeti��o do cio para continuar.");
		}
		
		if ( cobertura.getDataRepeticaoCio().before(cobertura.getData()) ||
				DateUtil.isSameDate(cobertura.getDataRepeticaoCio(), cobertura.getData())){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da repeti��o do cio n�o pode ser menor ou igual a data da cobertura.");
		}
		
		if ( cobertura.getSituacaoCobertura() == null || cobertura.getSituacaoCobertura().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome a situa��o da cobertura para continuar.");
		}
		
		if ( !cobertura.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"No registro de repeti��o de cio a situa��o escolhida deve ser [" + SituacaoCobertura.REPETIDA+"]");
		}
		
		if ( cobertura.getDataPrimeiroToque() != null &&
				cobertura.getDataPrimeiroToque().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da repeti��o do cio n�o pode ser menor que a data do primeiro toque.");
		}
		
		if ( cobertura.getDataReconfirmacao() != null &&
				cobertura.getDataReconfirmacao().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da repeti��o do cio n�o pode ser menor que a data da reconfirma��o.");
		}

	}
	
	
}
