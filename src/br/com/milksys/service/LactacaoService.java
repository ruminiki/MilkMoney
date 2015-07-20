package br.com.milksys.service;

import java.util.List;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.LactacaoDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.Lactacao;
import br.com.milksys.validation.LactacaoValidation;

@Service
public class LactacaoService implements IService<Integer, Lactacao>{

	@Autowired private LactacaoDao dao;
	@Autowired private CoberturaService coberturaService;

	@Override
	@Transactional
	public boolean save(Lactacao entity) {
		
		LactacaoValidation.validate(entity);
		LactacaoValidation.validaFemeaCoberta(entity, isFemeaCoberta);
		
		return dao.persist(entity);
		
	}
	
	private Function<Animal, Boolean> isFemeaCoberta = animal -> {
		
		Cobertura coberturaAtiva = coberturaService.findCoberturaAtivaByAnimal(animal);
		return coberturaAtiva != null;
		
	};
	
	@Override
	@Transactional
	public boolean remove(Lactacao entity) {
		return dao.remove(entity);
	}

	@Override
	public Lactacao findById(Integer id) {
		return dao.findById(Lactacao.class, id);
	}

	@Override
	public List<Lactacao> findAll() {
		return dao.findAll(Lactacao.class);
	}
	
	public ObservableList<Lactacao> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(Lactacao.class));
	}
	
	@Override
	public ObservableList<Lactacao> defaultSearch(String param) {
		return null;
	}

	@Override
	public void validate(Lactacao entity) {
		
	}
	
	@Transactional
	public void desfazerEncerramentoLactacao(Animal animal) {
		
		Lactacao lactacao = dao.findUltimaLactacaoAnimal(animal);
		
		lactacao.setDataFim(null);
		lactacao.setMotivoEncerramentoLactacao(null);
		
		save(lactacao);
		
	}

	public List<Lactacao> findLactacoesAnimal(Animal animal) {
		return dao.findByAnimal(animal);
	}

}
