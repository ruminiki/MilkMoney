package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.ProducaoLeiteDao;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.model.ProducaoLeite;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.ProducaoLeiteValidation;

@Service
public class ProducaoLeiteService implements IService<Integer, ProducaoLeite>{

	@Autowired private ProducaoLeiteDao dao;
	@Autowired private PrecoLeiteService precoLeiteService;

	@Override
	@Transactional
	public boolean save(ProducaoLeite entity) {
		
		if ( entity.getId() <= 0 ){
			ProducaoLeite p = dao.findByDate(entity.getData());
			if ( p != null ){
				return true;
			}
		}
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(entity.getMes(), entity.getAno());
		if ( precoLeite != null ){
			entity.setValor(precoLeite.getValor().multiply(entity.getVolumeEntregue()));
		}
		
		ProducaoLeiteValidation.validate(entity);
		
		return dao.persist(entity);	
	}
	
	@Override
	@Transactional
	public boolean remove(ProducaoLeite entity) {
		return dao.remove(entity);
	}

	@Override
	public ProducaoLeite findById(Integer id) {
		return dao.findById(ProducaoLeite.class, id);
	}

	@Override
	public List<ProducaoLeite> findAll() {
		return dao.findAll(ProducaoLeite.class);
	}
	
	@Override
	public ObservableList<ProducaoLeite> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(ProducaoLeite.class));
	}
	
	@Override
	public ObservableList<ProducaoLeite> defaultSearch(String param) {
		return null;
	}
	
	public ObservableList<ProducaoLeite> findAllByPeriodoAsObservableList(Date inicio, Date fim) {
		return FXCollections.observableArrayList(dao.findAllByPeriodo(inicio, fim));
	}

	/**
	 * Método que percorre lista de objetos atualizando o valor com base no preço do leite do mês
	 * @param data
	 * @param mes
	 * @param ano
	 */
	@Transactional
	public void recarregaPrecoLeite(ObservableList<ProducaoLeite> data, String mes, int ano){
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(mes, ano);
		
		if ( precoLeite != null ){
			//varre a tabela atualizando os valores diários
			for ( int index = 0; index < data.size(); index++ ){
				ProducaoLeite producaoLeite = data.get(index);
				producaoLeite.setValor(precoLeite.getValor().multiply(producaoLeite.getVolumeEntregue()));
				data.set(index, producaoLeite);
			}
		}
		
	}

	/**
	 * Para cada mês selecionado, configura os dias para registro da produção diária.
	 * Os dias já são salvos no banco para a montagem da tabela. 
	 * 
	 * @param dataInicio
	 * @param dataFim
	 */
	@Transactional
	public void configuraTabelaDiasMesSelecionado(Date dataInicio, Date dataFim){
		
		Calendar cDataInicio = Calendar.getInstance();
		cDataInicio.setTimeInMillis(dataInicio.getTime());
		
		Calendar cDataFim = Calendar.getInstance();
		cDataFim.setTimeInMillis(dataFim.getTime());
		
		while ( cDataInicio.before(cDataFim) || cDataInicio.equals(cDataFim) ){
			ProducaoLeite producaoLeite = new ProducaoLeite(DateUtil.asLocalDate(cDataInicio.getTime()), 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
			save(producaoLeite);
			cDataInicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		
	}

	@Override
	public void validate(ProducaoLeite entity) {
		// TODO Auto-generated method stub
		
	}
	
	public ObservableList<Series<String, Number>> getDataChart(Date inicio, Date fim){

    	ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	XYChart.Series<String, Number> serieVolumeProduzido = new XYChart.Series<String, Number>();
    	serieVolumeProduzido.setName("Volume Produzido");
    	
    	for ( ProducaoLeite producaoLeite : dao.findAllByPeriodo(inicio, fim) ){
    		serieVolumeProduzido.getData().add(new XYChart.Data<String, Number>(DateUtil.format(producaoLeite.getData()), producaoLeite.getVolumeProduzido()));
    	}
    	
    	series.add(serieVolumeProduzido);
    	
    	return series;
    	
    }
	
	public Double getMediaProducaoIndividualPeriodo(Date dataInicio, Date dataFim){
		
		double mediaProducaoIndividualDiaria = 0;
		
		List<ProducaoLeite> producaoLeitePeriodo = dao.findAllByPeriodo(dataInicio, dataFim);
		
		int diasRegistrados = 0;
		
		for ( ProducaoLeite producaoLeite : producaoLeitePeriodo ){
			
			if ( producaoLeite.getVolumeProduzido().compareTo(BigDecimal.ZERO) > 0 ){
				mediaProducaoIndividualDiaria += producaoLeite.getMediaProducao().doubleValue();
				diasRegistrados++;
			}
			
		}
		
		return diasRegistrados > 0 ? mediaProducaoIndividualDiaria / diasRegistrados : 0;
		
	}
	
	public Double getMediaProducaoDiariaPeriodo(Date dataInicio, Date dataFim){
		
		double totalProducaoDiaria = 0;
		
		List<ProducaoLeite> producaoLeitePeriodo = dao.findAllByPeriodo(dataInicio, dataFim);
		
		int diasRegistrados = 0;
		
		for ( ProducaoLeite producaoLeite : producaoLeitePeriodo ){
			
			if ( producaoLeite.getVolumeProduzido().compareTo(BigDecimal.ZERO) > 0 ){
				totalProducaoDiaria += producaoLeite.getVolumeProduzido().doubleValue();
				diasRegistrados++;
			}
			
		}
		
		return diasRegistrados > 0 ? totalProducaoDiaria / diasRegistrados : 0;
		
	}
	
	public Double getProducaoPeriodo(Date dataInicio, Date dataFim){
		
		double producaoPeriodo = 0;
		
		List<ProducaoLeite> producaoLeitePeriodo = dao.findAllByPeriodo(dataInicio, dataFim);
		
		for ( ProducaoLeite producaoLeite : producaoLeitePeriodo ){
			producaoPeriodo += producaoLeite.getVolumeProduzido().doubleValue();
		}
		
		return producaoPeriodo;
		
	}
	
}
