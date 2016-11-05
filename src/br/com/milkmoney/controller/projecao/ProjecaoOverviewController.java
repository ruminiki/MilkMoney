package br.com.milkmoney.controller.projecao;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.TableCellDecimalFactory;
import br.com.milkmoney.controller.AbstractWindowPopUp;
import br.com.milkmoney.controller.indicador.renderer.PopUpWait;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.model.Projecao;
import br.com.milkmoney.service.PrecoLeiteService;
import br.com.milkmoney.service.ProjecaoService;
import br.com.milkmoney.util.NumberFormatUtil;

@Controller
public class ProjecaoOverviewController extends AbstractWindowPopUp{

	@FXML private TableView<Projecao> table;
	@FXML private TableColumn<Projecao, String> periodoColumn, animaisLactacaoColumn, percentualVariacaoAnimaisLactacaoColumn;
	@FXML private TableColumn<Projecao, String> animaisSecosColumn, percentualVariacaoAnimaisSecosColumn, producaoDiariaColumn;
	@FXML private TableColumn<Projecao, String> percentualVariacaoProducaoDiariaColumn, producaoMensalColumn, faturamentoColumn; 
	@FXML private TableColumn<Projecao, String> percentualVariacaoFaturamentoColumn;
	
	@FXML private TextField inputProdutividade, inputPrecoLeite;
	
	//chart
	@FXML private VBox                          vbChartLactacao, vbChartAnimaisSecos, vbChartProducao, vbChartFaturamento;
	private final CategoryAxis                  yAxis1                  = new CategoryAxis();
    private final NumberAxis                    xAxis1                  = new NumberAxis();
	private final LineChart<String, Number>     lineChartLactacao       = new LineChart<>(yAxis1, xAxis1);
	
	private final CategoryAxis                  yAxis2                  = new CategoryAxis();
    private final NumberAxis                    xAxis2                  = new NumberAxis();
	private final LineChart<String, Number>     lineChartAnimaisSecos   = new LineChart<>(yAxis2, xAxis2);
	
	private final CategoryAxis                  yAxis3                  = new CategoryAxis();
    private final NumberAxis                    xAxis3                  = new NumberAxis();
	private final LineChart<String, Number>     lineChartProducao       = new LineChart<>(yAxis3, xAxis3);
	
	private final CategoryAxis                  yAxis4                  = new CategoryAxis();
    private final NumberAxis                    xAxis4                  = new NumberAxis();
	private final LineChart<String, Number>     lineChartFaturamento    = new LineChart<>(yAxis4, xAxis4);
	
	@Autowired private ProjecaoService projecaoService;
	@Autowired private PrecoLeiteService precoLeiteService;
	
	private int mesInicio, anoInicio, mesFim, anoFim;
	private BigDecimal produtividade;
	private BigDecimal precoLeite;
	
