package br.com.milksys.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;
import br.com.milksys.model.Animal;
import br.com.milksys.service.ParametroService;

@Service
public class SearchFemeasNaoPrenhasXDiasAposParto extends Search<Integer, Animal> {
	
	@Autowired AnimalDao dao;
	@Autowired ParametroService parametroService;
	
	@Override
	public ObservableList<Animal> doSearch(Object ...objects) {
		
		if ( objects != null && objects.length > 0 ){
			int dias = Integer.parseInt(String.valueOf(objects[0]));
			return FXCollections.observableArrayList(dao.findFemeasNaoPrenhasXDiasAposParto(dias));
		}
		return null;
	}
	
}
