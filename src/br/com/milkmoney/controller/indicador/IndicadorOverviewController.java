package br.com.milkmoney.controller.indicador;

import java.util.function.Function;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.controller.indicador.renderer.BoxIndicador;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.indicadores.EficienciaReprodutiva;
import br.com.milkmoney.service.indicadores.IndicadorService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class IndicadorOverviewController {

	@FXML private GridPane gridIndicadoresZootecnicos, gridQuantitativosRebanho;
	@FXML private Label lblEficienciaReprodutiva;
	@Autowired private IndicadorService service;
	@Autowired private IndicadorFormController indicadorFormController;
	@Autowired private RelatorioService relatorioService;
	@Autowired private RootLayoutController rootLayoutController;
	
	@Autowired private EficienciaReprodutiva eficienciaReprodutiva;
	
	private ObservableList<Indicador> data;
	private Scene scene;
	
	@FXML
	public void initialize() {
		//zootécnicos
		//data = service.findAllIndicadoresZootecnicosAsObservableList(false, DateUtil.today);
	}
	
	private void carregaIndicadores(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				int col = 0;
				int row = 0;
				
				scene.setCursor(Cursor.WAIT);
				
				for (Indicador indicador : data) {
	
					//indicador = service.refreshValorApurado(indicador, DateUtil.today);
	
					BoxIndicador box = new BoxIndicador(indicador, editIndicador);
					GridPane.setConstraints(box, col, row);
	
					col++;
	
					if (col == 5) {
						col = 0;
						row++;
					}
	
					gridIndicadoresZootecnicos.getChildren().add(box);
	
				}
	
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblEficienciaReprodutiva.setText(eficienciaReprodutiva
								.getValue(DateUtil.today).toString() + "%");
					}
				});
				
				scene.setCursor(Cursor.DEFAULT);
				
			}
		});
		
		//quantitativos
				/*data = service.findAllQuantitativosRebanhoAsObservableList();
				
				int row = 0;
				col = 0;
				
				for ( Indicador indicador : data ){
					
					BoxIndicador box = new BoxIndicador(indicador, editIndicador);
					GridPane.setConstraints(box, col, row);
					
					col++;
					
					if ( col == 5 ){
						col = 0;
						row++;
					}
					
					gridQuantitativosRebanho.getChildren().add(box);
					
				}*/
	}
	
	Function<Indicador, Boolean> editIndicador = indicador -> {
		if ( indicador != null ){
			indicadorFormController.setState(State.UPDATE);
			indicadorFormController.setObject(indicador);
			indicadorFormController.showForm();
		} else {
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	public void showForm() {	
		
		AnchorPane form = (AnchorPane) MainApp.load(getFormName());
		Stage dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		scene = new Scene(form);
		dialogStage.setScene(scene);
		dialogStage.setResizable(false);
		
		scene.setCursor(Cursor.WAIT);
		
		dialogStage.show();
		
		carregaIndicadores();
	}
	
	@FXML
	private void handleImprimirIndicadores(){
		
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, RelatorioService.RELATORIO_INDICADORES);
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
	}
	
	public String getFormName(){
		return "view/indicador/IndicadorOverview.fxml";
	}

	public String getFormTitle() {
		return "Indicador";
	}
	
}
