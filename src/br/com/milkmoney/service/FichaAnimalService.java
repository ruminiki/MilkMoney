package br.com.milkmoney.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.dao.FichaAnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.fichaAnimal.AbstractFichaAnimal;
import br.com.milkmoney.service.fichaAnimal.DataUltimaCobertura;
import br.com.milkmoney.service.fichaAnimal.DataUltimoParto;
import br.com.milkmoney.service.fichaAnimal.DiasEmAbertoAnimal;
import br.com.milkmoney.service.fichaAnimal.DiasEmLactacaoAnimal;
import br.com.milkmoney.service.fichaAnimal.EficienciaReprodutivaAnimal;
import br.com.milkmoney.service.fichaAnimal.EficienciaTempoProducao;
import br.com.milkmoney.service.fichaAnimal.EncerramentoLactacao;
import br.com.milkmoney.service.fichaAnimal.IdadePrimeiraCobertura;
import br.com.milkmoney.service.fichaAnimal.IdadePrimeiroParto;
import br.com.milkmoney.service.fichaAnimal.IntervaloEntrePartosAnimal;
import br.com.milkmoney.service.fichaAnimal.LoteAnimal;
import br.com.milkmoney.service.fichaAnimal.MediaProducaoUltimaLactacao;
import br.com.milkmoney.service.fichaAnimal.NumeroCriasFemea;
import br.com.milkmoney.service.fichaAnimal.NumeroCriasMacho;
import br.com.milkmoney.service.fichaAnimal.NumeroPartos;
import br.com.milkmoney.service.fichaAnimal.NumeroServicosAtePrenhes;
import br.com.milkmoney.service.fichaAnimal.ProximoParto;
import br.com.milkmoney.service.fichaAnimal.ProximoServico;
import br.com.milkmoney.service.fichaAnimal.SituacaoUltimaCobertura;
import br.com.milkmoney.service.fichaAnimal.UltimoProcedimento;

@Service
public class FichaAnimalService{

	@Autowired private FichaAnimalDao dao;
	@Autowired private AnimalService animalService;

	public ObservableList<FichaAnimal> findAllEventosByAnimal(Animal animal) {
		return FXCollections.observableArrayList(dao.findAllByAnimal(animal));
	}
	
	@Transactional
	public FichaAnimal generateFichaAnimal(Animal animal, List<AbstractFichaAnimal> fieldsToLoad){
		
		FichaAnimal fichaAnimal = dao.findFichaAnimal(animal);
		
		if (fichaAnimal == null){
			fichaAnimal = new FichaAnimal(animal);
		}
		
		for (AbstractFichaAnimal field : fieldsToLoad) {

			Object[] params = new Object[]{
		    		fichaAnimal,
		    		animal
		    };
		    
		    field.load(params);
		    
		}
		
		dao.persist(fichaAnimal);
		return fichaAnimal;
		
	}
	
	@Transactional
	public void generateFichaForAll(Object ... params){
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<AbstractFichaAnimal> fieldsToLoad = (List) params[0];
		
		if ( fieldsToLoad == null ){
			fieldsToLoad = getAllFields();
		}
		
		List<Animal> animais = animalService.findAll();
		for ( Animal animal : animais ){
			generateFichaAnimal(animal, fieldsToLoad);
		}
		
	}
	
	public List<AbstractFichaAnimal> getAllFields(){
		return Arrays.asList(new AbstractFichaAnimal[] {
				(AbstractFichaAnimal)MainApp.getBean(DataUltimaCobertura.class),
				(AbstractFichaAnimal)MainApp.getBean(DataUltimoParto.class),
				(AbstractFichaAnimal)MainApp.getBean(DiasEmAbertoAnimal.class),
				(AbstractFichaAnimal)MainApp.getBean(DiasEmLactacaoAnimal.class),
				(AbstractFichaAnimal)MainApp.getBean(EficienciaReprodutivaAnimal.class),
				(AbstractFichaAnimal)MainApp.getBean(EncerramentoLactacao.class),
				(AbstractFichaAnimal)MainApp.getBean(IdadePrimeiraCobertura.class),
				(AbstractFichaAnimal)MainApp.getBean(IdadePrimeiroParto.class),
				(AbstractFichaAnimal)MainApp.getBean(IntervaloEntrePartosAnimal.class),
				(AbstractFichaAnimal)MainApp.getBean(LoteAnimal.class),
				(AbstractFichaAnimal)MainApp.getBean(MediaProducaoUltimaLactacao.class),
				(AbstractFichaAnimal)MainApp.getBean(NumeroCriasFemea.class),
				(AbstractFichaAnimal)MainApp.getBean(NumeroCriasMacho.class),
				(AbstractFichaAnimal)MainApp.getBean(NumeroPartos.class),
				(AbstractFichaAnimal)MainApp.getBean(NumeroServicosAtePrenhes.class),
				(AbstractFichaAnimal)MainApp.getBean(ProximoParto.class),
				(AbstractFichaAnimal)MainApp.getBean(ProximoServico.class),
				(AbstractFichaAnimal)MainApp.getBean(SituacaoUltimaCobertura.class),
				(AbstractFichaAnimal)MainApp.getBean(UltimoProcedimento.class),
				(AbstractFichaAnimal)MainApp.getBean(EficienciaTempoProducao.class)
		});
	}
	
	@Transactional
	public List<FichaAnimal> generateFichaAnimal(List<Animal> animais, List<AbstractFichaAnimal> fieldsToLoad) {
		
		List<FichaAnimal> fichas = new ArrayList<FichaAnimal>();
		
		if ( fieldsToLoad == null ){
			fieldsToLoad = getAllFields();
		}
		
		for (Animal animal : animais){
			fichas.add(generateFichaAnimal(animal, fieldsToLoad));
		}
		
		return fichas;
		
		
	}
	
	
}
