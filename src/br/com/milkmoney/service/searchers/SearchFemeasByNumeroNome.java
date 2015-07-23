package br.com.milkmoney.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.ParametroDao;
import br.com.milkmoney.model.Animal;

@Service
public class SearchFemeasByNumeroNome extends Search<Integer, Animal> {
	
	@Autowired AnimalDao dao;
	@Autowired ParametroDao parametroDao;
	
	@Override
	public ObservableList<Animal> doSearch(Object ...objects) {
		if ( objects != null && objects.length > 0 )
			return FXCollections.observableArrayList(dao.findFemeasByNumeroNome((String)objects[0]));
		return null;
	}
	
}
