package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.ConfirmacaoPrenhezDao;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.ConfirmacaoPrenhez;
import br.com.milkmoney.validation.ConfirmacaoPrenhezValidation;

@Service
public class ConfirmacaoPrenhezService implements IService<Integer, ConfirmacaoPrenhez>{

	@Autowired private ConfirmacaoPrenhezDao dao;
	@Autowired private PartoService partoService;
	@Autowired private SemenService semenService;

	@Override
	@Transactional
	public boolean save(ConfirmacaoPrenhez confirmacaoPrenhez) {
		
		ConfirmacaoPrenhezValidation.validate(confirmacaoPrenhez);
		return dao.persist(confirmacaoPrenhez);
		
	}
	
	public ObservableList<ConfirmacaoPrenhez> findByCobertura(Cobertura cobertura){
		return FXCollections.observableArrayList(dao.findByCobertura(cobertura));
	}
	
	@Override
	@Transactional
	public boolean remove(ConfirmacaoPrenhez confirmacaoPrenhez) {
		return dao.remove(confirmacaoPrenhez);
	}

	@Override
	public ConfirmacaoPrenhez findById(Integer id) {
		return dao.findById(ConfirmacaoPrenhez.class, id);
	}

	@Override
	public List<ConfirmacaoPrenhez> findAll() {
		return dao.findAll(ConfirmacaoPrenhez.class);
	}
	
	public ObservableList<ConfirmacaoPrenhez> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(ConfirmacaoPrenhez.class));
	}
	
	@Override
	public ObservableList<ConfirmacaoPrenhez> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(ConfirmacaoPrenhez confirmacaoPrenhez) {
		ConfirmacaoPrenhezValidation.validate(confirmacaoPrenhez);
	}

	public ConfirmacaoPrenhez findLastByCobertura(Cobertura cobertura) {
		return dao.findLastByCobertura(cobertura);
		
	}

}
