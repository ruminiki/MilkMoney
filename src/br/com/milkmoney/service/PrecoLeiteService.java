package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.PrecoLeiteDao;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.util.Util;
import br.com.milkmoney.validation.PrecoLeiteValidation;

@Service
public class PrecoLeiteService implements IService<Integer, PrecoLeite>{

	@Autowired private PrecoLeiteDao dao;
	private ObservableList<String> meses = Util.generateListMonths();

	@Override
	@Transactional
	public boolean save(PrecoLeite entity) {
		PrecoLeiteValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(PrecoLeite entity) {
		return dao.remove(entity);
	}

	@Override
	public PrecoLeite findById(Integer id) {
		return dao.findById(PrecoLeite.class, id);
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
		return FXCollections.observableArrayList(dao.findAll(PrecoLeite.class));
	}
	
	@Override
	public ObservableList<PrecoLeite> defaultSearch(String param) {
		return null;
	}

	public ObservableList<PrecoLeite> findAllByAnoAsObservableList(int anoReferencia) {
		return FXCollections.observableArrayList(dao.findAllByAno(anoReferencia));
	}

	public boolean isPrecoCadastrado(String mesReferencia, int anoReferencia) {
		PrecoLeite precoLeite = dao.findByMesAno(mesReferencia, anoReferencia);
		return precoLeite != null && precoLeite.getValor().compareTo(BigDecimal.ZERO) > 0;
	}
	
	/**
	 * Configura os meses para registro dos preços.
	 */
	@Transactional
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

	@SuppressWarnings("unchecked")
	public ObservableList<Series<String, Number>> getDataChart(int ano){

    	ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	XYChart.Series<String, Number> serieValorPraticado = new XYChart.Series<String, Number>();
    	XYChart.Series<String, Number> serieValorRecebido = new XYChart.Series<String, Number>();
    	
    	serieValorPraticado.setName("Valor Máximo Praticado");
    	serieValorRecebido.setName("Valor Máximo Recebido");
    	
    	for ( PrecoLeite precoLeite : dao.findAllByAno(ano) ){
    		serieValorPraticado.getData().add(new XYChart.Data<String, Number>(precoLeite.getMesReferencia(), precoLeite.getValorMaximoPraticado()));
    		serieValorRecebido.getData().add(new XYChart.Data<String, Number>(precoLeite.getMesReferencia(), precoLeite.getValorRecebido()));
    	}
    	
    	series.addAll(serieValorPraticado, serieValorRecebido);
    	
    	return series;
    	
    }

}
