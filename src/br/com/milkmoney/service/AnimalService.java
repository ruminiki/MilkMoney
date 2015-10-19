package br.com.milkmoney.service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.ParametroDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.AnimalValidation;

@Service
public class AnimalService implements IService<Integer, Animal>{

	@Autowired private AnimalDao dao;
	@Autowired private PartoDao partoDao;
	@Autowired private CoberturaDao coberturaDao;
	@Autowired private ParametroDao parametroDao;

	@Override
	@Transactional
	public boolean save(Animal entity) {
		AnimalValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Animal entity) {
		return dao.remove(entity);
	}

	@Override
	public Animal findById(Integer id) {
		return dao.findById(Animal.class, id);
	}

	@Override
	public List<Animal> findAll() {
		return dao.findAll(Animal.class);
	}

	public ObservableList<Animal> findAllAsObservableList() {
		ObservableList<Animal> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(Animal.class));
		return list;
	}
	
	@Override
	public ObservableList<Animal> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.defultSearch(param));
	}

	public ObservableList<Animal> findAllFemeasAtivasAsObservableList() {
		return FXCollections.observableArrayList(dao.findAllFemeasAtivas());
	}

	public ObservableList<Animal> findAllReprodutoresAsObservableList() {
		return findAllAsObservableList();
	}

	@Override
	public void validate(Animal entity) {
		AnimalValidation.validate(entity);
	}

	public Long countAnimaisEmLactacao(Date data) {
		return dao.contaAnimaisEmLactacao(data);
	}
	
	public Long getNumeroPartos(Animal animal) {
		return partoDao.countByAnimal(animal);
	}
	
	public boolean isInLactacao(Date data, Animal animal) {
		return dao.isInLactacao(data, animal);
	}
	
	/*
	 * Vacas disponíveis para serem cobertas:
	 * (1) não vendidas, 
	 * (2) não mortas, 
	 * (3) que não estejam cobertas(prenhas)
	 * (3) não são recém paridas,  
	 * (4) tem idade suficiente para cobertura
	 */
	public List<Animal> findAnimaisDisponiveisParaCobertura(Date dataInicio, Date dataFim) {
			
		List<Animal> result = new ArrayList<Animal>();
		
		int diasIdadeMinimaParaCobertura = 0;
		try{
			//o parametro estara em meses, multiplicar por 30 para obter os dias
			diasIdadeMinimaParaCobertura = Integer.parseInt(parametroDao.findBySigla(Parametro.IDADE_MINIMA_PARA_COBERTURA)) * 30;
		}catch(Exception e){
			diasIdadeMinimaParaCobertura = 24 * 30;
		}
		
		int periodoVoluntarioEspera = 0;
		try{
			periodoVoluntarioEspera = Integer.parseInt(parametroDao.findBySigla(Parametro.PERIODO_VOLUNTARIO_ESPERA));
		}catch(Exception e){
			periodoVoluntarioEspera = 40;//default 40 dias
		}
		
		//busca animais que tinha idade suficiente e não estavam mortos nem vendidos
		List<Animal> animais = dao.findAnimaisAtivosComIdadeParaServicoNoPeriodo(diasIdadeMinimaParaCobertura, dataInicio, dataFim);
		
		for ( Animal animal : animais ){
			//buscar ultima cobertura antes data inicio
			Cobertura cobertura = coberturaDao.findFirstBeforeDate(animal, dataInicio);
			if ( cobertura != null ){
				//se a cobertura tiver parto, verificar se a data do parto + pve fica dentro ou antes do periodo inicial
				if ( cobertura.getParto() != null ){
					if ( DateUtil.asLocalDate(cobertura.getParto().getData()).plusDays(periodoVoluntarioEspera).compareTo(DateUtil.asLocalDate(dataFim)) <= 0 ){
						result.add(animal);
					}
					continue;
				}
				//se não tiver parto verificar se estiver vazia
				if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.VAZIA) ){
					//se sim, somar a data de confirmacao + 21 dias e verificar se está dentro ou antes do periodo
					if ( DateUtil.asLocalDate(cobertura.getDataConfirmacaoPrenhes()).plusDays(21).compareTo(DateUtil.asLocalDate(dataFim)) <= 0 ){
						result.add(animal);
					}
					continue;
				}
				
				//se não tiver confirmação, verificar se a data inicial + o tempo de gestação + pve fica antes ou dentro do periodo
				if ( DateUtil.asLocalDate(cobertura.getData()).plusDays(282 + periodoVoluntarioEspera).compareTo(DateUtil.asLocalDate(dataFim)) <= 0 ){
					result.add(animal);
				}
				
				continue;
				
			}else{
				result.add(animal);
			}
			
		}
		
		return result;
	}

	public ObservableList<PieChart.Data> getChartData(){
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data(SituacaoAnimal.EM_LACTACAO, dao.countAllFemeasEmLactacao().doubleValue()),
                new PieChart.Data(SituacaoAnimal.SECO, dao.countAllFemeasSecas().doubleValue()));
        return pieChartData;
	}
	
	public int getIdadePrimeiroParto(Animal animal) {
		Parto parto = partoDao.findFirstParto(animal);
		int idadePrimeiroParto = 0;
		if ( parto != null ){
			idadePrimeiroParto = (int) ChronoUnit.MONTHS.between(DateUtil.asLocalDate(animal.getDataNascimento()), DateUtil.asLocalDate(parto.getData()));
		}
		return idadePrimeiroParto;
	}
	
	public int getIdadePrimeiraCobertura(Animal animal) {
		Cobertura cobertua = coberturaDao.findFirstCobertura(animal);
		int idadePrimeiroCobertura = 0;
		if ( cobertua != null ){
			idadePrimeiroCobertura = (int) ChronoUnit.MONTHS.between(DateUtil.asLocalDate(animal.getDataNascimento()), DateUtil.asLocalDate(cobertua.getData()));
		}
		return idadePrimeiroCobertura;
	}

}
