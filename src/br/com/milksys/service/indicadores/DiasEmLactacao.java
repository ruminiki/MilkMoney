package br.com.milksys.service.indicadores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.MorteAnimalDao;
import br.com.milksys.dao.PartoDao;
import br.com.milksys.dao.VendaAnimalDao;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.MorteAnimal;
import br.com.milksys.model.Parto;
import br.com.milksys.model.VendaAnimal;
import br.com.milksys.util.DateUtil;

/**
 * Dias em Lactação
 * 1. Buscar todos os partos ocorridos até a data atual
 * 2. Conta os dias de lactação de cada um deles, considerando o fim da lactação como
 * 1. Registro de encerramento da lactação;
 * 2. Morte do animal;
 * 3. Venda do animal.
 * 3. Divide o total de dias de lactação pelo número de lactações.
 * 
 * @author ruminiki
 *
 */


@Service
public class DiasEmLactacao extends AbstractCalculadorIndicador{

	@Autowired private PartoDao partoDao;
	@Autowired private VendaAnimalDao vendaAnimalDao;
	@Autowired private MorteAnimalDao morteAnimalDao;
	
	@Override
	public String getValue() {
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		int        totalPartos    = 0;
		
		List<Parto> partos = partoDao.findAllOrderDataDesc();
		
		for ( Parto parto : partos ){
			
			BigDecimal diasLactacaoParto = contaDiasLactacaoParto(parto);
			
			if ( diasLactacaoParto.compareTo(BigDecimal.ZERO) <= 0 ){
				//se retornou zero é porque o ultimo parto não teve encerramento da lactação
				//e o animal não foi vendido nem está morto.
				//Nesse caso utilizar a data corrente para cálculo dos dias em lactação
				diasLactacaoParto = BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), LocalDate.now()));
			}
			
			diasEmLactacao = diasEmLactacao.add(diasLactacaoParto);
			totalPartos++;
			
		}
		
		if ( diasEmLactacao.compareTo(BigDecimal.ZERO) > 0 && totalPartos > 0 ){
			diasEmLactacao = diasEmLactacao.divide(new BigDecimal(totalPartos), 2, RoundingMode.HALF_UP);
		}
		
		return String.valueOf(diasEmLactacao);
		
	}
	
	private BigDecimal contaDiasLactacaoParto(Parto parto){
		
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		
		//verifica se o parto teve o encerramento da lactação
		EncerramentoLactacao encerramento = parto.getEncerramentoLactacao();
		
		if ( encerramento != null ){
			diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(encerramento.getData()))));
		}else{
			
			//Procura registro venda animal após o parto
			VendaAnimal vendaAnimal = vendaAnimalDao.findByAnimalAfterDate(parto.getData(), parto.getCobertura().getFemea());
			
			if ( vendaAnimal != null ){
				long diasEntreVendaEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(vendaAnimal.getDataVenda()));
				if ( diasEntreVendaEInicioPeriodo > 0 ){//a lactação avançou pelo período
					diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreVendaEInicioPeriodo));
				}
			}
			
			//Procura registro morte animal após o último parto
			MorteAnimal morteAnimal = morteAnimalDao.findByAnimalAfterDate(parto.getData(), parto.getCobertura().getFemea());
			if ( morteAnimal != null ){
				long diasEntreMorteEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(morteAnimal.getDataMorte()));
				if ( diasEntreMorteEInicioPeriodo > 0 ){//a lactação avançou pelo período
					diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreMorteEInicioPeriodo));
				}
			}
			
		}
		
		return diasEmLactacao;
		
	}

}
