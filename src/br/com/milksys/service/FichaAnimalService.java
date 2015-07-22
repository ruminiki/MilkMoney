package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milksys.dao.FichaAnimalDao;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FichaAnimal;
import br.com.milksys.model.Sexo;

@Service
public class FichaAnimalService{

	@Autowired private FichaAnimalDao dao;
	@Autowired private CoberturaService coberturaService;
	@Autowired private PartoService partoService;
	@Autowired private AnimalService animalService;

	public ObservableList<FichaAnimal> findAllEventosByAnimal(Animal animal) {
		return FXCollections.observableArrayList(dao.findAllByAnimal(animal));
	}
	
	@Transactional
	public FichaAnimal generateFichaAnimal(Animal animal){
		
		FichaAnimal fichaAnimal = dao.findFichaAnimal(animal);
		
		if (fichaAnimal == null){
			fichaAnimal = new FichaAnimal(animal);
		}
		//ultima cobertura
		fichaAnimal.setDataUltimaCobertura(coberturaService.getDataUltimaCoberturaAnimal(animal));
		//número serviços até prenhez, baseado na última cobertura
		fichaAnimal.setNumeroServicosAtePrenhez(coberturaService.getNumeroServicosAtePrenhez(animal));
		//próximo serviço
		fichaAnimal.setProximoServico(coberturaService.getProximoServico(animal));
		//número de partos
		fichaAnimal.setNumeroPartos(partoService.countByAnimal(animal));
		//número de crias machos
		fichaAnimal.setNumeroCriasMacho(partoService.countCriasByAnimalAndSexo(animal, Sexo.MACHO));
		//número de crias fêmeas
		fichaAnimal.setNumeroCriasFemea(partoService.countCriasByAnimalAndSexo(animal, Sexo.FEMEA));
		//número de dias em aberto
		fichaAnimal.setDiasEmAberto(coberturaService.getDiasEmAberto(animal));
		//dias em lactação
		fichaAnimal.setDiasEmLactacao(partoService.getDiasEmLactacao(animal));
		//intervalo entre partos
		fichaAnimal.setIntervaloEntrePartos(partoService.getIntervaloEntrePartos(animal));
		//idade primeiro parto
		fichaAnimal.setIdadePrimeiroParto(animalService.getIdadePrimeiroParto(animal));
		//idade primeira cobertura
		fichaAnimal.setIdadePrimeiraCobertura(animalService.getIdadePrimeiraCobertura(animal));
		
		dao.persist(fichaAnimal);
		
		return fichaAnimal;
	}
	
	@Transactional
	public void generateFichaForAll(){
		
		List<Animal> animais = animalService.findAll();
		for ( Animal animal : animais ){
			generateFichaAnimal(animal);
		}
		
		
	}
	
	
}
