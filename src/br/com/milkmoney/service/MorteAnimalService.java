package br.com.milkmoney.service;

import java.util.List;

import javax.transaction.Transactional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.MorteAnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.CausaMorteAnimal;
import br.com.milkmoney.model.MorteAnimal;

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
	public ObservableList<MorteAnimal> defaultSearch(String param) {
		return null;
	}
    
    public ObservableList<Series<Number, String>> getDataChart(){

    	ObservableList<Series<Number, String>> series = FXCollections.observableArrayList();
    	XYChart.Series<Number, String> serie;
    	for ( CausaMorteAnimal c : causaMorteAnimalService.findAll() ){
    		serie = new XYChart.Series<Number, String>();
            serie.getData().add(new XYChart.Data<Number, String>(dao.countByCausa(c.getDescricao()), c.getDescricao()));
            series.add(serie);
    	}
    	return series;
    	
    }

	@Override
	public void validate(MorteAnimal entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
