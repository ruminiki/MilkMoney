package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.PrecoLeiteDao;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.model.Projecao;
import br.com.milkmoney.util.DateUtil;

@Service
public class ProjecaoService{

	@Autowired private CoberturaDao coberturaDao;
	@Autowired private AnimalDao animalDao;
	@Autowired private ProducaoLeiteService producaoLeiteService;
	@Autowired private PrecoLeiteDao precoLeiteDao;
	
	/*
	 * JAN - FEV - MAR - ABR - MAI - JUN - JUL - AGO - SET - OUT - NOV - DEZ
	 * |------| 10
	 * |------------| 10 + 3
	 * |------------------| 10 + 3 + 2...
	 * |------------------------|
	 * |------------------------------|
	 * |------------------------------------|
	 * |------------------------------------------|...
	 * 
	 * Para calcular os animais em lactação vai fazendo a soma gradativamente mês a mês dentro do intervalo desejado,
	 * descontando as previsões de encerramento de lactação.
	 * 
	 */

	public int calculaNumeroAnimaisLactacao(int mesReferencia, int anoReferencia){
		Calendar calendar = Calendar.getInstance();
		calendar.set(anoReferencia, mesReferencia, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		
		Date dataInicio = Calendar.getInstance().getTime();
		Date dataFim = calendar.getTime();
		
		//verifica as coberturas que tem previsão de parto dentro do período informado
		List<Cobertura> partosPrevistos = coberturaDao.findAllWithPrevisaoPartoIn(dataInicio, dataFim);
		
		//desconta os encerramentos de lactação
		List<Cobertura> encerramentosPrevistos =  coberturaDao.findAllWithPrevisaoEncerramentoIn(dataInicio, dataFim);
		
		//considera os animais já em lactação e que não serão encerrados no período
		int lactacoes = animalDao.countAllFemeasEmLactacao().intValue();
		
		return 	(lactacoes - (encerramentosPrevistos != null ? encerramentosPrevistos.size() : 0)) +
				(partosPrevistos != null ? partosPrevistos.size() : 0);

	}

	private int calculaNumeroAnimaisSecos(int mesReferencia, int anoReferencia) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(anoReferencia, mesReferencia, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		
		Date dataInicio = Calendar.getInstance().getTime();
		Date dataFim = calendar.getTime();
		
		//verifica as coberturas que tem previsão de parto dentro do período informado
		List<Cobertura> partosPrevistos = coberturaDao.findAllWithPrevisaoPartoIn(dataInicio, dataFim);
		
		//desconta os encerramentos de lactação
		List<Cobertura> encerramentosPrevistos =  coberturaDao.findAllWithPrevisaoEncerramentoIn(dataInicio, dataFim);
		
		//considera os animais já em lactação e que não serão encerrados no período
		int animaisSecos = animalDao.countAllFemeasSecas().intValue();
		
		return 	(animaisSecos + (encerramentosPrevistos != null ? encerramentosPrevistos.size() : 0)) -
				(partosPrevistos != null ? partosPrevistos.size() : 0);

	}
	
	private BigDecimal calculaPercentualVariacaoNumeroAnimaisSecos(int numeroAnimaisSecosPrevistos){
		int animaisSecos = animalDao.countAllFemeasSecas().intValue();
		
		if ( animaisSecos > 0 )
			return BigDecimal.valueOf((( numeroAnimaisSecosPrevistos - animaisSecos ) * 100) / animaisSecos);
		
		return BigDecimal.valueOf(numeroAnimaisSecosPrevistos * 100).setScale(2, RoundingMode.UP);
		
	}

	private BigDecimal calculaPercentualVariacaoNumeroAnimaisLactacao(int numeroAnimaisLactacaoPrevistos){
		int animaisLactacao = animalDao.countAllFemeasEmLactacao().intValue();
		
		if ( animaisLactacao > 0 )
			return BigDecimal.valueOf((( numeroAnimaisLactacaoPrevistos - animaisLactacao ) * 100) / animaisLactacao);
		
		return BigDecimal.valueOf(numeroAnimaisLactacaoPrevistos * 100).setScale(2, RoundingMode.UP);
		 
	}
	
	private BigDecimal getProducaoDiariaIndividualAtual(){
		
		Date inicioMesAtual = DateUtil.asDate(LocalDate.now().withDayOfMonth(01));
		Date fimMesAtual = DateUtil.asDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
		
		double mediaProducaoDiariaAtual = producaoLeiteService.getMediaProducaoIndividualPeriodo(inicioMesAtual, fimMesAtual);
		return BigDecimal.valueOf(mediaProducaoDiariaAtual).setScale(2, RoundingMode.UP);
		
	}
	
	private BigDecimal getProducaoDiariaAtual(){
		
		Date inicioMesAtual = DateUtil.asDate(LocalDate.now().withDayOfMonth(01));
		Date fimMesAtual = DateUtil.asDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
		
		double mediaProducaoDiariaAtual = producaoLeiteService.getMediaProducaoDiariaPeriodo(inicioMesAtual, fimMesAtual);
		return BigDecimal.valueOf(mediaProducaoDiariaAtual).setScale(2, RoundingMode.UP);
		
	}

	private BigDecimal calculaPercentualVariacaoProducaoDiaria(BigDecimal producaoDiariaPrevista){
		BigDecimal producaoDiariaAtual = getProducaoDiariaAtual();
		
		if ( producaoDiariaAtual.compareTo(BigDecimal.ZERO) > 0 ){
			return producaoDiariaPrevista
					.subtract(producaoDiariaAtual)
					.multiply(BigDecimal.valueOf(100))
					.divide(producaoDiariaAtual).setScale(2, RoundingMode.UP);
		}
		
		return producaoDiariaPrevista
				.multiply(BigDecimal.valueOf(100))
				.setScale(2, RoundingMode.UP);
		
	}
	
	private BigDecimal getFaturamentoMensalAtual(){
		PrecoLeite precoLeite = precoLeiteDao.findByMesAno(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
		
		if ( precoLeite != null ){
			return precoLeite.getValor()
					.multiply(getProducaoDiariaAtual().multiply(BigDecimal.valueOf(LocalDate.now().lengthOfMonth())));
		}
		return BigDecimal.ZERO;
	}

	public Projecao generatePrevisao(int mesReferencia, int anoReferencia){
		
		Projecao projecao = new Projecao(mesReferencia, anoReferencia);
		projecao.setNumeroAnimaisLactacao(calculaNumeroAnimaisLactacao(mesReferencia, anoReferencia));
		projecao.setNumeroAnimaisSecos(calculaNumeroAnimaisSecos(mesReferencia, anoReferencia));
		projecao.setProducaoDiaria(getProducaoDiariaIndividualAtual().multiply(BigDecimal.valueOf(projecao.getNumeroAnimaisLactacao())).setScale(1, RoundingMode.UP));
		//multiplica a previsão de produção diária pelos dias do mês de referência
		projecao.setProducaoMensal(projecao.getProducaoDiaria().multiply(BigDecimal.valueOf(LocalDate.of(anoReferencia, mesReferencia, 01).lengthOfMonth())).setScale(1, RoundingMode.UP));
		
		//projecao.setPercentualVariacaoNumeroAnimaisSecos(calculaPercentualVariacaoNumeroAnimaisSecos(projecao.getNumeroAnimaisSecos()));
		//projecao.setPercentualVariacaoNumeroAnimaisLactacao(calculaPercentualVariacaoNumeroAnimaisLactacao(projecao.getNumeroAnimaisLactacao()));
		//projecao.setPercentualVariacaoProducaoDiaria(calculaPercentualVariacaoProducaoDiaria(projecao.getProducaoDiaria()));
		
		PrecoLeite precoLeite = precoLeiteDao.findByMesAno(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
		
		if ( precoLeite != null ){
			projecao.setFaturamentoMensal(precoLeite.getValor().multiply(projecao.getProducaoMensal()).setScale(1, RoundingMode.UP));
			/*projecao.setPercentualVariacaFaturamentoMensal(projecao.getFaturamentoMensal()
					.subtract(getFaturamentoMensalAtual())
					.multiply(BigDecimal.valueOf(100))
					.divide(getFaturamentoMensalAtual(), 1, RoundingMode.UP));*/
		}
		
		return projecao;
		
	}
	
	public List<Projecao> getProjecaoPeriodo(int mesInicio, int mesFim, int anoInicio, int anoFim){
		
		List<Projecao> projecaos = new ArrayList<Projecao>();
		do{
			
			projecaos.add(generatePrevisao(mesInicio, anoInicio));
			
			if ( mesInicio == 12 ){
				mesInicio = 1;
				anoInicio++;
			}else{
				mesInicio++;
			}
			
		}while ( mesInicio + anoInicio != mesFim + anoFim);
		
		return projecaos;
		
	}
	
    public ObservableList<Series<String, Number>> getDataChartAnimaisLactacao(List<Projecao> projecoes){

    	ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	
    	//lactação
    	XYChart.Series<String, Number> serieAnimaisLactacaoAtual = new XYChart.Series<String, Number>();
    	XYChart.Series<String, Number> serieAnimaisLactacaoPrevisto = new XYChart.Series<String, Number>();
    	//XYChart.Series<String, Number> serieVariacaoAnimaisLactacao = new XYChart.Series<String, Number>();
    	
    	serieAnimaisLactacaoAtual.setName("Atual");
    	serieAnimaisLactacaoPrevisto.setName("Projetado");
    	//serieVariacaoAnimaisLactacao.setName("Variação Animais Lactação");
    	
    	for ( Projecao projecao : projecoes ){
    		
    		serieAnimaisLactacaoAtual.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), animalDao.countAllFemeasEmLactacao()));
        	serieAnimaisLactacaoPrevisto.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), projecao.getNumeroAnimaisLactacao()));
        	//serieVariacaoAnimaisLactacao.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), projecao.getPercentualVariacaoNumeroAnimaisLactacao()));
        	
    	}
    	series.add(serieAnimaisLactacaoAtual);
        series.add(serieAnimaisLactacaoPrevisto);
       // series.add(serieVariacaoAnimaisLactacao);
        
    	return series;
    	
    }
    
    public ObservableList<Series<String, Number>> getDataChartAnimaisSecos(List<Projecao> projecoes){

    	ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	
    	XYChart.Series<String, Number> serieAnimaisSecosAtual = new XYChart.Series<String, Number>();
    	XYChart.Series<String, Number> serieAnimaisSecosPrevisto = new XYChart.Series<String, Number>();
    	//XYChart.Series<String, Number> serieVariacaoAnimaisSecos = new XYChart.Series<String, Number>();
    	
    	serieAnimaisSecosAtual.setName("Atual");
    	serieAnimaisSecosPrevisto.setName("Projetado");
    	//serieVariacaoAnimaisSecos.setName("Variação Animais Secos");
    	
    	for ( Projecao projecao : projecoes ){
    		
        	serieAnimaisSecosAtual.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), animalDao.countAllFemeasSecas()));
        	serieAnimaisSecosPrevisto.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), projecao.getNumeroAnimaisSecos()));
        	//serieVariacaoAnimaisSecos.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), projecao.getPercentualVariacaoNumeroAnimaisSecos()));
        	
    	}

    	series.add(serieAnimaisSecosAtual);
        series.add(serieAnimaisSecosPrevisto);
        //series.add(serieVariacaoAnimaisSecos);
        
    	return series;
    	
    }
    
    public ObservableList<Series<String, Number>> getDataChartProducao(List<Projecao> projecoes){

    	ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	
    	XYChart.Series<String, Number> serieProducaoAtual = new XYChart.Series<String, Number>();
    	XYChart.Series<String, Number> serieProducaoPrevista = new XYChart.Series<String, Number>();
    	
    	serieProducaoAtual.setName("Atual");
    	serieProducaoPrevista.setName("Projetado");
    	
    	for ( Projecao projecao : projecoes ){
    		
    		serieProducaoAtual.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), getProducaoDiariaAtual()));
    		serieProducaoPrevista.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), projecao.getProducaoDiaria()));
        	
    	}

    	series.add(serieProducaoAtual);
        series.add(serieProducaoPrevista);
        
    	return series;
    	
    }
	
    public ObservableList<Series<String, Number>> getDataChartFaturamento(List<Projecao> projecoes){

    	ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	
    	XYChart.Series<String, Number> serieFaturamentoAtual = new XYChart.Series<String, Number>();
    	XYChart.Series<String, Number> serieFaturamentoPrevista = new XYChart.Series<String, Number>();
    	
    	serieFaturamentoAtual.setName("Atual");
    	serieFaturamentoPrevista.setName("Projetado");
    	
    	for ( Projecao projecao : projecoes ){
    		
    		serieFaturamentoAtual.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), getFaturamentoMensalAtual()));
    		serieFaturamentoPrevista.getData().add(new XYChart.Data<String, Number>(projecao.getPeriodo(), projecao.getFaturamentoMensal()));
        	
    	}

    	series.add(serieFaturamentoAtual);
        series.add(serieFaturamentoPrevista);
        
    	return series;
    	
    }
	
}
