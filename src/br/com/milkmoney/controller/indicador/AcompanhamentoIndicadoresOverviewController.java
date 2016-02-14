package br.com.milkmoney.controller.indicador;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.controller.configuracaoIndicador.ConfiguracaoIndicadorFormController;
import br.com.milkmoney.controller.indicador.renderer.BoxDescricaoIndicador;
import br.com.milkmoney.controller.indicador.renderer.BoxIndicadorSquare;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.model.ConfiguracaoIndicador;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.State;
import br.com.milkmoney.model.ValorIndicador;
import br.com.milkmoney.service.indicadores.EficienciaReprodutiva;
import br.com.milkmoney.service.indicadores.IndicadorService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.Util;

@Controller
public class AcompanhamentoIndicadoresOverviewController {

	@FXML private VBox  vbMain, vbIndicadores, vbJan, vbFev, vbMar, vbAbr, vbMai, 
						vbJun, vbJul, vbAgo, vbSet, vbOut, vbNov, vbDez;
	@FXML private Label lblAno;
	
	@Autowired private IndicadorService service;
	@Autowired private IndicadorFormController indicadorFormController;
	@Autowired private RootLayoutController rootLayoutController;
	@Autowired private EficienciaReprodutiva eficienciaReprodutiva;
	@Autowired ConfiguracaoIndicadorFormController configuracaoIndicadorFormController;
	
	private ObservableList<Indicador> data = FXCollections.observableArrayList();
	
	private int ano = LocalDate.now().getYear();
	private List<VBox> vBoxes;
	
	@FXML
	public void initialize() {
		lblAno.setText(String.valueOf(ano));
		vBoxes = new ArrayList<VBox>(Arrays.asList(vbJan, vbFev, vbMar, vbAbr, vbMai, 
												   vbJun, vbJul, vbAgo, vbSet, vbOut, vbNov, vbDez));
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				montarTabela(false);
			}
		});
		
	}
	
	private void montarTabela(boolean calcularIndicadores){
		data.clear();
		data.addAll(service.findAll());
		clearTable();
		
		for (Indicador indicador : data) {

			BoxDescricaoIndicador bdi = new BoxDescricaoIndicador(indicador, editIndicador);
			VBox.setVgrow(bdi, Priority.ALWAYS);
			HBox.setHgrow(bdi, Priority.ALWAYS);
			vbIndicadores.getChildren().add(bdi);
			
			if ( indicador.getConfiguracaoIndicador(ano) == null ){
				ConfiguracaoIndicador ci = new ConfiguracaoIndicador(indicador);
				ci.setAno(ano);
				indicador.getConfiguracoesIndicador().add(ci);
			}
			
			for ( Number n : getMesesAno() ){
				
				if ( indicador.getValorIndicador(ano, n.intValue()) == null ){
					ValorIndicador vi = new ValorIndicador(indicador);
					vi.setValor(BigDecimal.ZERO);
					vi.setAno(ano);
					vi.setMes(n.intValue());
					indicador.getValores().add(vi);
				}
				
				if ( calcularIndicadores ){
					service.refreshValorApurado(indicador.getValorIndicador(ano, n.intValue()), DateUtil.lastDayOfMonth(ano, n.intValue()));					
				}
				
			}
			
			service.save(indicador);
			
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for (Indicador indicador : data) {
					
					for ( Number n : getMesesAno() ){
						
						vBoxes.get(n.intValue() - 1).getChildren().add(
								new BoxIndicadorSquare(indicador, ano, n.intValue(), editIndicador));
						
					}	
					
				}
			}
		});
		
	}
	
	@FXML
	private void calcularIndicadores(){
		
		HBox message = new HBox();
		message.setAlignment(Pos.CENTER);
		message.setMinWidth(300);
		message.setMinHeight(80);
		message.getChildren().add(new Label("Aguarde..."));
		
		VBox.setVgrow(message, Priority.ALWAYS);
		HBox.setHgrow(message, Priority.ALWAYS);
		
		PopOver notification = new PopOver();
		notification.centerOnScreen();
		notification.setArrowSize(0);
		notification.setAutoHide(false);
		notification.setDetachable(false);
		notification.setContentNode(message);
		notification.setCornerRadius(0);
		
		notification.showingProperty().addListener(
				(observable, oldValue, newValue) -> {
			if ( notification.isShowing() ){
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						montarTabela(true);
						notification.hide();
					}
				});
				
			}
		});
		
		notification.show(vbMain.getScene().getWindow());
		
	}
	
	@FXML
	private void definirMetas(){
		//configuracaoIndicadorFormController.setObject();
	}
	
	private ObservableList<Number> getMesesAno(){
		int meses = ano < LocalDate.now().getYear() ? 12 : LocalDate.now().getMonthValue();
		return Util.generateListNumbers(1, meses);
	}
	
	private void clearTable() {
		vbIndicadores.getChildren().clear(); 
		for ( VBox v : vBoxes ){
			v.getChildren().clear();
		}
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
		if ( ano < LocalDate.now().getYear() ){
			ano++;
			lblAno.setText(String.valueOf(ano));
			montarTabela(false);			
		}
	}
	
	@FXML
	private void handleDecreaseAnoReferencia() {
		ano--;
		lblAno.setText(String.valueOf(ano));
		montarTabela(false);
	}
	
	public void showForm() {	
		
		AnchorPane form = (AnchorPane) MainApp.load(getFormName());
		Stage dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);
		
		dialogStage.show();
		
	}
	
	public String getFormName(){
		return "view/indicador/AcompanhamentoIndicadoresOverview.fxml";
	}

	public String getFormTitle() {
		return "Indicador";
	}
	
}
