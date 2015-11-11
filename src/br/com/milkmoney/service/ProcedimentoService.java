package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.ProcedimentoDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Procedimento;
import br.com.milkmoney.validation.ProcedimentoValidation;

@Service
public class ProcedimentoService implements IService<Integer, Procedimento>{

	@Autowired private ProcedimentoDao dao;

	@Override
	@Transactional
	public boolean save(Procedimento procedimento) {
		validate(procedimento);
		return dao.persist(procedimento);
	}

	@Override
	@Transactional
	public boolean remove(Procedimento procedimento) {
		return dao.remove(procedimento);
	}

	@Override
	public Procedimento findById(Integer id) {
		return dao.findById(Procedimento.class, id);
	}

	@Override
	public List<Procedimento> findAll() {
		return dao.findAll(Procedimento.class);
	}
	
	public ObservableList<Procedimento> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Procedimento.class));
	}
	
	@Override
	public ObservableList<Procedimento> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(Procedimento procedimento) {
		ProcedimentoValidation.validate(procedimento);
	}

	public Procedimento getUltimoTratamento(Animal animal) {
		return dao.getUtimoTratamento(animal);
	}

	public List<Procedimento> findByAnimal(Animal animal) {
		return dao.findByAnimal(animal);
	}
	
	
}
