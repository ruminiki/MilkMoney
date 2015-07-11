package br.com.milksys.service;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.dao.MorteAnimalDao;
import br.com.milksys.model.CausaMorteAnimal;
import br.com.milksys.model.MorteAnimal;

@Service
public class MorteAnimalService implements IService<Integer, MorteAnimal>{

	@Autowired private MorteAnimalDao dao;
	@Autowired private CausaMorteAnimalService causaMorteAnimalService;

	@Override
	public boolean save(MorteAnimal entity) {
		return dao.persist(entity);
	}

	@Override
	public boolean remove(MorteAnimal entity) {
		return dao.remove(entity);
	}

	@Override
	public MorteAnimal findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<MorteAnimal> findAll() {
		return dao.findAll(MorteAnimal.class);
	}
	
	public ObservableList<MorteAnimal> findAllAsObservableList() {
		ObservableList<MorteAnimal> list = FXCollections.observableArrayList();
		list.addAll(dao.findAll(MorteAnimal.class));
		return list;
	}
	
	@Override
	public ObservableList<MorteAnimal> defaultSearch(String param) {
		return null;
	}
    
    public ObservableList<Series<String, Number>> getDataChart(){

    	ObservableList<Series<String, Number>> series = FXCollections.observableArrayList();
    	
    	XYChart.Series<String, Number> serie;
    	
    	for ( CausaMorteAnimal c : causaMorteAnimalService.findAll() ){
    		serie = new XYChart.Series<String, Number>();
    		//serie.setName(c.getDescricao());
            serie.getData().add(new XYChart.Data<String, Number>(c.getDescricao(), dao.countByCausa(c.getDescricao())));
            series.add(serie);
    	}
    	
    	return series;
    	
    }

	@Override
	public void validate(MorteAnimal entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}
