package br.com.milkmoney.controller.painel;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.indicador.IndicadorOverviewController;

@Controller
public class PainelController {
	
	//declarations
	@FXML private VBox group;
	@Autowired private EficienciaReprodutivaMapController eficienciaReprodutivaMapController;
	@Autowired private IndicadorOverviewController painelIndicadoresOverviewController;
	@Autowired private CausaMorteAnimalChartController causaMorteAnimalChartController;
	@Autowired private FinanceiroChartController financeiroChartController;
	
	@FXML
	public void initialize() {

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
		financeiroChartController.showForm();
	}
	
	@FXML 
	private void handlePainelIndicadores(){
		painelIndicadoresOverviewController.showForm();
	}
	
	@FXML 
	private void handleEficienciaReprodutivaChart(){
		eficienciaReprodutivaMapController.showForm();
	}
	
	@FXML
	private void handleCausaMorteAnimalChart(){
		causaMorteAnimalChartController.showForm();
	}
	
}
