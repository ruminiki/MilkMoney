package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.PrecoLeiteDao;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Projecao;
import br.com.milkmoney.model.PrecoLeite;
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
	
	private double calculaPercentualVariacaoNumeroAnimaisSecos(int numeroAnimaisSecosPrevistos){
		int animaisSecos = animalDao.countAllFemeasSecas().intValue();
		
		if ( animaisSecos > 0 )
			return (( numeroAnimaisSecosPrevistos - animaisSecos ) * 100) / animaisSecos;
		
		return BigDecimal.valueOf(numeroAnimaisSecosPrevistos * 100).setScale(2, RoundingMode.UP).doubleValue();
		
	}

	private double calculaPercentualVariacaoNumeroAnimaisLactacao(int numeroAnimaisLactacaoPrevistos){
		int animaisLactacao = animalDao.countAllFemeasEmLactacao().intValue();
		
		if ( animaisLactacao > 0 )
			return (( numeroAnimaisLactacaoPrevistos - animaisLactacao ) * 100) / animaisLactacao;
		
		return BigDecimal.valueOf(numeroAnimaisLactacaoPrevistos * 100).setScale(2, RoundingMode.UP).doubleValue();
		 
	}
	
	private double getProducaoDiariaIndividualAtual(){
		
		Date inicioMesAtual = DateUtil.asDate(LocalDate.now().withDayOfMonth(01));
		Date fimMesAtual = DateUtil.asDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
		
		double mediaProducaoDiariaAtual = producaoLeiteService.getMediaProducaoIndividualPeriodo(inicioMesAtual, fimMesAtual);
		return BigDecimal.valueOf(mediaProducaoDiariaAtual).setScale(2, RoundingMode.UP).doubleValue();
		
	}
	
	private double getProducaoDiariaAtual(){
		
		Date inicioMesAtual = DateUtil.asDate(LocalDate.now().withDayOfMonth(01));
		Date fimMesAtual = DateUtil.asDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
		
		double mediaProducaoDiariaAtual = producaoLeiteService.getMediaProducaoDiariaPeriodo(inicioMesAtual, fimMesAtual);
		return BigDecimal.valueOf(mediaProducaoDiariaAtual).setScale(2, RoundingMode.UP).doubleValue();
		
	}

	private double calculaPercentualVariacaoProducaoDiaria(double producaoDiariaPrevista){
		double producaoDiariaAtual = getProducaoDiariaAtual();
		
		if ( producaoDiariaAtual > 0 )
			return (( producaoDiariaPrevista - producaoDiariaAtual ) * 100) / producaoDiariaAtual;
		
		return BigDecimal.valueOf(producaoDiariaPrevista * 100).setScale(2, RoundingMode.UP).doubleValue();
		
	}

	public Projecao generatePrevisao(int mesReferencia, int anoReferencia){
		
		Projecao projecao = new Projecao(mesReferencia, anoReferencia);
		projecao.setNumeroAnimaisLactacao(calculaNumeroAnimaisLactacao(mesReferencia, anoReferencia));
		projecao.setNumeroAnimaisSecos(calculaNumeroAnimaisSecos(mesReferencia, anoReferencia));
		projecao.setPercentualVariacaoNumeroAnimaisSecos(calculaPercentualVariacaoNumeroAnimaisSecos(projecao.getNumeroAnimaisSecos()));
		projecao.setPercentualVariacaoNumeroAnimaisLactacao(calculaPercentualVariacaoNumeroAnimaisLactacao(projecao.getNumeroAnimaisLactacao()));
		projecao.setProducaoDiaria(BigDecimal.valueOf(projecao.getNumeroAnimaisLactacao() * getProducaoDiariaIndividualAtual()).setScale(1, RoundingMode.UP).doubleValue());
		projecao.setPercentualVariacaoProducaoDiaria(calculaPercentualVariacaoProducaoDiaria(projecao.getProducaoDiaria()));
		//multiplica a previsão de produção diária pelos dias do mês de referência
		projecao.setProducaoMensal(BigDecimal.valueOf(projecao.getProducaoDiaria() * LocalDate.of(anoReferencia, mesReferencia, 01).lengthOfMonth()).setScale(1, RoundingMode.UP).doubleValue());
		
		PrecoLeite precoLeite = precoLeiteDao.findByMesAno(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
		
		if ( precoLeite != null ){
			projecao.setFaturamentoMensal(precoLeite.getValor().multiply(BigDecimal.valueOf(projecao.getProducaoMensal())).setScale(1, RoundingMode.UP).doubleValue());
			double faturamentoMensalAtual = precoLeite.getValor().multiply(BigDecimal.valueOf(getProducaoDiariaAtual() * LocalDate.now().lengthOfMonth())).doubleValue();
			projecao.setPercentualVariacaFaturamentoMensal(BigDecimal.valueOf((( projecao.getFaturamentoMensal() - faturamentoMensalAtual ) * 100) / faturamentoMensalAtual).setScale(1, RoundingMode.UP).doubleValue());
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
	
	
}
