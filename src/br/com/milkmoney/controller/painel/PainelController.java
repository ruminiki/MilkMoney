package br.com.milkmoney.controller.painel;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.indicador.IndicadorOverviewController;
import br.com.milkmoney.controller.painel.renderer.VBoxOption;

@Controller
public class PainelController {
	
	//declarations
	@FXML private HBox group;
	@Autowired private EficienciaReprodutivaMapController eficienciaReprodutivaMapController;
	@Autowired private IndicadorOverviewController painelIndicadoresOverviewController;
	@Autowired private CausaMorteAnimalChartController causaMorteAnimalChartController;
	@Autowired private FinanceiroChartController financeiroChartController;
	@Autowired private IndicadorBubbleChartController indicadorBubbleChartController;
	
	private static final String ICON_INDICADORES      = "img/indicadores_48.png";
	private static final String ICON_EFICIENCIA       = "img/eficiencia_individual_48.png";
	private static final String ICON_EFICIENCIA_GERAL = "img/eficiencia_geral_48.png";
	private static final String ICON_FINANCEIRO       = "img/financeiro_48.png";
	private static final String ICON_CAUSA_MORTE      = "img/causa_morte_48.png";
	
	@FXML
	public void initialize() {

		VBoxOption buttonIndicadores = new VBoxOption(ICON_INDICADORES, "Indicadores");
		buttonIndicadores.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		handlePainelIndicadores();
        	}
        });
		group.getChildren().add(buttonIndicadores);
		
		VBoxOption buttonEficienciaIndividual = new VBoxOption(ICON_EFICIENCIA, "Eficiência Reprodutiva (cálculo individual)");
		buttonEficienciaIndividual.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		handleEficienciaReprodutivaIndividualChart();
        	}
        });
		group.getChildren().add(buttonEficienciaIndividual);
		
		VBoxOption buttonEficienciaGeral= new VBoxOption(ICON_EFICIENCIA_GERAL, "Eficiência Reprodutiva (visão geral rebanho)");
		buttonEficienciaGeral.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		handleEficienciaReprodutivaGeralChart();
        	}
        });
		group.getChildren().add(buttonEficienciaGeral);
		
		VBoxOption buttonFinanceiro = new VBoxOption(ICON_FINANCEIRO, "Financeiro");
		buttonFinanceiro.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		handleFinanceiroChart();
        	}
        });
		group.getChildren().add(buttonFinanceiro);
		
		VBoxOption buttonCausaMorte= new VBoxOption(ICON_CAUSA_MORTE, "Causa Morte");
		buttonCausaMorte.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		handleCausaMorteAnimalChart();
        	}
        });
		group.getChildren().add(buttonCausaMorte);
		
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
	private void handleEficienciaReprodutivaGeralChart(){
		eficienciaReprodutivaMapController.showForm();
	}
	
	@FXML 
	private void handleEficienciaReprodutivaIndividualChart(){
		indicadorBubbleChartController.showForm();
	}
	
	@FXML
	private void handleCausaMorteAnimalChart(){
		causaMorteAnimalChartController.showForm();
	}
	
}
