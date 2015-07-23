package br.com.milkmoney.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.ParametroDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Parametro;

@Service
public class SearchAnimaisDisponiveisParaCobertura extends Search<Integer, Animal> {
	
	@Autowired AnimalDao dao;
	@Autowired ParametroDao parametroDao;
	
	@Override
	public ObservableList<Animal> doSearch(Object ...objects) {
		
		int diasIdadeMinimaParaCobertura = 0;
		try{
			//o parametro estara em meses, multiplicar por 30 para obter os dias
			diasIdadeMinimaParaCobertura = Integer.parseInt(parametroDao.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA)) * 30;
		}catch(Exception e){
			diasIdadeMinimaParaCobertura = 24 * 30;
		}
		
		int periodoVoluntarioEspera = 0;
		try{
			periodoVoluntarioEspera = Integer.parseInt(parametroDao.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		}catch(Exception e){
			periodoVoluntarioEspera = 40;//default 40 dias
		}
		
		return FXCollections.observableArrayList(dao.findAnimaisDisponiveisParaCobertura(diasIdadeMinimaParaCobertura, periodoVoluntarioEspera));
		
	}
	
}
