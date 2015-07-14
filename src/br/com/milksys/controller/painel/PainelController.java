package br.com.milksys.controller.painel;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;

@Controller
public class PainelController {
	
	//declarations
	@FXML private VBox group;
	@Autowired IndicadorOverviewController indicadorOverviewController;
	@Autowired PrecoLeiteChartController precoLeiteChartController;
	
	@FXML
	public void initialize() {
		handleIndicadores();
	}
	
	
	//handlers
	@FXML
	private void handleIndicadores(){
		group.getChildren().clear();
		
		AnchorPane node = (AnchorPane) MainApp.load(indicadorOverviewController.getFormName());
		VBox.setVgrow(node, Priority.SOMETIMES);		
		group.getChildren().add(node);
		
	}
	
	
	//handlers
		@FXML
		private void handlePrecoLeiteChart(){
			group.getChildren().clear();
			
			AnchorPane node = (AnchorPane) MainApp.load(precoLeiteChartController.getFormName());
			
			VBox.setVgrow(node, Priority.SOMETIMES);
	        HBox.setHgrow(node, Priority.SOMETIMES);
	        
			group.getChildren().add(node);
			
		}
	
	//....
	
	

}
