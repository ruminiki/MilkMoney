package br.com.milkmoney.validation;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.TipoCobertura;
import br.com.milkmoney.util.DateUtil;

public class CoberturaValidation extends Validator {
	
	public static void validate(Cobertura cobertura) {
		
		if ( cobertura.getParto() != null ){
			throw new ValidationException(Validator.REGRA_NEGOCIO, "A cobertura j� tem parto registrado, n�o sendo poss�vel executar essa opera��o.");
		}
		
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
		
		if ( cobertura.getNomeResponsavel() == null || cobertura.getNomeResponsavel().isEmpty() ){
			throw new ValidationException(CAMPO_OBRIGATORIO, 
					"Por favor, infome o campo [respons�vel pela ensemina��o] para continuar.");
		}

	}
	
	/*
	 * Uma vaca n�o pode ter a cobertura cadastrada/alterada se:
	 * 1. Foi coberta a menos de 21 dias;
	 * 2. Tiver outra cobertura com situa��o: PRENHA, ou INDEFINIDA; 
	 * Obs: Sempre que a vaca repetir de cio o usu�rio deve marcar a situa��o da cobertura como VAZIA;
	 * No caso de registro de parto a situa��o muda para PARIDA.
	 * @param cobertura
	 */
	public static void validaFemeaSelecionada(Cobertura cobertura, List<Cobertura> coberturasAnimal, int idadeMinimaParaCobertura){
		
		if ( cobertura.getFemea() != null && !cobertura.getFemea().getSexo().equals(Sexo.FEMEA) ){
			throw new ValidationException(REGRA_NEGOCIO, 
					"O animal selecionado para a cobertura deve ser uma f�mea.");
		}
		
		//calcula a idade da f�ma na data da cobertura
		long idadeFemeaDataCobertura = ChronoUnit.MONTHS.between(DateUtil.asLocalDate(cobertura.getData()), DateUtil.asLocalDate(cobertura.getFemea().getDataNascimento()));
		
		if ( Math.abs(idadeFemeaDataCobertura) < idadeMinimaParaCobertura ){
			throw new ValidationException(REGRA_NEGOCIO, 
					"A f�mea selecionada possui " + Math.abs(idadeFemeaDataCobertura) + " meses de idade no dia " + DateUtil.format(cobertura.getData()) + ". " + 
					"No entanto a idade m�nima para cobertura est� definida em " + idadeMinimaParaCobertura + " meses. Por favor, verifique se a data da cobertura e o cadastro do animal est�o corretos e tente novamente.");
		}
		
		if ( coberturasAnimal != null && coberturasAnimal.size() > 0 ){
			
			for ( Cobertura c : coberturasAnimal ){
				//se n�o for a mesma cobertura
				if ( c.getId() != cobertura.getId() ){
					
					long diasEntreCoberturas = ChronoUnit.DAYS.between(DateUtil.asLocalDate(c.getData()), DateUtil.asLocalDate(cobertura.getData()));
					
					if ( Math.abs(diasEntreCoberturas) < 21 ){
						throw new ValidationException(REGRA_NEGOCIO, "O intervalo entre uma cobertura e outra deve ser de pelo menos 21 dias. "
								+ "A f�mea [" + c.getFemea().getNumeroNome()+"] teve cobertura registrada no dia " + DateUtil.format(c.getData()) + ". "
								+ "Verifique se aquela data est� correta. Se for necess�rio corrija-a para ent�o ser poss�vel registrar essa cobertura.");
					}else{
						validaSobreposicaoCoberturas(cobertura, c);
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
	 * @param dosesDisponiveis 
	 * @param aumentouQuantidadeDosesUtilizadas
	 */
	public static void validaEnseminacaoArtificial(Cobertura cobertura, BigDecimal dosesDisponiveis, boolean aumentouQuantidadeDosesUtilizadas){
		
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
				if ( cobertura.getQuantidadeDosesUtilizadas() > dosesDisponiveis.intValue() ){
					throw new ValidationException(REGRA_NEGOCIO, 
							"O s�men selecionado n�o possui quantidade suficiente dispon�vel. "
							+ "Por favor, verifique se a quantidade de doses informada est� correta ou selecione outro s�men.");
				}
			}
			
			//DEVE SER INFORMADA A QUANTIDADE DE DOSES PARA FAZER O CONTROLE SOBRE O ESTOQUE
			if ( cobertura.getQuantidadeDosesUtilizadas() <= 0 ){
				throw new ValidationException(CAMPO_OBRIGATORIO, 
						"Por favor, infome o campo [quantidade doses utilizadas] para continuar.");
			}
			
			
			if ( cobertura.getSemen().getDataCompra().after(cobertura.getData()) ){
				throw new ValidationException(VALIDACAO_FORMULARIO, "A data da cobertura n�o pode ser menor que a data da "
						+ "compra do s�men selecionado. Por favor, corrija e tente novamente.");
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
						throw new ValidationException(REGRA_NEGOCIO,
								"A f�mea [" + c.getFemea().getNumeroNome()+"] possui uma cobertura registrada no dia " + 
								DateUtil.format(c.getData()) +	" com situa��o " + c.getSituacaoCobertura() + ". " +
								"� necess�rio finalizar aquela cobertura, indicando se houve repeti��o de cio ou parto, para ent�o registrar/alterar essa cobertura. " +
								"N�o � poss�vel haver duas coberturas com situa��es PRENHA ou INDEFINIDA ao mesmo tempo para um mesmo animal.");
					}
					
				}
			}
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
