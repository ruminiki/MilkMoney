package br.com.milkmoney.controller.projecao;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.TableCellDecimalFactory;
import br.com.milkmoney.model.Projecao;
import br.com.milkmoney.service.ProjecaoService;

@Controller
public class ProjecaoOverviewController {

	@FXML private TableView<Projecao> table;
	@FXML private TableColumn<Projecao, String> periodoColumn, animaisLactacaoColumn, percentualVariacaoAnimaisLactacaoColumn;
	@FXML private TableColumn<Projecao, String> animaisSecosColumn, percentualVariacaoAnimaisSecosColumn, producaoDiariaColumn;
	@FXML private TableColumn<Projecao, String> percentualVariacaoProducaoDiariaColumn, producaoMensalColumn, faturamentoColumn; 
	@FXML private TableColumn<Projecao, String> percentualVariacaoFaturamentoColumn;
	
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
	
	@FXML
	public void initialize() {
		
		periodoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("periodo"));
		animaisLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("numeroAnimaisLactacao"));
		//percentualVariacaoAnimaisLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("percentualVariacaoNumeroAnimaisLactacao"));
		animaisSecosColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("numeroAnimaisSecos"));
		//percentualVariacaoAnimaisSecosColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("percentualVariacaoNumeroAnimaisSecos"));
		producaoDiariaColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("producaoDiaria"));
		producaoDiariaColumn.setCellFactory(new TableCellDecimalFactory<Projecao,String>());
		//percentualVariacaoProducaoDiariaColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("percentualVariacaoProducaoDiaria"));
		producaoMensalColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("producaoMensal"));
		producaoMensalColumn.setCellFactory(new TableCellDecimalFactory<Projecao,String>());
		faturamentoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("faturamentoMensal"));
		faturamentoColumn.setCellFactory(new TableCellDecimalFactory<Projecao,String>());
		//percentualVariacaoFaturamentoColumn.setCellValueFactory(new PropertyValueFactory<Projecao,String>("percentualVariacaFaturamentoMensal"));
		
		int mesInicio = LocalDate.now().getMonthValue();
		int anoInicio = LocalDate.now().getYear();
		int mesFim = LocalDate.now().plusMonths(9).getMonthValue();
		int anoFim = LocalDate.now().plusMonths(9).getYear();
		
		table.setItems(FXCollections.observableArrayList(projecaoService.getProjecaoPeriodo(mesInicio, mesFim, anoInicio, anoFim)));
		
		//chart
		lineChartLactacao.setTitle("Animais Lactação");
		lineChartLactacao.setLegendVisible(true);
		lineChartLactacao.getData().clear();
		lineChartLactacao.getData().addAll(projecaoService.getDataChartAnimaisLactacao(table.getItems()));
        VBox.setVgrow(lineChartLactacao, Priority.SOMETIMES);
        HBox.setHgrow(lineChartLactacao, Priority.SOMETIMES);
        vbChartLactacao.getChildren().add(lineChartLactacao);
        
		lineChartAnimaisSecos.setTitle("Animais Secos");
		lineChartAnimaisSecos.setLegendVisible(true);
		lineChartAnimaisSecos.getData().clear();
		lineChartAnimaisSecos.getData().addAll(projecaoService.getDataChartAnimaisSecos(table.getItems()));
        VBox.setVgrow(lineChartAnimaisSecos, Priority.SOMETIMES);
        HBox.setHgrow(lineChartAnimaisSecos, Priority.SOMETIMES);
        vbChartAnimaisSecos.getChildren().add(lineChartAnimaisSecos);
        
		lineChartProducao.setTitle("Produção Diária");
		lineChartProducao.setLegendVisible(true);
		lineChartProducao.getData().clear();
		lineChartProducao.getData().addAll(projecaoService.getDataChartProducao(table.getItems()));
        VBox.setVgrow(lineChartProducao, Priority.SOMETIMES);
        HBox.setHgrow(lineChartProducao, Priority.SOMETIMES);
        vbChartProducao.getChildren().add(lineChartProducao);
        
		lineChartFaturamento.setTitle("Faturamento Mensal");
		lineChartFaturamento.setLegendVisible(true);
		lineChartFaturamento.getData().clear();
		lineChartFaturamento.getData().addAll(projecaoService.getDataChartFaturamento(table.getItems()));
        VBox.setVgrow(lineChartFaturamento, Priority.SOMETIMES);
        HBox.setHgrow(lineChartFaturamento, Priority.SOMETIMES);
        vbChartFaturamento.getChildren().add(lineChartFaturamento);
		
	}

	protected String getFormTitle() {
		return "Projecao";
	}
	
	public String getFormName() {
		return "view/projecao/ProjecaoOverview.fxml";
	}
	
}
