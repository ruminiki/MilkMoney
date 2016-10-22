package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	public ObservableList<Lote> defaultSearch(String param, int limit) {
		return FXCollections.observableArrayList(dao.defaultSearch(param));
	}

	@Override
	public void validate(Lote entity) {
		LoteValidation.validate(entity);
	}

	public Float getMediaIdadeAnimais(List<Animal> animais) {
		
		float somaIdade = 0F;
		
		for ( Animal animal : animais ){
		
			somaIdade += animal.getIdade();
			
		}
		
		return BigDecimal.valueOf(somaIdade > 0 ? somaIdade / animais.size() : somaIdade)
				.setScale(2, RoundingMode.HALF_EVEN)
				.floatValue();
		
	}

	public Float getMediaLactacoesAnimais(List<Animal> animais) {
		
		float somaLactacoes = 0F;
		
		for ( Animal animal : animais ){
		
			somaLactacoes += lactacaoDao.countByAnimal(animal);
			
		}
		
		return BigDecimal.valueOf(somaLactacoes > 0 ? somaLactacoes / animais.size() : somaLactacoes)
				.setScale(2, RoundingMode.HALF_EVEN)
				.floatValue();
	}

	public Float getMediaProducaoAnimais(List<Animal> animais) {
		
		float mediaAnimal = 0F;
		
		for ( Animal animal : animais ){
			mediaAnimal += producaoIndividualService.getMediaProducaoAnimal(animal);
		}
		
		return BigDecimal.valueOf(mediaAnimal > 0 ? mediaAnimal / animais.size() : mediaAnimal)
				.setScale(2, RoundingMode.HALF_EVEN)
				.floatValue();
	}

	public String getNomeLotes(Animal animal) {
		
		List<Lote> lotes = dao.findByAnimal(animal);
		String nomeLotes = "";
		for ( Lote lote : lotes ){
			nomeLotes += lote.getDescricao() + "|";
		}
		
		if ( !nomeLotes.isEmpty() ) {
			nomeLotes = nomeLotes.substring(0, (nomeLotes.length() - 1));
		}else{
			nomeLotes = "--";
		}
		
		return nomeLotes;
	}
	
	
}
