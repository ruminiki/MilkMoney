package br.com.milkmoney.controller.painel;

import java.time.LocalDate;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
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
import br.com.milkmoney.service.ProducaoLeiteService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.Util;

@Controller
public class ProducaoLeiteChartController {

	@FXML private VBox group;
	@FXML private UCTextField inputAno;
	@FXML private Button btnBuscarDadosGrafico;
	@FXML private ComboBox<String> inputMesReferencia;
	
	@Autowired ProducaoLeiteService producaoLeiteService;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private int selectedMesReferencia = LocalDate.now().getMonthValue();
	
	private ObservableList<String> meses = Util.generateListMonths();
	
	private final CategoryAxis              xAxis     = new CategoryAxis();
    private final NumberAxis                yAxis     = new NumberAxis();
	private final LineChart<String, Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
	
	@FXML
	public void initialize() {

		MaskFieldUtil.numeroInteiroWithouMask(inputAno);
		inputAno.setText(String.valueOf(LocalDate.now().getYear()));
		
        xAxis.setLabel("Meses");
        lineChart.setTitle("Variação Produção de Leite");
        lineChart.setLegendVisible(true);
        
        VBox.setVgrow(lineChart, Priority.SOMETIMES);
        HBox.setHgrow(lineChart, Priority.SOMETIMES);
        
        setChartData();
        group.getChildren().add(lineChart);
        
        btnBuscarDadosGrafico.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selectedAnoReferencia = Integer.parseInt(inputAno.getText());
				setChartData();
			}
			
		});
        
        inputMesReferencia.setItems(Util.generateListMonths());
		inputMesReferencia.getSelectionModel().select(selectedMesReferencia-1);
		inputMesReferencia.valueProperty().addListener(new ChangeListener<String>() {
			
			public void changed(javafx.beans.value.ObservableValue<? extends String> observable, String oldValue, String newValue) {
				selectedMesReferencia = meses.indexOf(newValue) + 1;
				selectedAnoReferencia = Integer.parseInt(inputAno.getText());
				setChartData();
			};
			
		});
        
        inputAno.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode().equals(KeyCode.ENTER)) {
					selectedAnoReferencia = Integer.parseInt(inputAno.getText());
					setChartData();
				}

			}

		});
        
	}
	
	private void setChartData(){
		lineChart.getData().clear();
		lineChart.getData().addAll(producaoLeiteService.getDataChart(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
	}
	
	/**
	 * Retorna a data do primeiro dia do mês selecionado
	 * @return
	 */
	private LocalDate dataInicioMes(){
		return LocalDate.of(selectedAnoReferencia, selectedMesReferencia, 01);
	}
	
	/**
	 * Retorna a data do último dia do mês selecionado
	 * @return
	 */
	private LocalDate dataFimMes(){
		return LocalDate.of(selectedAnoReferencia, selectedMesReferencia, dataInicioMes().lengthOfMonth());
	}
	
	public String getFormName(){
		return "view/painel/ProducaoLeiteChart.fxml";
	}

}
