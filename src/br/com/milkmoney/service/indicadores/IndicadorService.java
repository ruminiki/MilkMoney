package br.com.milkmoney.service.indicadores;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.transaction.Transactional;

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
		validate(entity);
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
	
	@Override
	public ObservableList<Indicador> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Indicador.class));
	}
	
	public Indicador refreshValorApurado(Indicador indicador, Date data){
		return dao.refreshValorApurado(indicador, data);
	}
	
	public ObservableList<Indicador> findAllIndicadoresZootecnicosAsObservableList(boolean refreshValorApurado, Date data) {
		return FXCollections.observableArrayList(dao.findAllIndicadoresZootecnicos(refreshValorApurado, data));
	}
	
	public ObservableList<Indicador> findAllQuantitativosRebanhoAsObservableList(boolean refreshValorApurado, Date data) {
		return FXCollections.observableArrayList(dao.findAllQuantitativosRebanho(refreshValorApurado, data));
	}
	
	@Override
	public ObservableList<Indicador> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(Indicador entity) {
		IndicadorValidation.validate(entity);
	}

}
