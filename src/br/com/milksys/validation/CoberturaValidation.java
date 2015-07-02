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
		
		//===============FÊMEA========================
		if ( !cobertura.getFemea().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"O animal selecionado para a cobertura deve ser uma fêmea.");
		}
		
		if ( cobertura.getFemea().getIdade() < 18 ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A fêmea selecionada possui apenas " + cobertura.getFemea().getIdade() + " meses de idade. " +
					"No entanto é necessário que tenha pelo menos 18 meses. " +
					"Verifique se existe um erro no cadastro do animal.");
		}
		
		if ( cobertura.getFemea().getCoberturas() != null && cobertura.getFemea().getCoberturas().size() > 0 ){
			/*
			* Uma vaca não pode ter a cobertura cadastrada se:
			* 1. Foi coberta a menos de 30 dias;
			* 2. Tiver uma cobertura com situação: PRENHA, ou INDEFINIDA; 
			* Obs: Sempre que a vaca repetir de cio o usuário deve marcar a situação da cobertura como VAZIA;
			* No caso de registro de parto a situação muda para PARIDA.
			*/
			for ( Cobertura c : cobertura.getFemea().getCoberturas() ){
				if ( c.getId() != cobertura.getId() && 
						(c.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ||
						c.getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA)) ){
					
					throw new ValidationException(CAMPO_OBRIGATORIO,
							"A fêmea [" + c.getFemea().getNumeroNome()+"] possui uma cobertura registrada no dia " + 
							DateUtil.format(c.getData()) +	" com situação " + c.getSituacaoCobertura() + ". " +
							"É necessário finalizar aquela cobertura, indicando se houve repetição de cio ou parto, para então registrar uma nova cobertura.");
					
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
			//TOURO NÃO PODE SER NULO
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
						"No entanto é necessário que tenha pelo menos 18 meses. " +
						"Verifique se existe um erro no cadastro do animal.");
			}
		}
		
		//===============ENSEMINAÇÃO ARTIFICIAL========
		if ( cobertura.getTipoCobertura().equals(TipoCobertura.ENSEMINACAO_ARTIFICIAL) ){
			//SEMEN NÃO PODE SER NULO
			if ( cobertura.getSemen() == null ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"Por favor, infome o campo [sêmen] para continuar.");
			}
			//DEVE SER INFORMADA A QUANTIDADE DE DOSES PARA FAZER O CONTROLE SOBRE O ESTOQUE
			if ( cobertura.getQuantidadeDosesSemen() <= 0 ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"Por favor, infome o campo [quantidade doses utilizadas] para continuar.");
			}
		}
		
		//===============RESPONSÁVEL COBERTURA===========
		if ( cobertura.getNomeResponsavel() == null || cobertura.getNomeResponsavel().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome o campo [responsável pela enseminação] para continuar.");
		}
		
	}
	
	
	public static void validateRegistroPrimeiroToque(Cobertura cobertura){
		//===============PREENCHIMENTO DOS CAMPOS===========
		if ( cobertura.getResultadoPrimeiroToque() == null || cobertura.getResultadoPrimeiroToque().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome a situação da cobertura para continuar.");
		}
		
		if ( cobertura.getDataPrimeiroToque() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, informe a data do primeiro toque para continuar.");
		}
		
		if ( cobertura.getDataPrimeiroToque().before(cobertura.getData()) ||
				DateUtil.isSameDate(cobertura.getDataPrimeiroToque(), cobertura.getData())){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do primeiro toque não pode ser menor ou igual a data da cobertura.");
		}
		
		if ( cobertura.getDataReconfirmacao() != null && !cobertura.getResultadoReconfirmacao().isEmpty() &&
				(cobertura.getDataPrimeiroToque().after(cobertura.getDataReconfirmacao()) || DateUtil.isSameDate(cobertura.getDataPrimeiroToque(), cobertura.getDataReconfirmacao())) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do primeiro toque não pode ser maior ou igual a data da reconfirmação.");
		}
		
		if ( cobertura.getDataRepeticaoCio() != null  &&
				cobertura.getDataPrimeiroToque().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do primeiro toque não pode ser maior que a data da repetição do cio.");
		}
		
	}
	
	public static void validateRegistroReconfirmacao(Cobertura cobertura){
		//===============PREENCHIMENTO DOS CAMPOS===========
		if ( cobertura.getResultadoReconfirmacao() == null || cobertura.getResultadoReconfirmacao().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome a situação da cobertura para continuar.");
		}
		
		if ( cobertura.getDataReconfirmacao() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, informe a data da reconfirmação para continuar.");
		}
		
		if ( cobertura.getDataReconfirmacao().before(cobertura.getData()) ||
				DateUtil.isSameDate(cobertura.getDataReconfirmacao(), cobertura.getData())){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da reconfirmação não pode ser menor ou igual a data da cobertura.");
		}
		
		if ( cobertura.getDataPrimeiroToque() == null || 
				cobertura.getResultadoPrimeiroToque() == null || cobertura.getResultadoPrimeiroToque().isEmpty() ){ 
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, registre o primeiro toque antes de registrar a reconfirmação.");
		}
		
		if ( cobertura.getDataPrimeiroToque() != null && !cobertura.getResultadoPrimeiroToque().isEmpty() &&
				(cobertura.getDataPrimeiroToque().after(cobertura.getDataReconfirmacao()) || DateUtil.isSameDate(cobertura.getDataPrimeiroToque(), cobertura.getDataReconfirmacao())) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da reconfirmação não pode ser menor ou igual a data do primeiro toque.");
		}
		
		if ( cobertura.getDataRepeticaoCio() != null  &&
				cobertura.getDataReconfirmacao().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da reconfirmação não pode ser maior que a data da repetição do cio.");
		}
		
	}
	
	public static void validateRegistroRepeticaoCio(Cobertura cobertura){
		
		//===============PREENCHIMENTO DOS CAMPOS===========
		if ( cobertura.getDataRepeticaoCio() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, informe a data da repetição do cio para continuar.");
		}
		
		if ( cobertura.getDataRepeticaoCio().before(cobertura.getData()) ||
				DateUtil.isSameDate(cobertura.getDataRepeticaoCio(), cobertura.getData())){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da repetição do cio não pode ser menor ou igual a data da cobertura.");
		}
		
		if ( cobertura.getSituacaoCobertura() == null || cobertura.getSituacaoCobertura().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome a situação da cobertura para continuar.");
		}
		
		if ( !cobertura.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"No registro de repetição de cio a situação escolhida deve ser [" + SituacaoCobertura.REPETIDA+"]");
		}
		
		if ( cobertura.getDataPrimeiroToque() != null &&
				cobertura.getDataPrimeiroToque().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da repetição do cio não pode ser menor que a data do primeiro toque.");
		}
		
		if ( cobertura.getDataReconfirmacao() != null &&
				cobertura.getDataReconfirmacao().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da repetição do cio não pode ser menor que a data da reconfirmação.");
		}

	}
	
	
}
