package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.LancamentoFinanceiroDao;
import br.com.milkmoney.model.CentroCusto;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.model.LancamentoFinanceiroChartModel;
import br.com.milkmoney.model.SaldoCategoriaDespesa;
import br.com.milkmoney.model.TipoLancamentoFinanceiro;
import br.com.milkmoney.validation.LancamentoFinanceiroValidation;

@Service
public class LancamentoFinanceiroService implements IService<Integer, LancamentoFinanceiro>{

	@Autowired private LancamentoFinanceiroDao dao;

	@Override
	@Transactional
	public boolean save(LancamentoFinanceiro lancamentoFinanceiro) {
		validate(lancamentoFinanceiro);
		return dao.persist(lancamentoFinanceiro);
	}

	@Override
	@Transactional
	public boolean remove(LancamentoFinanceiro lancamentoFinanceiro) {
		return dao.remove(lancamentoFinanceiro);
	}

	@Override
	public LancamentoFinanceiro findById(Integer id) {
		return dao.findById(LancamentoFinanceiro.class, id);
	}

	@Override
	public List<LancamentoFinanceiro> findAll() {
		return dao.findAll(LancamentoFinanceiro.class);
	}
	
	public ObservableList<LancamentoFinanceiro> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(LancamentoFinanceiro.class));
	}
	
	@Override
	public ObservableList<LancamentoFinanceiro> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(LancamentoFinanceiro lancamentoFinanceiro) {
		LancamentoFinanceiroValidation.validate(lancamentoFinanceiro);
	}

	public ObservableList<LancamentoFinanceiro> findByMes(Date dataInicio, Date dataFim, String tipoLancamento) {
		return FXCollections.observableArrayList(dao.findByMes(dataInicio, dataFim, tipoLancamento));
	}
	
	public ObservableList<LancamentoFinanceiro> findByMes(String param, Date dataInicio, Date dataFim) {
		return FXCollections.observableArrayList(dao.findByMes(param, dataInicio, dataFim));
	}
	
	public ObservableList<Series<String, Number>> getDataChart(ObservableList<LancamentoFinanceiro> data){

    	ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	XYChart.Series<String, Number> serieReceitas, serieDespesas;
    	
    	BigDecimal receitas = new BigDecimal(0);
		BigDecimal despesas = new BigDecimal(0);
		
		for ( LancamentoFinanceiro lancamento : data ){
			if ( lancamento.getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA) ){
				receitas = receitas.add(lancamento.getValorTotal());
			}else{
				despesas = despesas.add(lancamento.getValorTotal());
			}
		}
    	
		serieReceitas = new XYChart.Series<String, Number>();
		serieReceitas.getData().add(new XYChart.Data<String, Number>(TipoLancamentoFinanceiro.RECEITA, receitas));
		series.add(serieReceitas);
		
		serieDespesas = new XYChart.Series<String, Number>();
		serieDespesas.getData().add(new XYChart.Data<String, Number>(TipoLancamentoFinanceiro.DESPESA, despesas));
		series.add(serieDespesas);
		
    	return series;
    	
    }
	
	@SuppressWarnings("unchecked")
	public ObservableList<Series<String, Number>> getDataChart(int ano, CentroCusto centroCusto) {
		
		ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	XYChart.Series<String, Number> serieDespesas = new XYChart.Series<String, Number>();
    	XYChart.Series<String, Number> serieReceitas = new XYChart.Series<String, Number>();
    	
    	serieDespesas.setName("Despesas");
    	serieReceitas.setName("Receitas");
    	
    	for ( LancamentoFinanceiroChartModel model : dao.resumeByMonthAndYear(ano, centroCusto)){
    		
    		serieReceitas.getData().add(new XYChart.Data<String, Number>(model.getMes(), model.getReceita()));	
   			serieDespesas.getData().add(new XYChart.Data<String, Number>(model.getMes(), model.getDespesa()));	
    		
    	}
    	
    	series.addAll(serieReceitas, serieDespesas);
    	
    	return series;
    	
	}

	public List<SaldoCategoriaDespesa> getSaldoByCategoriaDespesa(Date dataInicio, Date dataFim) {
		return dao.getSaldoByCategoriaDespesa(dataInicio, dataFim);
	}

	
}
