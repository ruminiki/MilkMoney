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
import javafx.scene.Node;
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
import br.com.milkmoney.controller.configuracaoIndicador.ConfiguracaoIndicadorOverviewController;
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
	@Autowired ConfiguracaoIndicadorOverviewController configuracaoIndicadorOverviewController;
	
	private ObservableList<Indicador> data = FXCollections.observableArrayList();
	
	private int ano = LocalDate.now().getYear();
	private List<VBox> vBoxes;
	
	@FXML
	public void initialize() {
		lblAno.setText(String.valueOf(ano));
		vBoxes = new ArrayList<VBox>(Arrays.asList(vbJan, vbFev, vbMar, vbAbr, vbMai, 
												   vbJun, vbJul, vbAgo, vbSet, vbOut, vbNov, vbDez));
		configuraIndicadores();
	}
	
	private void configuraIndicadores(){
		
		data.clear();
		data.addAll(service.findAll());
		clearTable();
		
		vbMain.setDisable(true);
		
		for (Indicador indicador : data) {

			BoxDescricaoIndicador bdi = new BoxDescricaoIndicador(indicador, editIndicador);
			VBox.setVgrow(bdi, Priority.ALWAYS);
			HBox.setHgrow(bdi, Priority.ALWAYS);
			vbIndicadores.getChildren().add(bdi);
			
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for (Indicador indicador : data) {
					
					if ( indicador.getConfiguracaoIndicador(ano) == null ){
						ConfiguracaoIndicador ci = new ConfiguracaoIndicador(indicador);
						ci.setMenorValorEsperado(indicador.getMenorValorIdeal());
						ci.setMaiorValorEsperado(indicador.getMaiorValorIdeal());
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
						
					}
					
					service.save(indicador);
					
					for ( Number n : getMesesAno() ){
						vBoxes.get(n.intValue() - 1).getChildren().add(new BoxIndicadorSquare(indicador, ano, n.intValue()));
					}	
					
				}
				
				vbMain.setDisable(false);
			}
		});
		
	}
	
	@FXML
	private void calcularIndicadores(){
		vbMain.setDisable(true);
		vbMain.layout();
		
		Platform.runLater(() -> {
			for ( Number n : getMesesAno() ){
				
				ObservableList<Node> nodes = vBoxes.get(n.intValue() - 1).getChildren();
				
				for ( Node node : nodes ){
					
					BoxIndicadorSquare box = (BoxIndicadorSquare) node;
					
					service.refreshValorApurado(box.getIndicador().getValorIndicador(ano, n.intValue()), 
							DateUtil.lastDayOfMonth(ano, n.intValue()));					
					
					box.setValue();
					
				}
				
				//salva os novos valores calculados
				for ( Indicador i : data ){
					service.save(i);
				}
				
			}	
			vbMain.setDisable(false);
			vbMain.layout();
		});
		
	}
	
	@FXML
	private void definirMetas(){
		configuracaoIndicadorOverviewController.setAno(ano);
		configuracaoIndicadorOverviewController.showForm();
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
			configuraIndicadores();			
		}
	}
	
	@FXML
	private void handleDecreaseAnoReferencia() {
		ano--;
		lblAno.setText(String.valueOf(ano));
		configuraIndicadores();
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
