package br.com.milkmoney.controller.painel;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.service.PrecoLeiteService;

@Controller
public class PrecoLeiteChartController {

	@FXML private VBox group;
	@FXML private UCTextField inputAno;
	@FXML private Button btnBuscarDadosGrafico;
	
	@Autowired PrecoLeiteService precoLeiteService;
	
	@FXML
	public void initialize() {

		MaskFieldUtil.numeroInteiro(inputAno);
		inputAno.setText(String.valueOf(LocalDate.now().getYear()));
		
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Meses");
        
        final LineChart<String, Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
        
        lineChart.setTitle("Variação Preço do Leite");
        lineChart.setLegendVisible(true);
        
        
        VBox.setVgrow(lineChart, Priority.SOMETIMES);
        HBox.setHgrow(lineChart, Priority.SOMETIMES);
        
        lineChart.getData().addAll(precoLeiteService.getDataChart(Integer.parseInt(inputAno.getText())));
        group.getChildren().add(lineChart);
        
        btnBuscarDadosGrafico.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				lineChart.getData().clear();
				lineChart.getData().addAll(precoLeiteService.getDataChart(Integer.parseInt(inputAno.getText())));
			}
			
		});
        
        inputAno.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					lineChart.getData().clear();
					lineChart.getData().addAll(precoLeiteService.getDataChart(Integer.parseInt(inputAno.getText())));
				}

			}

		});
        
	}
	
	
	
	
	public String getFormName(){
		return "view/painel/PrecoLeiteChart.fxml";
	}

}
