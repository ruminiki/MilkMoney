package br.com.milkmoney.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.MorteAnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.CausaMorteAnimal;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.validation.MorteAnimalValidation;

@Service
public class MorteAnimalService implements IService<Integer, MorteAnimal>{

	@Autowired private MorteAnimalDao dao;
	@Autowired private CausaMorteAnimalService causaMorteAnimalService;

	@Override
	@Transactional
	public boolean save(MorteAnimal entity) {
		return dao.persist(entity);
	}

	@Override
	@Transactional
	public boolean remove(MorteAnimal entity) {
		return dao.remove(entity);
	}
	
	@Transactional
	public void removeByAnimal(Animal animal) {
		dao.removeByAnimal(animal);
	}
	
	@Override
	public MorteAnimal findById(Integer id) {
		return dao.findById(MorteAnimal.class, id);
	}

	@Override
	public List<MorteAnimal> findAll() {
		return dao.findAll(MorteAnimal.class);
	}
	
	public ObservableList<MorteAnimal> findAllAsObservableList() {
		return FXCollections.observableArrayList(dao.findAll(MorteAnimal.class));
	}
	
	@Override
	public ObservableList<MorteAnimal> defaultSearch(String param, int limit) {
		return null;
	}
    
    public ObservableList<PieChart.Data> getDataChart(){

    	ObservableList<PieChart.Data> series = FXCollections.observableArrayList();
    	for ( CausaMorteAnimal c : causaMorteAnimalService.findAll() ){
    		series.add(new PieChart.Data(c.getDescricao(), dao.countByCausa(c.getDescricao())));
    	}
    	return series;
    	
    }

	@Override
	public void validate(MorteAnimal entity) {
		MorteAnimalValidation.validate(entity);
	}

	public MorteAnimal findByAnimal(Animal animal) {
		return dao.findByAnimal(animal);
	}
	
	
}
