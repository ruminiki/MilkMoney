package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.LactacaoDao;
import br.com.milkmoney.dao.LoteDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lote;
import br.com.milkmoney.validation.LoteValidation;

@Service
public class LoteService implements IService<Integer, Lote>{

	@Autowired private LoteDao dao;
	@Autowired private LactacaoDao lactacaoDao;
	@Autowired private ProducaoIndividualService producaoIndividualService;

	@Override
	@Transactional
	public boolean save(Lote entity) {
		LoteValidation.validate(entity);
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(Lote entity) {
		return dao.remove(entity);
	}

	@Override
	public Lote findById(Integer id) {
		return dao.findById(Lote.class, id);
	}

	@Override
	public List<Lote> findAll() {
		return dao.findAll(Lote.class);
	}
	
	public ObservableList<Lote> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Lote.class));
	}
	
	@Override
	public ObservableList<Lote> defaultSearch(String param) {
		return FXCollections.observableArrayList(dao.findByDescricao(param));
	}

	@Override
	public void validate(Lote entity) {
		LoteValidation.validate(entity);
	}

	public Float getMediaIdadeAnimais(Lote lote) {
		
		float somaIdade = 0F;
		
		for ( Animal animal : lote.getAnimais() ){
		
			somaIdade += animal.getIdade();
			
		}
		
		return somaIdade > 0 ? somaIdade / lote.getAnimais().size() : somaIdade;
		
	}

	public Float getMediaLactacoesAnimais(Lote lote) {
		
		float somaLactacoes = 0F;
		
		for ( Animal animal : lote.getAnimais() ){
		
			somaLactacoes += lactacaoDao.countByAnimal(animal);
			
		}
		
		return somaLactacoes > 0 ? somaLactacoes / lote.getAnimais().size() : somaLactacoes;
	}

	public Float getMediaProducaoAnimais(Lote lote) {
		
		float mediaAnimal = 0F;
		
		for ( Animal animal : lote.getAnimais() ){
			mediaAnimal += producaoIndividualService.getMediaProducaoAnimal(animal);
		}
		
		return mediaAnimal > 0 ? mediaAnimal / lote.getAnimais().size() : mediaAnimal;
	}
	
	
}
