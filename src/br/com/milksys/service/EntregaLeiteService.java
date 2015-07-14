package br.com.milksys.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.EntregaLeiteDao;
import br.com.milksys.model.EntregaLeite;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoLeite;
import br.com.milksys.util.Util;
import br.com.milksys.validation.EntregaLeiteValidation;

@Service
public class EntregaLeiteService implements IService<Integer, EntregaLeite>{

	@Autowired private EntregaLeiteDao dao;
	@Autowired private PrecoLeiteService precoLeiteService;
	@Autowired private ProducaoLeiteService producaoLeiteService;
	private ObservableList<String> meses = Util.generateListMonths();

	@Override
	public boolean save(EntregaLeite entity) {
		
		BigDecimal totalEntregue = loadTotalEntreguePeriodo(entity.getDataInicio(), entity.getDataFim());
		
		entity.setVolume(totalEntregue);
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(entity.getMesReferencia(), entity.getAnoReferencia());
		if ( precoLeite != null ){
			entity.setPrecoLeite(precoLeite);
		}

		EntregaLeiteValidation.validate(entity);
		
		return dao.persist(entity);	
	}
	
	@Override
	public boolean remove(EntregaLeite entity) {
		return dao.remove(entity);
	}

	@Override
	public EntregaLeite findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<EntregaLeite> findAll() {
		return dao.findAll(EntregaLeite.class);
	}
	
	@Override
	public ObservableList<EntregaLeite> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(EntregaLeite.class));
	}
	
	@Override
	public ObservableList<EntregaLeite> defaultSearch(String param) {
		return null;
	}
	
	public EntregaLeite findByMesAno(String mes, int ano){
		return dao.findByMesAno(mes, ano);
	}
	
	public ObservableList<EntregaLeite> findAllByAnoAsObservableList(int anoReferencia) {
		ObservableList<EntregaLeite> list = FXCollections.observableArrayList();
		list.addAll(dao.findAllByAno(anoReferencia));
		return list;
	}
	
	/**
	 * Para cada ano selecionado, configura os meses para registro das entregas realizadas.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 */

	public void configuraMesesEntregaAnoReferencia(int ano){
		
		for (int i = 0; i < meses.size(); i++) {
			
			PrecoLeite precoLeite = precoLeiteService.findByMesAno(meses.get(i), ano);
			EntregaLeite entregaLeite = findByMesAno(meses.get(i), ano);
			
			if ( entregaLeite == null ){
				entregaLeite = new EntregaLeite(meses.get(i), ano, BigDecimal.ZERO, precoLeite);
			}else{
				BigDecimal totalEntregue = loadTotalEntreguePeriodo(entregaLeite.getDataInicio(), entregaLeite.getDataFim());
				entregaLeite.setVolume(totalEntregue);
				if ( entregaLeite.getPrecoLeite() == null ){
					entregaLeite.setPrecoLeite(precoLeite);
				}
			}
			
			save(entregaLeite);
		}

	}
	
	
	/**
	 * Carrega o total entregue no período selecionado.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	private BigDecimal loadTotalEntreguePeriodo(Date dataInicio, Date dataFim){
		BigDecimal totalEntregue = BigDecimal.ZERO;
		List<ProducaoLeite> producaoLeite = producaoLeiteService.findAllByPeriodoAsObservableList(dataInicio, dataFim);
		
		for( ProducaoLeite p : producaoLeite ){
			totalEntregue = totalEntregue.add(p.getVolumeEntregue());
		}
		
		return totalEntregue;
	}

	@Override
	public void validate(EntregaLeite entity) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public ObservableList<Series<String, Number>> getDataChart(int ano) {
		
		ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	XYChart.Series<String, Number> serieVolumeEntregue = new XYChart.Series<String, Number>();
    	XYChart.Series<String, Number> serieValorTotal = new XYChart.Series<String, Number>();
    	
    	serieVolumeEntregue.setName("Volume Entregue");
    	serieValorTotal.setName("Valor Recebido");
    	
    	for ( EntregaLeite entregaLeite : dao.findAllByAno(ano) ){
    		
    		serieVolumeEntregue.getData().add(new XYChart.Data<String, Number>(entregaLeite.getMesReferencia(), entregaLeite.getVolume() ));
    		serieValorTotal.getData().add(new XYChart.Data<String, Number>(entregaLeite.getMesReferencia(), entregaLeite.getValorTotal() ));
    		
    	}
    	
    	series.addAll(serieVolumeEntregue, serieValorTotal);
    	
    	return series;
    	
	}

}
