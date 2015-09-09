package br.com.milkmoney.service;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.ConfirmacaoPrenhesDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.ConfirmacaoPrenhes;
import br.com.milkmoney.validation.ConfirmacaoPrenhesValidation;

@Service
public class ConfirmacaoPrenhesService implements IService<Integer, ConfirmacaoPrenhes>{

	@Autowired private ConfirmacaoPrenhesDao dao;
	@Autowired private PartoService partoService;

	@Override
	@Transactional
	public boolean save(ConfirmacaoPrenhes confirmacaoPrenhes) {
		
		ConfirmacaoPrenhesValidation.validate(confirmacaoPrenhes);
		return dao.persist(confirmacaoPrenhes);
		
	}
	
	public ObservableList<ConfirmacaoPrenhes> findByCobertura(Cobertura cobertura){
		return FXCollections.observableArrayList(dao.findByCobertura(cobertura));
	}
	
	@Override
	@Transactional
	public boolean remove(ConfirmacaoPrenhes confirmacaoPrenhes) {
		return dao.remove(confirmacaoPrenhes);
	}

	@Override
	public ConfirmacaoPrenhes findById(Integer id) {
		return dao.findById(ConfirmacaoPrenhes.class, id);
	}

	@Override
	public List<ConfirmacaoPrenhes> findAll() {
		return dao.findAll(ConfirmacaoPrenhes.class);
	}
	
	public ObservableList<ConfirmacaoPrenhes> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(ConfirmacaoPrenhes.class));
	}
	
	@Override
	public ObservableList<ConfirmacaoPrenhes> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(ConfirmacaoPrenhes confirmacaoPrenhes) {
		ConfirmacaoPrenhesValidation.validate(confirmacaoPrenhes);
	}

	public ConfirmacaoPrenhes findLastByCobertura(Cobertura cobertura) {
		return dao.findLastByCobertura(cobertura);
		
	}

	public ConfirmacaoPrenhes findFirstAfterData(Animal animal, Date data, String situacao) {
		return dao.findFirstAfterData(animal, data, situacao);
	}

}
