package br.com.milksys.controller.painel;

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

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.service.EntregaLeiteService;

@Controller
public class EntregaLeiteChartController {

	@FXML private VBox                      group;
	@FXML private UCTextField               inputAno;
	@FXML private Button                    btnBuscarDadosGrafico;
	
	@Autowired EntregaLeiteService          entregaLeiteService;
	
	private final CategoryAxis              xAxis     = new CategoryAxis();
    private final NumberAxis                yAxis     = new NumberAxis();
	private final LineChart<String, Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
	
	@FXML
	public void initialize() {

		MaskFieldUtil.numeroInteiro(inputAno);
		inputAno.setText(String.valueOf(LocalDate.now().getYear()));
		
        xAxis.setLabel("Meses");
        lineChart.setTitle("Variação Volume Entrega de Leite");
        lineChart.setLegendVisible(true);
        
        VBox.setVgrow(lineChart, Priority.SOMETIMES);
        HBox.setHgrow(lineChart, Priority.SOMETIMES);
        
        setChartData();
        
        group.getChildren().add(lineChart);
        
        btnBuscarDadosGrafico.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setChartData();
			}
			
		});
        
        inputAno.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					setChartData();
				}

			}

		});
        
	}
	
	private void setChartData(){
		lineChart.getData().clear();
		lineChart.getData().addAll(entregaLeiteService.getDataChart(Integer.parseInt(inputAno.getText())));
	}
	
	public String getFormName(){
		return "view/painel/EntregaLeiteChart.fxml";
	}

}
