package br.com.milkmoney.controller.painel;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;

@Controller
public class PainelController {
	
	//declarations
	@FXML private VBox group;
	@Autowired PrecoLeiteChartController precoLeiteChartController;
	@Autowired ProducaoLeiteChartController producaoLeiteChartController;
	@Autowired EntregaLeiteChartController entregaLeiteChartController;
	@Autowired CausaMorteAnimalChartController causaMorteAnimalChartController;
	@Autowired FinanceiroChartController financeiroChartController;
	
	@FXML
	public void initialize() {
		handleFinanceiroChart();
	}
	
	private void changeItem(AnchorPane node){
		
		group.getChildren().clear();
		VBox.setVgrow(node, Priority.SOMETIMES);
        HBox.setHgrow(node, Priority.SOMETIMES);
		group.getChildren().add(node);
		
	}
	
	@FXML
	protected void closeForm(){
		if ( group != null ){
			Stage stage = (Stage)group.getScene().getWindow();
			// se for popup
			if ( stage.getModality().equals(Modality.APPLICATION_MODAL) ){
				((Stage)group.getScene().getWindow()).close();	
			}else{
				MainApp.resetLayout();
			}
		}
	}
	
	//handlers
	@FXML
	private void handleFinanceiroChart(){
		changeItem((AnchorPane) MainApp.load(financeiroChartController.getFormName()));
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
