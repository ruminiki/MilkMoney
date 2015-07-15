package br.com.milksys.controller.painel;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.service.MorteAnimalService;

@Controller
public class CausaMorteAnimalChartController {

	@FXML private VBox                      group;
	@Autowired MorteAnimalService           morteAnimalService;
	
	private final CategoryAxis              yAxis     = new CategoryAxis();
    private final NumberAxis                xAxis     = new NumberAxis();
	private final BarChart<Number,String>   barChart  = new BarChart<>(xAxis,yAxis);
	
	@FXML
	public void initialize() {

		xAxis.setLabel("Causa");
        yAxis.setLabel("Quantidade");

        barChart.setPrefHeight(200);
        barChart.setPrefWidth(200);
        
        barChart.setTitle("Principais Causas Mortes");
        barChart.setLegendVisible(false);
        
        barChart.getData().clear();
        barChart.getData().addAll(morteAnimalService.getDataChart());
        
        xAxis.setTickLabelRotation(90);
        xAxis.setTickLabelsVisible(true);
        
        VBox.setVgrow(barChart, Priority.SOMETIMES);
        HBox.setHgrow(barChart, Priority.SOMETIMES);
        
        group.getChildren().add(barChart);
        
	}
	
	public String getFormName(){
		return "view/painel/CausaMorteAnimalChart.fxml";
	}

}
