package br.com.milksys.service;

import java.util.List;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.EncerramentoLactacaoDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.validation.EncerramentoLactacaoValidation;

@Service
public class EncerramentoLactacaoService implements IService<Integer, EncerramentoLactacao>{

	@Autowired private EncerramentoLactacaoDao dao;
	@Autowired private CoberturaService coberturaService;

	@Override
	public boolean save(EncerramentoLactacao entity) {
		
		EncerramentoLactacaoValidation.validate(entity);
		EncerramentoLactacaoValidation.validaSituacaoAnimal(entity.getAnimal());
		EncerramentoLactacaoValidation.validaFemeaCoberta(entity, isFemeaCoberta);
		
		return dao.persist(entity);
		
	}
	
	private Function<Animal, Boolean> isFemeaCoberta = animal -> {
		
		Cobertura coberturaAtiva = coberturaService.findCoberturaAtivaByAnimal(animal);
		return coberturaAtiva != null;
		
	};
	
	@Override
	public boolean remove(EncerramentoLactacao entity) {
		return dao.remove(entity);
	}

	@Override
	public EncerramentoLactacao findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<EncerramentoLactacao> findAll() {
		return dao.findAll(EncerramentoLactacao.class);
	}
	
	public ObservableList<EncerramentoLactacao> findAllAsObservableList() {
		ObservableList<EncerramentoLactacao> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(EncerramentoLactacao.class));
		return list;
	}
	
	@Override
	public ObservableList<EncerramentoLactacao> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(EncerramentoLactacao entity) {
		// TODO Auto-generated method stub
		
	}

	public void removeLastByAnimal(Animal animal) {
		dao.removeLastByAnimal(animal);
	}
	
}
