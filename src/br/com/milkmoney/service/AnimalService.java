package br.com.milkmoney.service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.AnimalDao;
import br.com.milkmoney.dao.CoberturaDao;
import br.com.milkmoney.dao.PartoDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.AnimalValidation;

@Service
public class AnimalService implements IService<Integer, Animal>{

	@Autowired private AnimalDao dao;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private PartoDao partoDao;
	@Autowired private CoberturaDao coberturaDao;

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
		return FXCollections.observableArrayList(dao.findAllByNumeroNome(param));
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
	
	public ObservableList<Lactacao> findLactacoesAnimal(Animal animal) {
		return FXCollections.observableArrayList(lactacaoService.findLactacoesAnimal(animal));
	}

	public Long getNumeroPartos(Animal animal) {
		return partoDao.countByAnimal(animal);
	}

	public boolean isInLactacao(Date data, Animal animal) {
		return dao.isInLactacao(data, animal);
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
