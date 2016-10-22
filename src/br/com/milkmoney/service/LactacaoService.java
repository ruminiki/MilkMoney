package br.com.milkmoney.service;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.LactacaoDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.validation.LactacaoValidation;

@Service
public class LactacaoService implements IService<Integer, Lactacao>{

	@Autowired private LactacaoDao dao;
	@Autowired private CoberturaService coberturaService;
	@Autowired private ProducaoIndividualService producaoIndividualService;

	@Override
	@Transactional
	public boolean save(Lactacao entity) {
		
		LactacaoValidation.validate(entity);
		//LactacaoValidation.validaFemeaCoberta(entity, isFemeaCoberta);
		return dao.persist(entity);
		
	}
	
	/*private Function<Lactacao, Boolean> isFemeaCoberta = lactacao -> {
		
		Cobertura coberturaAnteriorLactacao = coberturaService.findPrimeiraCoberturaNaoVaziaAposData(lactacao.getAnimal(), lactacao.getDataInicio());
		return coberturaAtiva != null;
		
	};*/
	
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
	public ObservableList<Lactacao> defaultSearch(String param, int limit) {
		return null;
	}

	@Override
	public void validate(Lactacao entity) {
		
	}
	
	public Lactacao findUltimaLactacaoAnimal(Animal animal){
		return dao.findUltimaLactacaoAnimal(animal);
	}
	
	@Transactional
	public void desfazerEncerramentoLactacao(Lactacao lactacao) {
		
		lactacao.setDataFim(null);
		lactacao.setMotivoEncerramentoLactacao(null);
		
		dao.persist(lactacao);
		
	}

	public ObservableList<Lactacao> findLactacoesAnimal(Animal animal) {
		
		List<Lactacao> lactacoes = dao.findByAnimal(animal);
		
		for ( Lactacao lactacao : lactacoes ){
			lactacao.setMediaProducao(producaoIndividualService.getMediaAnimalPeriodo(
					animal, lactacao.getDataInicio(), lactacao.getDataFim() == null ? new Date() : lactacao.getDataFim()));
		}
		
		return FXCollections.observableArrayList(lactacoes);
	}

	public Lactacao findLactacaoAnimal(Animal animal, Date data) {
		return dao.findLactacaoAnimal(animal, data);
	}

	public Lactacao findLastBeforeDate(Animal animal, Date data) {
		return dao.findLastBeforeDate(animal, data);
	}

	public Long countLactacoesAnimal(Animal animal) {
		return dao.countLactacoesAnimal(animal);
	}

	public List<Lactacao> findAllWithPrevisaoEncerramentoIn(Date dataInicio, Date dataFim) {
		return dao.findAllWithPrevisaoEncerramentoIn(dataInicio, dataFim);
	}
	
}
