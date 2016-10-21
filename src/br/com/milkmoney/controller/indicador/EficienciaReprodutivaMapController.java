package br.com.milkmoney.controller.indicador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.indicador.renderer.CurveFittedAreaChart;
import br.com.milkmoney.controller.indicador.renderer.PopUpWait;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class EficienciaReprodutivaMapController {

	@FXML private VBox chartContainer;
	@FXML private Label lblPrimeiroQuadrante, lblSegundoQuadrante, lblTerceiroQuadrante, lblQuartoQuadrante;

	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private AnimalService animalService;

	private List<FichaAnimal> fichas;
	private List<Animal> animais;

	private CurveFittedAreaChart areaChart;

	@FXML
	public void initialize() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				generateChart();
			}
		});

		// paneQuadrante.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff5c33, #66ff33)");

		// gr�fico
		final NumberAxis xAxis = new NumberAxis(0, 100, 5);
		final NumberAxis yAxis = new NumberAxis();

		xAxis.setUpperBound(100);
		yAxis.setUpperBound(100);

		xAxis.setLabel("Efici�ncia reprodutiva");

		areaChart = new CurveFittedAreaChart(xAxis, yAxis);
		areaChart.setLegendVisible(false);
		areaChart.setVerticalGridLinesVisible(false);
		areaChart.setVerticalZeroLineVisible(false);

		VBox.setVgrow(areaChart, Priority.SOMETIMES);
		HBox.setHgrow(areaChart, Priority.SOMETIMES);

		areaChart.getStylesheets().add("css/chart.css");
		chartContainer.getChildren().add(0, areaChart);
	}

	private void generateChart() {

		PopUpWait pp = new PopUpWait("Aguarde...");

		Task<Void> t = new Task<Void>() {

			@Override
			public Void call() throws InterruptedException {
				fichas = new ArrayList<FichaAnimal>();
				animais = animalService.findAllFemeasAtivas(DateUtil.today);

				double progressComplete = animais.size();
				double index = 0;

				for (Animal animal : animais) {
					fichas.add(fichaAnimalService.generateFichaAnimal(
							animal,
							fichaAnimalService
									.getField(FichaAnimalService.EFICIENCIA_REPRODUTIVA_ANIMAL)));
					updateProgress(index++, progressComplete);
				}

				Collections.sort(fichas, new Comparator<FichaAnimal>() {
					public int compare(FichaAnimal f1, FichaAnimal f2) {
						return f1.getEficienciaReprodutiva().compareTo(
								f2.getEficienciaReprodutiva());
					}
				});

				return null;

			}
		};

		Thread thread = new Thread(t);
		thread.setDaemon(true);
		thread.start();

		t.setOnSucceeded(e -> {
			distribuiQuadrantes();
			pp.hide();
		});

		pp.getProgressBar().progressProperty().bind(t.progressProperty());
		pp.show(chartContainer.getScene().getWindow());
	}

	private void distribuiQuadrantes() {

		/*
		 * double scala = paneQuadrante.getWidth() / 100; double height =
		 * paneQuadrante.getHeight(); double width = paneQuadrante.getWidth();
		 */

		int countPrimeiroQuadrante, countSegundoQuadrante, countTerceiroQuadrante, countQuartoQuadrante;

		double x, y;

		Hashtable<Double, Double> mapPoints = new Hashtable<Double, Double>();

		// separa em 12 pontos no gr�fico de �rea
		for (FichaAnimal ficha : fichas) {

			Double eficiencia = ficha.getEficienciaReprodutiva().doubleValue();

			if (eficiencia > 100) {
				eficiencia = 100D;
			}

			Double pointOnChart = getPointOfValue(eficiencia);

			// se j� existir o ponto, conta quantos para subir na vertical
			if (mapPoints.containsKey(pointOnChart)) {
				Double countPoints = mapPoints.get(pointOnChart);
				mapPoints.put(pointOnChart, ++countPoints);
			} else {
				mapPoints.put(pointOnChart, 1.0);
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

	}

	private Double getPointOfValue(Double eficiencia) {
		Double chartScala = 100 / 12D; // quantos pontos ter�o no gr�fico
		for (double i = chartScala; i <= 100; i = i + chartScala) {

			if (eficiencia <= i) {
				return (double) Math.round(i);
			}

		}
		return 0D;
	}

	public void showForm() {
		AnchorPane form = (AnchorPane) MainApp.load(getFormName());
		Stage dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.getIcons().add(
				new Image(ClassLoader
						.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);
		dialogStage.setResizable(false);
		Scene scene = new Scene(form);
		dialogStage.setScene(scene);
		dialogStage.show();
	}

	public String getFormName() {
		return "view/indicador/EficienciaReprodutivaMap.fxml";
	}

	public String getFormTitle() {
		return "Gr�fico de Quadrantes";
	}

}
