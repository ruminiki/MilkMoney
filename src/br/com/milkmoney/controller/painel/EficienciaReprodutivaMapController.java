package br.com.milkmoney.controller.painel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.controller.AbstractWindowPopUp;
import br.com.milkmoney.controller.indicador.renderer.PopUpWait;
import br.com.milkmoney.controller.painel.renderer.CurveFittedAreaChart;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.Limit;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class EficienciaReprodutivaMapController extends AbstractWindowPopUp{

	@FXML private VBox chartContainer;
	@FXML private Label lblPrimeiroQuadrante, lblSegundoQuadrante, lblTerceiroQuadrante, lblQuartoQuadrante;

	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private AnimalService animalService;

	private List<FichaAnimal> fichas;
	private List<Animal> animais;

	private CurveFittedAreaChart areaChart;
	
	@SuppressWarnings("serial")
	private LinkedHashMap<Integer, Double> pointsChart = new LinkedHashMap<Integer, Double>(){
		{
	        put(1,0D);
	        put(2,12.5D);
	        put(3,37.5D);
	        put(4,62.5D);
	        put(5,87.5D);
	        put(6,100D);
		}
	};
	
	@SuppressWarnings("serial")
	private LinkedHashMap<Integer, Double> pointsZero = new LinkedHashMap<Integer, Double>(){
		{
	        put(1,25D);
	        put(2,50D);
	        put(3,75D);
		}
	};
	
	@FXML
	public void initialize() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				calculaEficiencia();
			}
		});

		// gráfico
		final NumberAxis xAxis = new NumberAxis(0, 100, 5);
		final NumberAxis yAxis = new NumberAxis();

		xAxis.setUpperBound(100);
		yAxis.setUpperBound(100);

		xAxis.setLabel("Eficiência reprodutiva");

		areaChart = new CurveFittedAreaChart(xAxis, yAxis);
		areaChart.setLegendVisible(false);
		areaChart.setVerticalGridLinesVisible(false);
		areaChart.setVerticalZeroLineVisible(false);

		VBox.setVgrow(areaChart, Priority.SOMETIMES);
		HBox.setHgrow(areaChart, Priority.SOMETIMES);

		areaChart.getStylesheets().add("css/chart.css");
		chartContainer.getChildren().add(0, areaChart);
	}

	private void calculaEficiencia() {

		PopUpWait pp = new PopUpWait("Aguarde...");

		Task<Void> t = new Task<Void>() {

			@Override
			public Void call() throws InterruptedException {
				fichas = new ArrayList<FichaAnimal>();
				animais = animalService.findAllFemeasAtivas(DateUtil.today, Limit.UNLIMITED);

				double progressComplete = animais.size();
				double index = 0;

				for (Animal animal : animais) {
					fichas.add(fichaAnimalService.generateFichaAnimal(animal,fichaAnimalService
									.getField(FichaAnimalService.EFICIENCIA_REPRODUTIVA_ANIMAL)));
					updateProgress(index++, progressComplete);
				}

				Collections.sort(fichas, new Comparator<FichaAnimal>() {
					public int compare(FichaAnimal f1, FichaAnimal f2) {
						return f1.getEficienciaReprodutiva().compareTo(f2.getEficienciaReprodutiva());
					}
				});

				return null;

			}
		};

		Thread thread = new Thread(t);
		thread.setDaemon(true);
		thread.start();

		t.setOnSucceeded(e -> {
			generateChart();
			pp.hide();
		});

		pp.getProgressBar().progressProperty().bind(t.progressProperty());
		pp.show(chartContainer.getScene().getWindow());
	}

	private void generateChart() {

		double countPrimeiroQuadrante, countSegundoQuadrante, countTerceiroQuadrante, countQuartoQuadrante;
		countPrimeiroQuadrante = countSegundoQuadrante = countTerceiroQuadrante = countQuartoQuadrante = 0;
		
		Hashtable<Double, Double> mapPoints = new Hashtable<Double, Double>();

		// separa em 12 pontos no gráfico de área
		for (FichaAnimal ficha : fichas) {

			Double eficiencia = ficha.getEficienciaReprodutiva().doubleValue();

			if (eficiencia > 100) {
				eficiencia = 100D;
			}
			
			countPrimeiroQuadrante = eficiencia <= 25 ? countPrimeiroQuadrante + 1 : countPrimeiroQuadrante; 
			countSegundoQuadrante  = eficiencia > 25 && eficiencia <= 50 ? countSegundoQuadrante + 1 : countSegundoQuadrante;
			countTerceiroQuadrante = eficiencia > 50 && eficiencia <= 75 ? countTerceiroQuadrante + 1 : countTerceiroQuadrante;
			countQuartoQuadrante   = eficiencia > 75 ? countQuartoQuadrante + 1 : countQuartoQuadrante; 
			
			Double pointOnChart = getPointOfValue(eficiencia);

			// se já existir o ponto, conta quantos para subir na vertical
			if (mapPoints.containsKey(pointOnChart)) {
				Double countPoints = mapPoints.get(pointOnChart);
				mapPoints.put(pointOnChart, ++countPoints);
			} else {
				mapPoints.put(pointOnChart, 1.0);
			}

		}

		//se tiverem pontos no gráfico sem animais, tem que setar zero 0
		Collection<Double> points = pointsChart.values();
		for ( Double point : points ) {
			if ( !mapPoints.containsKey(point) ) {
				mapPoints.put(point, 0D);
			}
		}
		//marca os pontos que devem estar zerados - entre os quadrantes
		points = pointsZero.values();
		for ( Double point : points ) {
			if ( !mapPoints.containsKey(point) ) {
				mapPoints.put(point, 0D);
			}
		}
		
		XYChart.Series<Number, Number> serie = new XYChart.Series<Number, Number>();
		serie.setName("Percentual rebanho");

		// seta o percentual de animais em cada ponto
		Enumeration<Double> enumKey = mapPoints.keys();
		while (enumKey.hasMoreElements()) {

			Double key = enumKey.nextElement();
			Double val = mapPoints.get(key);
			Double percentual = ((val / fichas.size()) * 100);

			serie.getData().add(new XYChart.Data<Number, Number>(key, percentual));

		}
		
		areaChart.getData().clear();
		areaChart.getData().add(serie);
		
		lblPrimeiroQuadrante.setText(Math.round(countPrimeiroQuadrante/fichas.size()*100) + "%");
		lblSegundoQuadrante.setText(Math.round(countSegundoQuadrante/fichas.size()*100) + "%");
		lblTerceiroQuadrante.setText(Math.round(countTerceiroQuadrante/fichas.size()*100) + "%");
		lblQuartoQuadrante.setText(Math.round(countQuartoQuadrante/fichas.size()*100) + "%");

	}

	private Double getPointOfValue(Double eficiencia) {
		//Double chartScala = 100 / 9D; // quantos pontos terão no gráfico
		
		//precisa achar entre quais os dois pontos que a eficiência está
		
		Double  point0 = 0D;
		
		Collection<Double> points = pointsChart.values();
		for ( Double point : points ) {

			if ( Math.round(eficiencia) == point ){
				return point;
			}
			
			if ( Math.round(eficiencia) < point ){
				
				Double distancePoint0 = eficiencia - point0;
				Double distancePoint1 = point - eficiencia;
					
				return distancePoint0 < distancePoint1 ? point0 : point;
			}
		
			point0 = point;
			
		}
		return 0D;
		
	}

	public String getFormName() {
		return "view/painel/EficienciaReprodutivaMap.fxml";
	}

	public String getFormTitle() {
		return "Gráfico da Eficiência Reprodutiva";
	}

}
