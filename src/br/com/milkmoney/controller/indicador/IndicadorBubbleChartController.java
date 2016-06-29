package br.com.milkmoney.controller.indicador;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.service.FichaAnimalService;


@Controller
public class IndicadorBubbleChartController {

	@FXML private VBox vBoxChart;
	@Autowired private FichaAnimalService fichaAnimalService;
	private LineChart<String,Number> linearChart;
	
	@FXML
	public void initialize() {
		
		//chart
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Animais");
        
        linearChart = new LineChart<String,Number>(xAxis,yAxis);
        
        linearChart.setTitle("Indicador");
        linearChart.setLegendVisible(true);
        
        VBox.setVgrow(linearChart, Priority.SOMETIMES);
        HBox.setHgrow(linearChart, Priority.SOMETIMES);
        
        linearChart.getData().clear();
        linearChart.getData().add(fichaAnimalService.mountDataChart(fichaAnimalService.generateFichaForAll(fichaAnimalService.getField(FichaAnimalService.EFICIENCIA_REPRODUTIVA_ANIMAL))));
        
        vBoxChart.getChildren().add(linearChart);
        
       // generateChart();
        
	}
	
	private void generateChart(){
		
		vBoxChart.setDisable(true);
		
		Task<Void> t = new Task<Void>() {
			
			@Override
			public Void call() throws InterruptedException {
				
		        fichaAnimalService.generateFichaForAll(fichaAnimalService.getField(FichaAnimalService.EFICIENCIA_REPRODUTIVA_ANIMAL));
		        linearChart.getData().clear();
		        linearChart.getData().add(fichaAnimalService.mountDataChart(fichaAnimalService.generateFichaForAll(fichaAnimalService.getField(FichaAnimalService.EFICIENCIA_REPRODUTIVA_ANIMAL))));
		        
				return null;
			}
		};
			
		t.setOnSucceeded(e -> {
			vBoxChart.setDisable(false);
		});
		
		Thread thread = new Thread(t);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void showForm() {	
		AnchorPane form = (AnchorPane) MainApp.load(getFormName());
		Stage dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);
		dialogStage.setResizable(false);
		Scene scene = new Scene(form);
		dialogStage.setScene(scene);
		dialogStage.show();
	}
	
	public String getFormName(){
		return "view/indicador/IndicadorBubbleChart.fxml";
	}

	public String getFormTitle() {
		return "Gráfico de Dispersão";
	}
	
}
