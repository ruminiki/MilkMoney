package br.com.milkmoney.controller.painel;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;

@Controller
public class PainelController {
	
	//declarations
	@FXML private VBox group;
	@Autowired IndicadorOverviewController indicadorOverviewController;
	@Autowired PrecoLeiteChartController precoLeiteChartController;
	@Autowired ProducaoLeiteChartController producaoLeiteChartController;
	@Autowired EntregaLeiteChartController entregaLeiteChartController;
	@Autowired CausaMorteAnimalChartController causaMorteAnimalChartController;
	
	@FXML
	public void initialize() {
		handleIndicadores();
	}
	
	private void changeItem(AnchorPane node){
		
		group.getChildren().clear();
		VBox.setVgrow(node, Priority.SOMETIMES);
        HBox.setHgrow(node, Priority.SOMETIMES);
		group.getChildren().add(node);
		
	}
	
	//handlers
	@FXML
	private void handleIndicadores(){
		changeItem((AnchorPane) MainApp.load(indicadorOverviewController.getFormName()));
	}
	
	
	@FXML
	private void handlePrecoLeiteChart(){
		changeItem((AnchorPane) MainApp.load(precoLeiteChartController.getFormName()));
	}
	
	@FXML
	private void handleProducaoLeiteChart(){
		changeItem((AnchorPane) MainApp.load(producaoLeiteChartController.getFormName()));
	}
	
	@FXML
	private void handleEntregaLeiteChart(){
		changeItem((AnchorPane) MainApp.load(entregaLeiteChartController.getFormName()));
	}
	
	@FXML
	private void handleCausaMorteAnimalChart(){
		group.getChildren().clear();
		AnchorPane node = (AnchorPane) MainApp.load(causaMorteAnimalChartController.getFormName());
		//VBox.setVgrow(node, Priority.SOMETIMES);		
		group.getChildren().add(node);
	}
	
	//....
	
	

}
