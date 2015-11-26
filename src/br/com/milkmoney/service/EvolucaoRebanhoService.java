package br.com.milkmoney.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.model.EvolucaoRebanho;

@Service
public class EvolucaoRebanhoService{

	@Autowired AnimalDao animalDao;
	
	/*public List<EvolucaoRebanho> calculaEvolucaoRebanho(String variavel, int mesInicio, int anoInicio, int mesFim, int anoFim){
		
		List<String> meses = Util.generateListMonthsAbrev();
		
		//pega o último dia do primeiro mês
		Calendar cDataInicio = Calendar.getInstance();
		cDataInicio.set(anoInicio, mesInicio, LocalDate.of(anoFim, mesFim, 01).lengthOfMonth());
		
		//pega o último dia do último mês
		Calendar cDataFim = Calendar.getInstance();
		cDataFim.set(anoFim, mesFim, LocalDate.of(anoFim, mesFim, 01).lengthOfMonth());
		
		List<EvolucaoRebanho> result = new ArrayList<EvolucaoRebanho>();
		
		while ( cDataInicio.compareTo(cDataFim) <= 0 ){
			
			//calcula animais em lactação
			EvolucaoRebanho evolucaoLactacao = new EvolucaoRebanho();
			evolucaoLactacao.setMes(meses.get(cDataInicio.get(Calendar.MONTH)) + "/" + cDataInicio.get(Calendar.YEAR));
			evolucaoLactacao.setVariavel(EvolucaoRebanho.EM_LACTACAO); 
			evolucaoLactacao.setValor(String.valueOf(animalDao.contaAnimaisEmLactacao(cDataInicio.getTime())));
			result.add(evolucaoLactacao);
			
			//calcula animais secos
			EvolucaoRebanho evolucaoSecos = new EvolucaoRebanho();
			evolucaoSecos.setMes(meses.get(cDataInicio.get(Calendar.MONTH)) + "/" + cDataInicio.get(Calendar.YEAR));
			evolucaoSecos.setVariavel(EvolucaoRebanho.SECAS); 
			evolucaoSecos.setValor(String.valueOf(animalDao.contaAnimaisSecos(cDataInicio.getTime())));
			result.add(evolucaoSecos);
			
			//calcula relação de animais em lactação e secos
			EvolucaoRebanho evolucaoRelacaoSecoLactacao = new EvolucaoRebanho();
			evolucaoRelacaoSecoLactacao.setMes(meses.get(cDataInicio.get(Calendar.MONTH)) + "/" + cDataInicio.get(Calendar.YEAR));
			evolucaoRelacaoSecoLactacao.setVariavel(EvolucaoRebanho.RELACAO_LACTACAO_SECOS); 
			if ( Long.parseLong(evolucaoLactacao.getValor()) > 0 ){
				evolucaoRelacaoSecoLactacao.setValor(Long.parseLong(evolucaoSecos.getValor()) / Long.parseLong(evolucaoLactacao.getValor()) * 100 + "%");	
			}else{
				evolucaoRelacaoSecoLactacao.setValor(Long.parseLong(evolucaoSecos.getValor()) * 100 + "%");
			}
			
			result.add(evolucaoRelacaoSecoLactacao);
			
			//calcular novilhas por faixa de idade
			
			cDataInicio.add(Calendar.MONTH, 1);
			
		}
		return result;
	}*/
	
	public List<String> calculaEvolucaoRebanho(String variavel, int mesInicio, int anoInicio, int mesFim, int anoFim){
		
		//pega o último dia do primeiro mês
		Calendar cDataInicio = Calendar.getInstance();
		cDataInicio.set(anoInicio, mesInicio, LocalDate.of(anoFim, mesFim, 01).lengthOfMonth());
		
		//pega o último dia do último mês
		Calendar cDataFim = Calendar.getInstance();
		cDataFim.set(anoFim, mesFim, LocalDate.of(anoFim, mesFim, 01).lengthOfMonth());
		
		List<String> result = new ArrayList<String>();
		
		while ( cDataInicio.compareTo(cDataFim) <= 0 ){
			
			if ( EvolucaoRebanho.EM_LACTACAO.equals(variavel) ){
				result.add(String.valueOf(animalDao.contaAnimaisEmLactacao(cDataInicio.getTime())));
			}
			
			if ( EvolucaoRebanho.SECAS.equals(variavel) ){
				result.add(String.valueOf(String.valueOf(animalDao.contaAnimaisSecos(cDataInicio.getTime()))));
			}
			
			cDataInicio.add(Calendar.MONTH, 1);
			
		}
		
		return result;
		
	}
	
}
