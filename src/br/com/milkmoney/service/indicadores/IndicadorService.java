package br.com.milkmoney.service.indicadores;

import java.util.List;

import javax.transaction.Transactional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.IndicadorDao;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.validation.IndicadorValidation;

@Service
public class IndicadorService implements IService<Integer, Indicador>{

	@Autowired private IndicadorDao dao;

	@Override
	@Transactional
	public boolean save(Indicador entity) {
		IndicadorValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Indicador entity) {
		return false;
	}

	@Override
	public Indicador findById(Integer id) {
		return dao.findById(Indicador.class, id);
	}

	@Override
	public List<Indicador> findAll() {
		return dao.findAll(Indicador.class);
	}
	
	public ObservableList<Indicador> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Indicador.class));
	}
	
	@Override
	public ObservableList<Indicador> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(Indicador entity) {
		
	}
	
	
}
