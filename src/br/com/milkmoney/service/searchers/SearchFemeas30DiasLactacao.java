package br.com.milkmoney.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.model.Animal;

@Service
public class SearchFemeas30DiasLactacao extends Search<Integer, Animal> {
	
	@Autowired AnimalDao dao;
	
	@Override
	public ObservableList<Animal> doSearch(Object ...objects) {
		return FXCollections.observableArrayList(dao.findAllFemeasLactacaoXDias(30));
	}
	
}
