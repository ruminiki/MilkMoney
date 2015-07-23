package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milksys.dao.PropriedadeDao;
import br.com.milksys.model.Propriedade;
import br.com.milksys.validation.PropriedadeValidation;

@Service
public class PropriedadeService implements IService<Integer, Propriedade>{

	@Autowired private PropriedadeDao dao;

	@Override
	@Transactional
	public boolean save(Propriedade propriedade) {
		PropriedadeValidation.validate(propriedade);
		return dao.persist(propriedade);
	}

	@Override
	@Transactional
	public boolean remove(Propriedade propriedade) {
		return dao.remove(propriedade);
	}

	@Override
	public Propriedade findById(Integer id) {
		return dao.findById(Propriedade.class, id);
	}

	@Override
	public List<Propriedade> findAll() {
		return dao.findAll(Propriedade.class);
	}
	
	public ObservableList<Propriedade> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Propriedade.class));
	}
	
	@Override
	public ObservableList<Propriedade> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findByDescricao(param));
	}

	@Override
	public void validate(Propriedade propriedade) {
		// TODO Auto-generated method stub
		
	}
	
	
}
