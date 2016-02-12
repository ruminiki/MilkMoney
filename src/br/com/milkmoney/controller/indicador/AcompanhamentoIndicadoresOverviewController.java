package br.com.milkmoney.controller.indicador;

import java.util.function.Function;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
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

@Controller
public class AcompanhamentoIndicadoresOverviewController {

	@FXML private VBox vbIndicadores, vbJan, vbFev, vbMar, vbAbr, vbMai, vbJun, vbJul, vbAgo, vbSet, vbOut, vbNov, vbDez;
	@Autowired private IndicadorService service;
	@Autowired private IndicadorFormController indicadorFormController;
	@Autowired private RootLayoutController rootLayoutController;
	@Autowired private EficienciaReprodutiva eficienciaReprodutiva;
	
	private ObservableList<Indicador> data;
	private Scene scene;
	
	@FXML
	public void initialize() {
		//zootécnicos
		data = service.findAllIndicadoresZootecnicosAsObservableList(false);
	}
	
	private void carregaIndicadores(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				scene.setCursor(Cursor.WAIT);
				
				for (Indicador indicador : data) {
	
					indicador = service.refreshValorApurado(indicador);
					
					BoxDescricaoIndicador bdi = new BoxDescricaoIndicador(indicador, editIndicador);
					VBox.setVgrow(bdi, Priority.ALWAYS);
					HBox.setHgrow(bdi, Priority.ALWAYS);
					vbIndicadores.getChildren().add(bdi);
					
					BoxIndicadorSquare box = new BoxIndicadorSquare(indicador, editIndicador);
					VBox.setVgrow(bdi, Priority.ALWAYS);
					HBox.setHgrow(bdi, Priority.ALWAYS);
					vbJan.getChildren().add(box);
	
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
	
	public String getFormName(){
		return "view/indicador/AcompanhamentoIndicadoresOverview.fxml";
	}

	public String getFormTitle() {
		return "Indicador";
	}
	
}
