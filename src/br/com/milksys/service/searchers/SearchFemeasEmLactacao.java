package br.com.milksys.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.AnimalDao;
import br.com.milksys.model.Animal;

@Service
public class SearchFemeasEmLactacao extends Search<Integer, Animal> {
	
	@Autowired AnimalDao dao;
	
	@Override
	public ObservableList<Animal> doSearch(Object ...params) {
		return FXCollections.observableArrayList(dao.findAllFemeasEmLactacao(params));
	}
	
}
