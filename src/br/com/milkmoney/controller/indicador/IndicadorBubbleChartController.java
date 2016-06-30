package br.com.milkmoney.controller.indicador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
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
import br.com.milkmoney.components.HoveredThresholdNode;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.controller.indicador.renderer.PopUpWait;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;


@Controller
public class IndicadorBubbleChartController {

	@FXML private VBox vBoxChart;
	
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private AnimalService      animalService;
	@Autowired private RelatorioService   relatorioService;
	
	private LineChart<String,Number> linearChart;
	private List<FichaAnimal> fichas;
	private List<Animal> animais;
	
	@FXML
	public void initialize() {
		
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Animais");
        
        linearChart = new LineChart<String,Number>(xAxis,yAxis);
        
        linearChart.setTitle("Eficiência Reprodutiva");
        linearChart.setLegendVisible(false);
        
        VBox.setVgrow(linearChart, Priority.SOMETIMES);
        HBox.setHgrow(linearChart, Priority.SOMETIMES);

        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				generateChart();
			}
        });
        
	}

	private void generateChart(){
		
		vBoxChart.setDisable(true);
		PopUpWait pp = new PopUpWait("Aguarde...");
		
		Task<Void> t = new Task<Void>() {
			
			@Override
			public Void call() throws InterruptedException {
				fichas = new ArrayList<FichaAnimal>();
				animais = animalService.findAllFemeasAtivas(DateUtil.today);
						
				double progressComplete = animais.size();
				double index = 0;
				
				for ( Animal animal : animais ){
					fichas.add(fichaAnimalService.generateFichaAnimal(animal, fichaAnimalService.getField(FichaAnimalService.EFICIENCIA_REPRODUTIVA_ANIMAL)));
					updateProgress(index++, progressComplete);
				}
				
				Collections.sort(fichas, new Comparator<FichaAnimal>() {
					public int compare(FichaAnimal f1, FichaAnimal f2) {
						return f1.getEficienciaReprodutiva().compareTo(f2.getEficienciaReprodutiva());
					}
				});
				
				Series<String, Number> serie = fichaAnimalService.mountDataChart(fichas);
				for ( XYChart.Data<String, Number> data : serie.getData() ){
					data.setNode(new HoveredThresholdNode(data.getXValue() + "(" + data.getYValue() + "%)"));
				}
				
		        linearChart.getData().add(serie);
		        
				return null;		
				
			}
		};
			
		Thread thread = new Thread(t);
		thread.setDaemon(true);
		thread.start();
		
		t.setOnSucceeded(e -> {
			vBoxChart.getChildren().add(linearChart);
			vBoxChart.setDisable(false);
			pp.hide();
		});
		
		pp.getProgressBar().progressProperty().bind(t.progressProperty());
		pp.show(vBoxChart.getScene().getWindow());
	}
	
	@FXML
	private void imprimirRankingEficiencia(){
		//os ids dos animais selecionados são passados como parâmetro
		StringBuilder sb = new StringBuilder();
		
		for ( Animal animal : animais ){
			sb.append(animal.getId());
			sb.append(",");
		}
		
		sb.setLength(sb.length() - 1);
		
		Object[] params = new Object[]{
			animais,
			sb.toString()
		};
		WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_RANKING_ANIMAIS, params), MainApp.primaryStage);
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