	@FXML
	public void initialize() {
		
		periodoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("periodo"));
		animaisLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("numeroAnimaisLactacao"));
		animaisSecosColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("numeroAnimaisSecos"));
		producaoDiariaColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("producaoDiaria"));
		producaoDiariaColumn.setCellFactory(new TableCellDecimalFactory<Projecao,String>());
		producaoMensalColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("producaoMensal"));
		producaoMensalColumn.setCellFactory(new TableCellDecimalFactory<Projecao,String>());
		faturamentoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("faturamentoMensal"));
		faturamentoColumn.setCellFactory(new TableCellDecimalFactory<Projecao,String>());
		
		mesInicio = LocalDate.now().getMonthValue();
		anoInicio = LocalDate.now().getYear();
		mesFim = LocalDate.now().plusMonths(9).getMonthValue();
		anoFim = LocalDate.now().plusMonths(9).getYear();
		
		MaskFieldUtil.decimalWithoutMask(inputProdutividade);
		MaskFieldUtil.decimalWithoutMask(inputPrecoLeite);
		
		inputProdutividade.setOnKeyPressed(new EventHandler<KeyEvent>(){
	        @Override
	        public void handle(KeyEvent ke){
	            if (ke.getCode().equals(KeyCode.ENTER)){
	                simular();
	            }
	        }
	    });
		
		inputPrecoLeite.setOnKeyPressed(new EventHandler<KeyEvent>(){
	        @Override
	        public void handle(KeyEvent ke){
	            if (ke.getCode().equals(KeyCode.ENTER)){
	                simular();
	            }
	        }
	    });
		
		//chart
		lineChartLactacao.setTitle("Animais Lactação");
		lineChartLactacao.setLegendVisible(true);
        VBox.setVgrow(lineChartLactacao, Priority.SOMETIMES);
        HBox.setHgrow(lineChartLactacao, Priority.SOMETIMES);
        vbChartLactacao.getChildren().add(lineChartLactacao);
        
		lineChartAnimaisSecos.setTitle("Animais Secos");
		lineChartAnimaisSecos.setLegendVisible(true);
        VBox.setVgrow(lineChartAnimaisSecos, Priority.SOMETIMES);
        HBox.setHgrow(lineChartAnimaisSecos, Priority.SOMETIMES);
        vbChartAnimaisSecos.getChildren().add(lineChartAnimaisSecos);
        
		lineChartProducao.setTitle("Produção Diária");
		lineChartProducao.setLegendVisible(true);
        VBox.setVgrow(lineChartProducao, Priority.SOMETIMES);
        HBox.setHgrow(lineChartProducao, Priority.SOMETIMES);
        vbChartProducao.getChildren().add(lineChartProducao);
        
		lineChartFaturamento.setTitle("Faturamento Mensal");
		lineChartFaturamento.setLegendVisible(true);
        VBox.setVgrow(lineChartFaturamento, Priority.SOMETIMES);
        HBox.setHgrow(lineChartFaturamento, Priority.SOMETIMES);
        vbChartFaturamento.getChildren().add(lineChartFaturamento);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				projecaoService.setSimulacao(false);
				
				produtividade = projecaoService.getProducaoDiariaIndividualAtual();
				PrecoLeite pl = precoLeiteService.findByMesAno(mesInicio, anoInicio);
				precoLeite = pl != null ? pl.getValor() : BigDecimal.ZERO;

				inputProdutividade.setText(NumberFormatUtil.decimalFormat(produtividade));
				inputPrecoLeite.setText(NumberFormatUtil.decimalFormat(precoLeite));

				generateDataCharts(mesInicio, anoInicio, mesFim, anoFim);
			}
		});
	
	}
	
	private void generateDataCharts(int mesInicio, int anoInicio, int mesFim, int anoFim) {
		PopUpWait pp = new PopUpWait("Aguarde...");

		Task<Void> t = new Task<Void>() {
			
			@Override
			public Void call() throws InterruptedException {
				
				table.setItems(FXCollections.observableArrayList(projecaoService.getProjecaoPeriodo(mesInicio, mesFim, anoInicio, anoFim)));
				
				return null;
			}
			
		};
		
		Thread thread = new Thread(t);
		thread.setDaemon(true);
		thread.start();

		t.setOnSucceeded(e -> {
			generateCharts();
			pp.hide();
		});

		pp.getProgressBar().progressProperty().bind(t.progressProperty());
		pp.show(table.getScene().getWindow());		
		
	}
	
	private void generateCharts() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//chart
				lineChartLactacao.getData().clear();
				lineChartLactacao.getData().addAll(projecaoService.getDataChartAnimaisLactacao(table.getItems()));
				lineChartAnimaisSecos.getData().clear();
				lineChartAnimaisSecos.getData().addAll(projecaoService.getDataChartAnimaisSecos(table.getItems()));
				lineChartProducao.getData().clear();
				lineChartProducao.getData().addAll(projecaoService.getDataChartProducao(table.getItems()));
				lineChartFaturamento.getData().clear();
				lineChartFaturamento.getData().addAll(projecaoService.getDataChartFaturamento(table.getItems()));
			}
		});
	}
	
	@FXML
	private void simular(){
		
		projecaoService.setSimulacao(true);
		projecaoService.setProdutividade(NumberFormatUtil.fromString(inputProdutividade.getText()));
		projecaoService.setPrecoLeite(NumberFormatUtil.fromString(inputPrecoLeite.getText()));
		
		generateDataCharts(mesInicio, anoInicio, mesFim, anoFim);
	}

	public String getFormTitle() {
		return "Projecao";
	}
	
	public String getFormName() {
		return "view/projecao/ProjecaoOverview.fxml";
	}
	
}
