package br.com.milksys.service;

import java.math.BigDecimal;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.PrecoLeiteDao;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.util.Util;
import br.com.milksys.validation.PrecoLeiteValidation;

@Service
public class PrecoLeiteService implements IService<Integer, PrecoLeite>{

	@Autowired private PrecoLeiteDao dao;
	private ObservableList<String> meses = Util.generateListMonths();

	@Override
	public boolean save(PrecoLeite entity) {
		PrecoLeiteValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	public boolean remove(PrecoLeite entity) {
		return dao.remove(entity);
	}

	@Override
	public PrecoLeite findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<PrecoLeite> findAll() {
		return dao.findAll(PrecoLeite.class);
	}
	
	public PrecoLeite findByMesAno(String mesReferencia, int anoReferencia){
		return dao.findByMesAno(mesReferencia, anoReferencia);
	}
	
	public PrecoLeite findByMesAno(int mesReferencia, int anoReferencia) {
		return dao.findByMesAno(mesReferencia, anoReferencia);
	}
	
	public ObservableList<PrecoLeite> findAllAsObservableList() {
		ObservableList<PrecoLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(PrecoLeite.class));
		return list;
	}
	
	@Override
	public ObservableList<PrecoLeite> defaultSearch(String param) {
		return null;
	}

	public ObservableList<PrecoLeite> findAllByAnoAsObservableList(int anoReferencia) {
		ObservableList<PrecoLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAllByAno(anoReferencia));
		return list;
	}

	public boolean isPrecoCadastrado(String mesReferencia, int anoReferencia) {
		PrecoLeite precoLeite = dao.findByMesAno(mesReferencia, anoReferencia);
		return precoLeite != null && precoLeite.getValor().compareTo(BigDecimal.ZERO) > 0;
	}
	
	/**
	 * Configura os meses para registro dos preços.
	 */
	public void configuraMesesAnoReferencia(int ano){
		
		for (int i = 0; i < meses.size(); i++) {
			
			PrecoLeite precoLeite = findByMesAno(meses.get(i), ano);
			if ( precoLeite == null ){
				precoLeite = new PrecoLeite(meses.get(i), i+1, ano, BigDecimal.ZERO, BigDecimal.ZERO);
				save(precoLeite);
			}
			
		}
		
	}

	@Override
	public void validate(PrecoLeite entity) {
		// TODO Auto-generated method stub
		
	}

}
