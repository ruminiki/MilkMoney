package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.EntregaLeiteDao;
import br.com.milkmoney.model.EntregaLeite;
import br.com.milkmoney.model.ProducaoLeite;
import br.com.milkmoney.model.SimNao;
import br.com.milkmoney.util.Util;
import br.com.milkmoney.validation.EntregaLeiteValidation;

@Service
public class EntregaLeiteService implements IService<Integer, EntregaLeite>{

	@Autowired private EntregaLeiteDao dao;
	@Autowired private PrecoLeiteService precoLeiteService;
	@Autowired private ProducaoLeiteService producaoLeiteService;
	private ObservableList<String> meses = Util.generateListMonths();

	@Override
	@Transactional
	public boolean save(EntregaLeite entregaLeite) {
		
		if ( entregaLeite.getCarregaMarcacoesMes().equals(SimNao.SIM) ){
			BigDecimal totalEntregue = loadTotalEntreguePeriodo(entregaLeite.getDataInicio(), entregaLeite.getDataFim());
			entregaLeite.setVolume(totalEntregue);
		}
		
		EntregaLeiteValidation.validate(entregaLeite);
		
		return dao.persist(entregaLeite);	
	}
	
	@Override
	@Transactional
	public boolean remove(EntregaLeite entregaLeite) {
		return dao.remove(entregaLeite);
	}

	@Override
	public EntregaLeite findById(Integer id) {
		return dao.findById(EntregaLeite.class, id);
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
	public ObservableList<EntregaLeite> defaultSearch(String param, int limit) {
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
	@Transactional
	public void configuraMesesEntregaAnoReferencia(int ano){
		
		for (int i = 0; i < meses.size(); i++) {
			
			EntregaLeite entregaLeite = findByMesAno(meses.get(i), ano);
			
			if ( entregaLeite == null ){
				entregaLeite = new EntregaLeite(meses.get(i), ano, BigDecimal.ZERO);
			}else{
				if ( entregaLeite.getCarregaMarcacoesMes().equals(SimNao.SIM) ){
					BigDecimal totalEntregue = loadTotalEntreguePeriodo(entregaLeite.getDataInicio(), entregaLeite.getDataFim());
					entregaLeite.setVolume(totalEntregue);
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
	public BigDecimal loadTotalEntreguePeriodo(Date dataInicio, Date dataFim){
		BigDecimal totalEntregue = BigDecimal.ZERO;
		List<ProducaoLeite> producaoLeite = producaoLeiteService.findAllByPeriodoAsObservableList(dataInicio, dataFim);
		
		for( ProducaoLeite p : producaoLeite ){
			totalEntregue = totalEntregue.add(p.getVolumeEntregue());
		}
		
		return totalEntregue;
	}

	@Override
	public void validate(EntregaLeite entregaLeite) {
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

	public EntregaLeite getPrimeiraEntrega() {
		return dao.getPrimeiraEntrega();
	}

}
