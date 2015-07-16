package br.com.milksys.validation;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoAnimal;
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
			/*if ( cobertura.getTouro().getIdade() < 18 ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"O reprodutor selecionado possui apenas " + cobertura.getTouro().getIdade() + " meses de idade. " +
						"No entanto � necess�rio que tenha pelo menos 18 meses. " +
						"Verifique se existe um erro no cadastro do animal.");
			}*/
			/*if ( cobertura.getFemea().getPaiMontaNatural() != null &&
					cobertura.getFemea().getPaiMontaNatural().getId() == cobertura.getTouro().getId() ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"O reprodutor selecionado � o pai da f�mea " + cobertura.getFemea().getNumeroNome() + " n�o sendo poss�vel registrar a cobertura.");
			}*/
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
	public static void validaFemeaSelecionada(Cobertura cobertura, List<Cobertura> coberturasAnimal){
		
		if ( cobertura.getFemea() != null && !cobertura.getFemea().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"O animal selecionado para a cobertura deve ser uma f�mea.");
		}
		
		/*if ( cobertura.getFemea().getIdade() < 18 ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A f�mea selecionada possui apenas " + cobertura.getFemea().getIdade() + " meses de idade. " +
					"No entanto � necess�rio que tenha pelo menos 18 meses. " +
					"Verifique se existe um erro no cadastro do animal.");
		}*/
		
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
	 * Valida o registro de ensemina��o na cobertura.
	 * 
	 * @param cobertura
	 * @param aumentouQuantidadeDosesUtilizadas
	 */
	public static void validaEnseminacaoArtificial(Cobertura cobertura, boolean aumentouQuantidadeDosesUtilizadas){
		
		if ( cobertura.getTipoCobertura().equals(TipoCobertura.ENSEMINACAO_ARTIFICIAL) ){
			//SEMEN N�O PODE SER NULO
			if ( cobertura.getSemen() == null ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"Por favor, infome o campo [s�men] para continuar.");
			}
			
			//se for novo registro ou 
			//se durante a atera��o de um registro o usu�rio aumentar a quantidade de doses
			//sendo que n�o h� doses suficientes dispon�veis
			if ( cobertura.getId() <= 0 || aumentouQuantidadeDosesUtilizadas ){
				if ( cobertura.getQuantidadeDosesUtilizadas() > cobertura.getSemen().getQuantidadeDisponivel().intValue() ){
					throw new ValidationException(CAMPO_OBRIGATORIO, 
							"O s�men selecionado n�o possui quantidade suficiente dispon�vel. "
							+ "Por favor, verifique se a quantidade de doses informada est� correta ou selecione outro s�men.");
				}
			}
			
			//DEVE SER INFORMADA A QUANTIDADE DE DOSES PARA FAZER O CONTROLE SOBRE O ESTOQUE
			if ( cobertura.getQuantidadeDosesUtilizadas() <= 0 ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"Por favor, infome o campo [quantidade doses utilizadas] para continuar.");
			}
		}
		
		if ( cobertura.getNomeResponsavel() == null || cobertura.getNomeResponsavel().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome o campo [respons�vel pela ensemina��o] para continuar.");
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
	
	
	public static void validateRegistroConfirmacaoPrenhez(Cobertura cobertura){
		//===============PREENCHIMENTO DOS CAMPOS===========
		if ( cobertura.getSituacaoConfirmacaoPrenhez() == null || cobertura.getSituacaoConfirmacaoPrenhez().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome a situa��o da cobertura para continuar.");
		}
		
		if ( cobertura.getDataConfirmacaoPrenhez() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, informe a data do primeiro toque para continuar.");
		}
		
		if ( cobertura.getDataConfirmacaoPrenhez().before(cobertura.getData()) ||
				DateUtil.isSameDate(cobertura.getDataConfirmacaoPrenhez(), cobertura.getData())){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do primeiro toque n�o pode ser menor ou igual a data da cobertura.");
		}
		
		if ( cobertura.getDataReconfirmacaoPrenhez() != null && !cobertura.getSituacaoReconfirmacaoPrenhez().isEmpty() &&
				(cobertura.getDataConfirmacaoPrenhez().after(cobertura.getDataReconfirmacaoPrenhez()) || DateUtil.isSameDate(cobertura.getDataConfirmacaoPrenhez(), cobertura.getDataReconfirmacaoPrenhez())) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do primeiro toque n�o pode ser maior ou igual a data da reconfirma��o.");
		}
		
		if ( cobertura.getDataRepeticaoCio() != null  &&
				cobertura.getDataConfirmacaoPrenhez().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data do primeiro toque n�o pode ser maior que a data da repeti��o do cio.");
		}
		
	}
	
	public static void validateRegistroReconfirmacaoPrenhez(Cobertura cobertura){
		//===============PREENCHIMENTO DOS CAMPOS===========
		if ( cobertura.getSituacaoReconfirmacaoPrenhez() == null || cobertura.getSituacaoReconfirmacaoPrenhez().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome a situa��o da cobertura para continuar.");
		}
		
		if ( cobertura.getDataReconfirmacaoPrenhez() == null ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, informe a data da reconfirma��o para continuar.");
		}
		
		if ( cobertura.getDataReconfirmacaoPrenhez().before(cobertura.getData()) ||
				DateUtil.isSameDate(cobertura.getDataReconfirmacaoPrenhez(), cobertura.getData())){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da reconfirma��o n�o pode ser menor ou igual a data da cobertura.");
		}
		
		if ( cobertura.getDataConfirmacaoPrenhez() == null || 
				cobertura.getSituacaoConfirmacaoPrenhez() == null || cobertura.getSituacaoConfirmacaoPrenhez().isEmpty() ){ 
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, registre o primeiro toque antes de registrar a reconfirma��o.");
		}
		
		if ( cobertura.getDataConfirmacaoPrenhez() != null && !cobertura.getSituacaoConfirmacaoPrenhez().isEmpty() &&
				(cobertura.getDataConfirmacaoPrenhez().after(cobertura.getDataReconfirmacaoPrenhez()) || DateUtil.isSameDate(cobertura.getDataConfirmacaoPrenhez(), cobertura.getDataReconfirmacaoPrenhez())) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da reconfirma��o n�o pode ser menor ou igual a data do primeiro toque.");
		}
		
		if ( cobertura.getDataRepeticaoCio() != null  &&
				cobertura.getDataReconfirmacaoPrenhez().after(cobertura.getDataRepeticaoCio()) ){
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
		
		if ( cobertura.getDataConfirmacaoPrenhez() != null &&
				cobertura.getDataConfirmacaoPrenhez().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da repeti��o do cio n�o pode ser menor que a data do primeiro toque.");
		}
		
		if ( cobertura.getDataReconfirmacaoPrenhez() != null &&
				cobertura.getDataReconfirmacaoPrenhez().after(cobertura.getDataRepeticaoCio()) ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"A data da repeti��o do cio n�o pode ser menor que a data da reconfirma��o.");
		}

	}
	
	public static void validaSituacaoAnimal(Animal animal){
		
		if ( animal.getSituacaoAnimal() != null ){
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado teve a morte registrada. Por favor, selecione outro animal para continuar.");
			}
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "O animal selecionado teve a venda registrada. Por favor, selecione outro animal para continuar.");
			}
		}
		
	}

	/*
	 * Cada cobertura que tenha parto deve ter um intervalo de pelo menos 21 dias antes da cobertura que teve parto
	 * (supondo que ela pode ter tido repeti��o) e 21 dias ap�s o parto. 
	 */
	public static void validaSobreposicaoCoberturas(Cobertura cobertura, Cobertura lastCobertura) {
		
		if ( lastCobertura != null && lastCobertura.getParto() != null ){
			
			//verifica se a cobertura est� sendo cacastrada antes de 30 dias ap�s o �ltimo parto
			if ( cobertura.getData().before( DateUtil.asDate(DateUtil.asLocalDate(lastCobertura.getParto().getData()).plusDays(21)) ) &&
					cobertura.getData().after( DateUtil.asDate(DateUtil.asLocalDate(lastCobertura.getData()).minusDays(21)) )){
				throw new ValidationException(REGRA_NEGOCIO, "N�o � poss�vel registrar a cobertura para o dia " + DateUtil.format(cobertura.getData()) + ", pois "
						+ "a �ltima cobertura do animal foi no dia " + DateUtil.format(lastCobertura.getData()) + " e o �ltimo parto no dia " + DateUtil.format(lastCobertura.getParto().getData()) + ". "
								+ "� necess�rio que tenha um intervalo de pelo menos 21 dias entre uma cobertura e outra e um parto e uma cobertura.");
			}
		}
		
	}
	
	
}
