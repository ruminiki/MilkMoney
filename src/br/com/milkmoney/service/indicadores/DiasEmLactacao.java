package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.LactacaoDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.Limit;
import br.com.milkmoney.service.PartoService;

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
	@Autowired private PartoService partoService;
	@Autowired private AnimalDao animalDao;
	@Autowired private LactacaoDao lactacaoDao;
	
	@Override
	public BigDecimal getValue(Date data) {
		
		BigInteger dias = BigInteger.ZERO;
		//vacas em lactação
		List<Animal> femeas = animalDao.findAllFemeasEmLactacao(data, Limit.UNLIMITED);
		
		for ( Animal animal : femeas ){
			
			Lactacao lactacao = lactacaoDao.findUltimaLactacaoAnimal(animal);
			dias = dias.add(BigInteger.valueOf(lactacao.getDuracaoLactacaoDias(data)));
			
		}

		if ( dias.compareTo(BigInteger.ZERO) > 0 ){
			return BigDecimal.valueOf(dias.divide(BigInteger.valueOf(femeas.size())).longValue());
		}
		
		return BigDecimal.ZERO;
		
	}

}
