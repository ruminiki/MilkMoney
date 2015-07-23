package br.com.milkmoney.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.ParametroService;

@Service
public class SearchFemeasNaoPrenhasAposXDiasAposParto extends Search<Integer, Animal> {
	
	@Autowired AnimalDao dao;
	@Autowired ParametroService parametroService;
	
	@Override
	public ObservableList<Animal> doSearch(Object ...objects) {
		
		if ( objects != null && objects.length > 0 ){
			int dias = Integer.parseInt(String.valueOf(objects[0]));
			return FXCollections.observableArrayList(dao.findFemeasNaoPrenhasAposXDiasAposParto(dias));
		}
		return null;
	}
	
}
