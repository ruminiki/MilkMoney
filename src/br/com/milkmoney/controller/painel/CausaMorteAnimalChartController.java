package br.com.milkmoney.controller.painel;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.controller.AbstractWindowPopUp;
import br.com.milkmoney.service.MorteAnimalService;

@Controller
public class CausaMorteAnimalChartController extends AbstractWindowPopUp{

	@FXML private VBox            group;
	@Autowired MorteAnimalService morteAnimalService;
	
	private PieChart chart;
	
	@FXML
	public void initialize() {

	    chart = new PieChart();
	       
        chart.setPrefHeight(200);
        chart.setPrefWidth(200);
        
        chart.setTitle("Principais Causas de Mortes");
        chart.setAnimated(true);
        
        chart.getData().clear();
        chart.getData().addAll(morteAnimalService.getDataChart());
        
        VBox.setVgrow(chart, Priority.SOMETIMES);
        HBox.setHgrow(chart, Priority.SOMETIMES);
        
        group.getChildren().add(chart);
        
	}
	
	public String getFormName(){
		return "view/painel/CausaMorteAnimalChart.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Gráfico Causa Morte Animais";
	}

}
