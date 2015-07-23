package br.com.milkmoney.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.service.ParametroService;

@Service
public class SearchFemeasEmPeriodoVoluntarioEspera extends Search<Integer, Animal> {
	
	@Autowired AnimalDao dao;
	@Autowired ParametroService parametroService;
	
	@Override
	public ObservableList<Animal> doSearch(Object ...objects) {
		return FXCollections.observableArrayList(
				dao.findAllFemeasEmPeriodoVoluntarioEspera(
						Integer.parseInt(parametroService.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA))));
	}
	
}
