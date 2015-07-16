package br.com.milksys.service;

import java.util.List;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.EncerramentoLactacaoDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.Parto;
import br.com.milksys.validation.EncerramentoLactacaoValidation;

@Service
public class EncerramentoLactacaoService implements IService<Integer, EncerramentoLactacao>{

	@Autowired private EncerramentoLactacaoDao dao;
	@Autowired private CoberturaService coberturaService;
	@Autowired private PartoService partoService;

	@Override
	@Transactional
	public boolean save(EncerramentoLactacao entity) {
		
		this.validate(entity);
		return dao.persist(entity);
		
	}
	
	private Function<Animal, Boolean> isFemeaCoberta = animal -> {
		
		Cobertura coberturaAtiva = coberturaService.findCoberturaAtivaByAnimal(animal);
		return coberturaAtiva != null;
		
	};
	
	@Override
	@Transactional
	public boolean remove(EncerramentoLactacao entity) {
		return dao.remove(entity);
	}

	@Override
	public EncerramentoLactacao findById(Integer id) {
		return dao.findById(EncerramentoLactacao.class, id);
	}

	@Override
	public List<EncerramentoLactacao> findAll() {
		return dao.findAll(EncerramentoLactacao.class);
	}
	
	public ObservableList<EncerramentoLactacao> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(EncerramentoLactacao.class));
	}
	
	@Override
	public ObservableList<EncerramentoLactacao> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(EncerramentoLactacao entity) {
		Parto ultimoParto = partoService.findLastParto(entity.getAnimal());
		EncerramentoLactacaoValidation.validaUltimoParto(entity, ultimoParto);
		EncerramentoLactacaoValidation.validate(entity);
		EncerramentoLactacaoValidation.validaFemeaCoberta(entity, isFemeaCoberta);
	}
	
	@Transactional
	public void removeLastByAnimal(Animal animal) {
		dao.removeLastByAnimal(animal);
	}
	
}
