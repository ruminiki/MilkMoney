package br.com.milksys.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;
import br.com.milksys.dao.ParametroDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Parametro;

@Service
public class SearchAnimaisDisponiveisParaCobertura extends Search<Integer, Animal> {
	
	@Autowired AnimalDao dao;
	@Autowired ParametroDao parametroDao;
	
	@Override
	public ObservableList<Animal> doSearch(Object ...objects) {
		
		int diasIdadeMinimaParaCobertura = 0;
		try{
			//o parametro estara em meses, multiplicar por 30 para obter os dias
			diasIdadeMinimaParaCobertura = Integer.parseInt(parametroDao.findBySigla(Parametro.IDMC)) * 30;
		}catch(Exception e){
			diasIdadeMinimaParaCobertura = 24 * 30;
		}
		
		int periodoVoluntarioEspera = 0;
		try{
			periodoVoluntarioEspera = Integer.parseInt(parametroDao.findBySigla(Parametro.PVE));
		}catch(Exception e){
			periodoVoluntarioEspera = 40;//default 40 dias
		}
		
		return FXCollections.observableArrayList(dao.findAnimaisDisponiveisParaCobertura(diasIdadeMinimaParaCobertura, periodoVoluntarioEspera));
		
	}
	
}
