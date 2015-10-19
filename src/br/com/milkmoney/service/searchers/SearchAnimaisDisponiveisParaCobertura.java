package br.com.milkmoney.service.searchers;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.AnimalService;

@Service
public class SearchAnimaisDisponiveisParaCobertura extends Search<Integer, Animal> {
	
	@Autowired private AnimalService animalService;
	
	@Override
	public ObservableList<Animal> doSearch(Object ...objects) {
		
		Date dataInicio = new Date();
		Date dataFim = new Date();
		
		//busca os animais disponíveis no período
		List<Animal> animaisDisponiveis = animalService.findAnimaisDisponiveisParaCobertura(dataInicio, dataFim);
		return FXCollections.observableArrayList(animaisDisponiveis);
		
	}
	
}
