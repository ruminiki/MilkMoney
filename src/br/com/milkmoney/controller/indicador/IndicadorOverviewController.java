package br.com.milkmoney.controller.indicador;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.controller.configuracaoIndicador.ConfiguracaoIndicadorOverviewController;
import br.com.milkmoney.controller.indicador.renderer.BoxDescricaoIndicador;
import br.com.milkmoney.controller.indicador.renderer.BoxIndicador;
import br.com.milkmoney.controller.indicador.renderer.PopUpWait;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.model.ConfiguracaoIndicador;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.State;
import br.com.milkmoney.model.ValorIndicador;
import br.com.milkmoney.service.ConfiguracaoIndicadorService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.indicadores.EficienciaReprodutiva;
import br.com.milkmoney.service.indicadores.IndicadorService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.Util;


@Controller
public class IndicadorOverviewController {

	@FXML private VBox  vbMain, vbIndicadores, vbJan, vbFev, vbMar, vbAbr, vbMai, 
						vbJun, vbJul, vbAgo, vbSet, vbOut, vbNov, vbDez;
	@FXML private Label lblAno;
	
	@Autowired private IndicadorService service;
	@Autowired private ConfiguracaoIndicadorService configuracaoIndicadorService;
	@Autowired private RelatorioService relatorioService;
	
	@Autowired private IndicadorFormController indicadorFormController;
	@Autowired private RootLayoutController rootLayoutController;
	@Autowired private EficienciaReprodutiva eficienciaReprodutiva;
	@Autowired ConfiguracaoIndicadorOverviewController configuracaoIndicadorOverviewController;
	@Autowired IndicadorBubbleChartController indicadorBubbleChartController;
	
	private ObservableList<Indicador> data = FXCollections.observableArrayList();
	
	private int ano = LocalDate.now().getYear();
	private List<VBox> vBoxes;
	
	Task<Void> task = null;
	PopUpWait popUpWait = null;
	
	@FXML
	public void initialize() {
		lblAno.setText(String.valueOf(ano));
		vBoxes = new ArrayList<VBox>(Arrays.asList(vbJan, vbFev, vbMar, vbAbr, vbMai, 
												   vbJun, vbJul, vbAgo, vbSet, vbOut, vbNov, vbDez));
		
		data.clear();
		data.addAll(service.findAll());
		
		configuraIndicadores();
	}
	
	private void configuraIndicadores(){
		
		vbMain.setDisable(true);
		clearTable();
		
		Task<Void> t = new Task<Void>() {
			
			@Override
			public Void call() throws InterruptedException {
				
				for (Indicador indicador : data) {
					
					if ( indicador.getConfiguracaoIndicador(ano) == null ){
						ConfiguracaoIndicador ci = new ConfiguracaoIndicador(indicador);
						ci.setMenorValorEsperado(indicador.getMenorValorIdeal());
						ci.setMaiorValorEsperado(indicador.getMaiorValorIdeal());
						ci.setObjetivo(indicador.getObjetivo());
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
					
				}
				return null;
			}
		};
			
		t.setOnSucceeded(e -> {
			//recarrega para preencher todos os ids das coleções inseridas
			data.clear();
			data.addAll(service.findAll());
			
			for (Indicador indicador : data) {

				BoxDescricaoIndicador bdi = new BoxDescricaoIndicador(indicador, editIndicador);
				VBox.setVgrow(bdi, Priority.ALWAYS);
				HBox.setHgrow(bdi, Priority.ALWAYS);
				vbIndicadores.getChildren().add(bdi);
				
			}
			
			for (Indicador indicador : data) {
				for ( Number n : getMesesAno() ){
					vBoxes.get(n.intValue() - 1).getChildren().add(new BoxIndicador(indicador, ano, n.intValue()));
				}	
			}
			vbMain.setDisable(false);
		});
		
		Thread thread = new Thread(t);
		thread.setDaemon(true);
		thread.start();
		
	}
	
	@FXML
	private void calcularIndicadores(){
		
		vbMain.setDisable(true);
		PopUpWait pp = new PopUpWait("Aguarde...");
		
		task = new Task<Void>() {
			
			@Override
			public Void call() throws InterruptedException {
				
				double progressComplete = getMesesAno().size() * data.size();
				double index = 0;
				
				for ( Number n : getMesesAno() ){
					
					ObservableList<Node> nodes = vBoxes.get(n.intValue() - 1).getChildren();
					for ( Node node : nodes ){
						
						BoxIndicador box = (BoxIndicador) node;
						
						ValorIndicador vi = box.getIndicador().getValorIndicador(ano, n.intValue());
						Date data = ((n.intValue() == LocalDate.now().getMonthValue() && ano == LocalDate.now().getYear()) ? new Date() : DateUtil.lastDayOfMonth(ano, n.intValue()));
						service.refreshValorApurado(vi, data); 
													
						updateProgress(index++, progressComplete);
					}
					
					//salva os novos valores calculados
					for ( Indicador i : data ){
						service.save(i);
					}
					
				}	

				return null;
			}
		};
		
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
		
		pp.getProgressBar().progressProperty().bind(task.progressProperty());
		pp.show(vbMain.getScene().getWindow());
		
		task.setOnSucceeded(e -> {
			for ( Number n : getMesesAno() ){
				ObservableList<Node> nodes = vBoxes.get(n.intValue() - 1).getChildren();
				for ( Node node : nodes ){
					BoxIndicador box = (BoxIndicador) node;
					box.setValue();
				}
			}
			pp.hide();
			vbMain.setDisable(false);
		});
			
	}
	
	@FXML
	private void graficoEficiencia(){
		indicadorBubbleChartController.showForm();
	}
	
	@FXML
	private void definirMetas(){
		
		configuracaoIndicadorOverviewController.setAno(ano);
		configuracaoIndicadorOverviewController.showForm();
		
		//força o refresh da meta
		data.clear();
		data.addAll(service.findAll());
		
		for (Indicador indicador : data) {
			for ( Number n : getMesesAno() ){
				
				ObservableList<Node> nodes = vBoxes.get(n.intValue() - 1).getChildren();
				for ( Node node : nodes ){
					
					BoxIndicador box = (BoxIndicador) node;
					if ( box.getIndicador().getId() == indicador.getId() ){
						box.setIndicador(indicador);
						box.setValue();						
					}
					
				}
				
			}	
			
		}
		
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
	
	@FXML
	private void imprimirIndicadores(){
		Object[] params = new Object[]{ano};
	
		WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
			RelatorioService.RELATORIO_INDICADORES, params), MainApp.primaryStage);

	}
	
	public void showForm() {	
		AnchorPane form = (AnchorPane) MainApp.load(getFormName());
		Stage dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);
		dialogStage.setResizable(false);
		Scene scene = new Scene(form);
		dialogStage.setScene(scene);
		
		/*dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				if ( task != null && task.isRunning() ){
					t.consume();
					Notifications.create().text("Por favor, aguarde a conclusão da operação!").showInformation();
					dialogStage.show();
					if ( popUpWait != null ){
						popUpWait.show(dialogStage);
					}
				}else{
					dialogStage.close();
				}
			}
		});*/
		
		dialogStage.show();
	}
	
	public String getFormName(){
		return "view/indicador/IndicadorOverview.fxml";
	}

	public String getFormTitle() {
		return "Indicador";
	}
	
}
