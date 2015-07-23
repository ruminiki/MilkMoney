package br.com.milkmoney.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;

@Service
public class SearchCoberturasAnimal extends Search<Integer, Cobertura> {
	
	@Autowired CoberturaDao dao;
	
	@Override
	public ObservableList<Cobertura> doSearch(Object ...objects) {
		return FXCollections.observableArrayList(dao.findByAnimal((Animal)objects[0]));
	}
	
}
