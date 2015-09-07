package br.com.milkmoney.controller.painel;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.model.CentroCusto;
import br.com.milkmoney.service.CentroCustoService;
import br.com.milkmoney.service.LancamentoFinanceiroService;

@Controller
public class FinanceiroChartController {

	@FXML private VBox group;
	@FXML private UCTextField inputAno;
	@FXML private ComboBox<CentroCusto> inputCentroCusto;
	@FXML private Button btnBuscarDadosGrafico;
	
	@Autowired LancamentoFinanceiroService lancamentoFinanceiroService;
	@Autowired CentroCustoService centroCustoService;
	
	private AreaChart<String, Number> areaChart;
	
	private CentroCusto ccBlank = new CentroCusto();
	
	@FXML
	public void initialize() {

		MaskFieldUtil.numeroInteiro(inputAno);
		inputAno.setText(String.valueOf(LocalDate.now().getYear()));
		
		inputCentroCusto.getItems().clear();
		inputCentroCusto.getItems().add(ccBlank);
		inputCentroCusto.getItems().addAll(centroCustoService.findAllAsObservableList());
		
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Meses");
        
        areaChart = new AreaChart<String,Number>(xAxis,yAxis);
        
        areaChart.setTitle("Receitas x Despesas");
        areaChart.setLegendVisible(true);
        
        VBox.setVgrow(areaChart, Priority.SOMETIMES);
        HBox.setHgrow(areaChart, Priority.SOMETIMES);
        
        setDataChart();
        
        group.getChildren().add(areaChart);
        
        btnBuscarDadosGrafico.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setDataChart();
			}
			
		});
        
        inputAno.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					setDataChart();
				}

			}

		});
        
	}
	
	private void setDataChart(){
		areaChart.getData().clear();
		areaChart.getData().addAll(lancamentoFinanceiroService.getDataChart(Integer.parseInt(inputAno.getText()), 
				inputCentroCusto.getValue() != null && inputCentroCusto.getValue().getId() > 0 ? inputCentroCusto.getValue() : null));
	}
	
	public String getFormName(){
		return "view/painel/FinanceiroChart.fxml";
	}

}
