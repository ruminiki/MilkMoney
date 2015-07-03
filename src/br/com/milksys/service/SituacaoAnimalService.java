package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.SituacaoAnimalDao;
import br.com.milksys.model.SituacaoAnimal;

@Service
public class SituacaoAnimalService implements IService<Integer, SituacaoAnimal>{

	@Autowired private SituacaoAnimalDao dao;

	@Override
	public boolean save(SituacaoAnimal entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(SituacaoAnimal entity) {
		return dao.remove(entity);
	}

	@Override
	public SituacaoAnimal findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<SituacaoAnimal> findAll() {
		return dao.findAll(SituacaoAnimal.class);
	}
	
	public ObservableList<SituacaoAnimal> findAllAsObservableList() {
		ObservableList<SituacaoAnimal> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(SituacaoAnimal.class));
		return list;
	}

	@Override
	public void validate(SituacaoAnimal entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
