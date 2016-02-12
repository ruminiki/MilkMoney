package br.com.milkmoney.controller.indicador;

import java.time.LocalDate;
import java.util.Date;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.controller.indicador.renderer.BoxDescricaoIndicador;
import br.com.milkmoney.controller.indicador.renderer.BoxIndicadorSquare;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.indicadores.EficienciaReprodutiva;
import br.com.milkmoney.service.indicadores.IndicadorService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class AcompanhamentoIndicadoresOverviewController {

	@FXML private VBox  vbIndicadores, vbJan, vbFev, vbMar, vbAbr, vbMai, vbJun, vbJul, vbAgo, vbSet, vbOut, vbNov, vbDez;
	@FXML private Label lblAno;
	
	@Autowired private IndicadorService service;
	@Autowired private IndicadorFormController indicadorFormController;
	@Autowired private RootLayoutController rootLayoutController;
	@Autowired private EficienciaReprodutiva eficienciaReprodutiva;
	
	private ObservableList<Indicador> data;
	private Scene scene;
	
	private int ano = LocalDate.now().getYear();
	
	@FXML
	public void initialize() {
		//zootécnicos
		data = service.findAllIndicadoresZootecnicosAsObservableList(false, DateUtil.today);
		data.addAll(service.findAllQuantitativosRebanhoAsObservableList(false, DateUtil.today));
		lblAno.setText(String.valueOf(ano));
	}
	
	private void carregaIndicadores(){
		
		clearAll();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				scene.setCursor(Cursor.WAIT);
				
				for (Indicador indicador : data) {
	
					BoxDescricaoIndicador bdi = new BoxDescricaoIndicador(indicador, editIndicador);
					VBox.setVgrow(bdi, Priority.ALWAYS);
					HBox.setHgrow(bdi, Priority.ALWAYS);
					vbIndicadores.getChildren().add(bdi);
					
					if ( indicador != null ){
						if ( exibeBoxValorIndicador(1) ){
							vbJan.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 1)));	
						}
						
						if ( exibeBoxValorIndicador(2) ){
							vbFev.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 2)));	
						}
							
						if ( exibeBoxValorIndicador(3) ){
							vbMar.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 3)));
						}
						
						if ( exibeBoxValorIndicador(4) ){
							vbAbr.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 4)));
						}
						
						if ( exibeBoxValorIndicador(5) ){
							vbMai.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 5)));
						}
						
						if ( exibeBoxValorIndicador(6) ){
							vbJun.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 6)));
						}
						
						if ( exibeBoxValorIndicador(7) ){
							vbJul.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 7)));
						}
						
						if ( exibeBoxValorIndicador(8) ){
							vbAgo.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 8)));
						}
						
						if ( exibeBoxValorIndicador(9) ){
							vbSet.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 9)));
						}
						
						if ( exibeBoxValorIndicador(10) ){
							vbOut.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 10)));
						}
						
						if ( exibeBoxValorIndicador(11) ){
							vbNov.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 11)));
						}
						
						if ( exibeBoxValorIndicador(12) ){
							vbDez.getChildren().add(getBoxIndicador(indicador, DateUtil.lastDayOfMonth(ano, 12)));
						}
						
					}
					
				}
				
				/*Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblEficienciaReprodutiva.setText(eficienciaReprodutiva
								.getValue().toString() + "%");
					}
				});*/
				
				scene.setCursor(Cursor.DEFAULT);
				
			}
		});
		
	}
	
	private void clearAll() {
		vbIndicadores.getChildren().clear(); 
		vbJan.getChildren().clear(); 
		vbFev.getChildren().clear(); 
		vbMar.getChildren().clear(); 
		vbAbr.getChildren().clear(); 
		vbMai.getChildren().clear(); 
		vbJun.getChildren().clear(); 
		vbJul.getChildren().clear(); 
		vbAgo.getChildren().clear(); 
		vbSet.getChildren().clear(); 
		vbOut.getChildren().clear(); 
		vbNov.getChildren().clear(); 
		vbDez.getChildren().clear(); 
	}

	private BoxIndicadorSquare getBoxIndicador(Indicador indicador, Date data){
		
		indicador = service.refreshValorApurado(indicador, data);
		BoxIndicadorSquare box = new BoxIndicadorSquare(indicador, editIndicador);
		VBox.setVgrow(box, Priority.ALWAYS);
		HBox.setHgrow(box, Priority.ALWAYS);
		return box;
		
	}
	
	private boolean exibeBoxValorIndicador(int mes){
		return (ano < LocalDate.now().getYear() || (ano == LocalDate.now().getYear() && LocalDate.now().getMonthValue() >= mes));
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
	
	@FXML
	private void handleIncreaseAnoReferencia() {
		ano++;
		lblAno.setText(String.valueOf(ano));
		carregaIndicadores();
	}
	
	@FXML
	private void handleDecreaseAnoReferencia() {
		ano--;
		lblAno.setText(String.valueOf(ano));
		carregaIndicadores();
	}
	
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
	
	public String getFormName(){
		return "view/indicador/AcompanhamentoIndicadoresOverview.fxml";
	}

	public String getFormTitle() {
		return "Indicador";
	}
	
}
