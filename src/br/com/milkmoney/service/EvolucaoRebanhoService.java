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
	
	public List<String> calculaEvolucaoRebanho(String variavel, int mesInicio, int anoInicio, int mesFim, int anoFim){
		
		//pega o último dia do primeiro mês
		Calendar cDataInicio = Calendar.getInstance();
		cDataInicio.set(anoInicio, mesInicio-1, LocalDate.of(anoFim, mesFim, 01).lengthOfMonth());
		
		//pega o último dia do último mês
		Calendar cDataFim = Calendar.getInstance();
		cDataFim.set(anoFim, mesFim-1, LocalDate.of(anoFim, mesFim, 01).lengthOfMonth());
		
		boolean dataFimMesCorrente = false;
		if ( (LocalDate.now().getYear() + "" + LocalDate.now().getMonthValue()).equals(anoFim + "" + mesFim) ){
			dataFimMesCorrente = true;
		}
		
		List<String> result = new ArrayList<String>();
		
		while ( cDataInicio.compareTo(cDataFim) <= 0 ){
			
			if ( EvolucaoRebanho.EM_LACTACAO.equals(variavel) ){
				result.add(String.valueOf(animalDao.contaAnimaisEmLactacao(cDataInicio.getTime())));
			}
			
			if ( EvolucaoRebanho.SECAS.equals(variavel) ){
				result.add(String.valueOf(String.valueOf(animalDao.contaAnimaisSecos(cDataInicio.getTime()))));
			}
			
			if ( EvolucaoRebanho.ZERO_A_UM_ANO.equals(variavel) ){
				result.add(String.valueOf(String.valueOf(animalDao.countAllNovilhasIdadeAteXMeses(12, cDataInicio.getTime()))));
			}
			
			if ( EvolucaoRebanho.UM_A_DOIS_ANOS.equals(variavel) ){
				result.add(String.valueOf(String.valueOf(animalDao.countAllNovilhasIdadeEntreMeses(13, 24, cDataInicio.getTime()))));
			}
			
			if ( EvolucaoRebanho.DOIS_A_TRES_ANOS.equals(variavel) ){
				result.add(String.valueOf(String.valueOf(animalDao.countAllNovilhasIdadeEntreMeses(25, 36, cDataInicio.getTime()))));
			}
			
			if ( EvolucaoRebanho.TRES_A_QUATRO_ANOS.equals(variavel) ){
				result.add(String.valueOf(String.valueOf(animalDao.countAllNovilhasIdadeEntreMeses(37, 48, cDataInicio.getTime()))));
			}
			
			if ( EvolucaoRebanho.MAIS_QUATRO_ANOS.equals(variavel) ){
				result.add(String.valueOf(String.valueOf(animalDao.countAllNovilhasIdadeAcimaXMeses(48, cDataInicio.getTime()))));
			}
			
			if ( dataFimMesCorrente && cDataInicio.get(Calendar.MONTH) + 1 == mesFim - 1 && cDataInicio.get(Calendar.YEAR) == anoFim ){
				cDataInicio.set(Calendar.DAY_OF_MONTH, LocalDate.now().getDayOfMonth());
			}
			
			cDataInicio.add(Calendar.MONTH, 1);
			
		}
		
		return result;
		
	}
	
}
