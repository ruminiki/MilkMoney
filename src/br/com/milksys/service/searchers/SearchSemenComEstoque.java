package br.com.milksys.service.searchers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.SemenDao;
import br.com.milksys.model.Semen;

@Service
public class SearchSemenComEstoque extends Search<Integer, Semen> {
	
	@Autowired SemenDao dao;
	
	@Override
	public ObservableList<Semen> doSearch(Object ...objects) {
		return FXCollections.observableArrayList(dao.findAllComEstoque());
	}
	
}
