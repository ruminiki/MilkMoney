package br.com.milkmoney.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.FichaAnimalDao;
import br.com.milkmoney.dao.LactacaoDao;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Dimensionamento;
import br.com.milkmoney.model.Lactacao;

@Service
public class DimensionamentoService{

	@Autowired private FichaAnimalDao dao;
	@Autowired private CoberturaDao coberturaDao;
	@Autowired private LactacaoDao lactacaoDao;
	@Autowired private PartoService partoService;
	@Autowired private AnimalService animalService;
	
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

	@Transactional
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
		List<Lactacao> lactacoes = lactacaoDao.findAllWithDurationIn(dataInicio, dataFim);
		
		
		return 	((lactacoes != null ? lactacoes.size() : 0) -
				(encerramentosPrevistos != null ? encerramentosPrevistos.size() : 0)) +
				(partosPrevistos != null ? partosPrevistos.size() : 0);

	}
	
	
	@Transactional
	public Dimensionamento generatePrevisao(int mesReferencia, int anoReferencia){
		Dimensionamento dimensionamento = new Dimensionamento(mesReferencia, anoReferencia);
		dimensionamento.setNumeroAnimaisLactacao(calculaNumeroAnimaisLactacao(mesReferencia, anoReferencia));
		
		return dimensionamento;
		
	}
	
	public List<Dimensionamento> getDimensionamentoPeriodo(int mesInicio, int mesFim, int anoInicio, int anoFim){
		
		List<Dimensionamento> dimensionamentos = new ArrayList<Dimensionamento>();
		do{
			
			dimensionamentos.add(generatePrevisao(mesInicio, anoInicio));
			
			if ( mesInicio == 12 ){
				mesInicio = 1;
				anoInicio++;
			}else{
				mesInicio++;
			}
			
		}while ( mesInicio + anoInicio != mesFim + anoFim);
		
		return dimensionamentos;
		
	}
	
	
}
