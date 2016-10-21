package br.com.milkmoney.controller.indicador;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.indicador.renderer.PopUpWait;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.util.DateUtil;


@Controller
public class EficienciaReprodutivaMapController {

	@FXML private Pane paneQuadrante;
	
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private AnimalService      animalService;
	
	private List<FichaAnimal> fichas;
	private List<Animal> animais;
	
	@FXML
	public void initialize() {
		
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				generateChart();
			}
        });
        
        paneQuadrante.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff944d, #99e600)");
        
	}

	private void generateChart(){
		
		PopUpWait pp = new PopUpWait("Aguarde...");
		
		Task<Void> t = new Task<Void>() {
			
			@Override
			public Void call() throws InterruptedException {
				fichas = new ArrayList<FichaAnimal>();
				animais = animalService.findAllFemeasAtivas(DateUtil.today);
						
				double progressComplete = animais.size();
				double index = 0;
				
				for ( Animal animal : animais ){
					fichas.add(fichaAnimalService.generateFichaAnimal(animal, 
							fichaAnimalService.getField(FichaAnimalService.EFICIENCIA_REPRODUTIVA_ANIMAL)));
					updateProgress(index++, progressComplete);
				}
				
				/*Collections.sort(fichas, new Comparator<FichaAnimal>() {
					public int compare(FichaAnimal f1, FichaAnimal f2) {
						return f1.getEficienciaReprodutiva().compareTo(f2.getEficienciaReprodutiva());
					}
				});*/

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
		pp.show(paneQuadrante.getScene().getWindow());
	}
	
	private void distribuiQuadrantes(){
		
	    for ( FichaAnimal ficha : fichas ){
	    
	    	//primeiro quadrante (escala 250 px por 250 px)
	    	if ( ficha.getEficienciaReprodutiva().intValue() <= 25 ){
    			Circle ponto = new Circle();
    			ponto.setLayoutY(25 - ficha.getEficienciaReprodutiva().doubleValue());
    			ponto.setLayoutX(ficha.getEficienciaReprodutiva().doubleValue() * 10);
    			ponto.setRadius(5);
    			ponto.setFill(Color.WHITE);
    			paneQuadrante.getChildren().add(ponto);
	    	}
	    	
	    	//segundo quadrante
	    	/*if ( ficha.getEficienciaReprodutiva().intValue() > 25 && ficha.getEficienciaReprodutiva().intValue() <= 50 ){
    			Circle ponto = new Circle();
    			ponto.setLayoutX(ficha.getEficienciaReprodutiva().doubleValue());
    			ponto.setLayoutY(ficha.getEficienciaReprodutiva().doubleValue());
    			ponto.setRadius(5);
    			ponto.setFill(Color.WHITE);
    			paneSegundoQuadrante.getChildren().add(ponto);
	    	}
	    	
	    	//terceiro quadrante
	    	if ( ficha.getEficienciaReprodutiva().intValue() > 50 && ficha.getEficienciaReprodutiva().intValue() <= 75 ){
    			Circle ponto = new Circle();
    			ponto.setLayoutX(ficha.getEficienciaReprodutiva().doubleValue());
    			ponto.setLayoutY(ficha.getEficienciaReprodutiva().doubleValue());
    			ponto.setRadius(5);
    			ponto.setFill(Color.WHITE);
    			paneTerceiroQuadrante.getChildren().add(ponto);
	    	}
	    	
	    	//quarto quadrante
	    	if ( ficha.getEficienciaReprodutiva().intValue() > 75 ){
    			Circle ponto = new Circle();
    			ponto.setLayoutX(ficha.getEficienciaReprodutiva().doubleValue());
    			ponto.setLayoutY(ficha.getEficienciaReprodutiva().doubleValue());
    			ponto.setRadius(5);
    			ponto.setFill(Color.WHITE);
    			paneQuartoQuadrante.getChildren().add(ponto);
	    	}*/

	    }
	    
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
		return "view/indicador/EficienciaReprodutivaMap.fxml";
	}

	public String getFormTitle() {
		return "Gráfico de Quadrantes";
	}
	
}
