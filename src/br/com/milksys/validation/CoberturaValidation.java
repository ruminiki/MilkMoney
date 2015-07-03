package br.com.milksys.validation;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.TipoCobertura;
import br.com.milksys.util.DateUtil;

public class CoberturaValidation extends Validator {
	
	public static void validate(Cobertura cobertura) {
	
		Validator.validate(cobertura);
		
		if ( DateUtil.after(cobertura.getData(), new Date()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da cobertura n�o pode ser maior que a data atual.");
		}
		
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
		
		
		if ( cobertura.getTipoCobertura() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome o campo [tipo de cobertura] para continuar.");
		}
		
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
		
		if ( cobertura.getNomeResponsavel() == null || cobertura.getNomeResponsavel().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome o campo [respons�vel pela ensemina��o] para continuar.");
		}
		
	}
	
	
	/*
	 * Uma vaca n�o pode ter a cobertura cadastrada/alterada se:
	 * 1. Foi coberta a menos de 21 dias;
	 * 2. Tiver outra cobertura com situa��o: PRENHA, ou INDEFINIDA; 
	 * Obs: Sempre que a vaca repetir de cio o usu�rio deve marcar a situa��o da cobertura como VAZIA ou indicar a repeti��o;
	 * No caso de registro de parto a situa��o muda para PARIDA.
	 * @param cobertura
	 */
	public static void validaCoberturasAnimal(Cobertura cobertura, List<Cobertura> coberturasAnimal){
		if ( coberturasAnimal != null && coberturasAnimal.size() > 0 ){
			
			for ( Cobertura c : coberturasAnimal ){
				//se n�o for a mesma cobertura
				if ( c.getId() != cobertura.getId() ){
					
					long diasEntreCoberturas = ChronoUnit.DAYS.between(DateUtil.asLocalDate(c.getData()), DateUtil.asLocalDate(cobertura.getData()));
					
					if ( Math.abs(diasEntreCoberturas) < 21 ){
						throw new ValidationException(CAMPO_OBRIGATORIO, "O intervalo entre uma cobertura e outra deve ser de pelo menos 21 dias. "
								+ "A f�mea [" + c.getFemea().getNumeroNome()+"] teve cobertura registrada no dia " + DateUtil.format(c.getData()) + ". "
								+ "Verifique se aquela data est� correta. Se for necess�rio corrija-a para ent�o ser poss�vel registrar essa cobertura.");
					}
					
					validaSituacoesCoberturasDoAnimal(cobertura, coberturasAnimal);
					
				}
			}
		}
	}
	
	/**
	 * N�o deve ser permitido que seja mantido duas coberturas com status PRENHA ou INDEFINIDA.
	 * 
	 * Toda cobertura precisa ser finalizada de alguma forma, ou indicando a repeti��o, o vazio ou o parto
	 * as situa��es que indicam que a cobertura est� finalizada s�o
	 * PARIDA, VAZIA e REPETIDA.
	 * 
	 * @param cobertura
	 */
	public static void validaSituacoesCoberturasDoAnimal(Cobertura cobertura, List<Cobertura> coberturasAnimal){
		
		if ( coberturasAnimal != null && coberturasAnimal.size() > 0 ){
			
			for ( Cobertura c : coberturasAnimal ){
				//se n�o for a mesma cobertura
				if ( c.getId() != cobertura.getId() ){
					
					if ( (c.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) || c.getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA)) &&
						 (cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) || cobertura.getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA)) ){
						
						throw new ValidationException(CAMPO_OBRIGATORIO,
								"A f�mea [" + c.getFemea().getNumeroNome()+"] possui uma cobertura registrada no dia " + 
								DateUtil.format(c.getData()) +	" com situa��o " + c.getSituacaoCobertura() + ". " +
								"� necess�rio finalizar aquela cobertura, indicando se houve repeti��o de cio ou parto, para ent�o registrar/alterar essa cobertura. " +
								"N�o � poss�vel haver duas coberturas com situa��es PRENHA ou INDEFINIDA ao mesmo tempo para um mesmo animal.");
					}
					
				}
			}
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
