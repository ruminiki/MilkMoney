package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.MotivoVendaAnimalDao;
import br.com.milkmoney.model.MotivoVendaAnimal;

@Service
public class MotivoVendaAnimalService implements IService<Integer, MotivoVendaAnimal>{

	@Autowired private MotivoVendaAnimalDao dao;

	@Override
	@Transactional
	public boolean save(MotivoVendaAnimal entity) {
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(MotivoVendaAnimal entity) {
		return dao.remove(entity);
	}

	@Override
	public MotivoVendaAnimal findById(Integer id) {
		return dao.findById(MotivoVendaAnimal.class, id);
	}

	@Override
	public List<MotivoVendaAnimal> findAll() {
		return dao.findAll(MotivoVendaAnimal.class);
	}
	
	public ObservableList<MotivoVendaAnimal> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(MotivoVendaAnimal.class));
	}
	
	@Override
	public ObservableList<MotivoVendaAnimal> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.findByDescricao(param));
	}

	@Override
	public void validate(MotivoVendaAnimal entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
