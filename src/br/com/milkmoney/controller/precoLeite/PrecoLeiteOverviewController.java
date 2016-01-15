package br.com.milkmoney.controller.precoLeite;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.events.ActionEvent;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.service.PrecoLeiteService;

@Controller
public class PrecoLeiteOverviewController extends AbstractOverviewController<Integer, PrecoLeite> implements ApplicationListener<ActionEvent>{

	@FXML private TableColumn<PrecoLeite, String> mesReferenciaColumn;
	@FXML private TableColumn<PrecoLeite, String> anoReferenciaColumn;
	@FXML private TableColumn<PrecoLeite, String> valorMaximoPraticadoColumn;
	@FXML private TableColumn<PrecoLeite, String> valorRecebidoColumn;
	
	@FXML private Button btnIncrease;
	@FXML private Button btnDecrease;
	
	@FXML private HBox hBox;
	@FXML private Label lblAno;
	
	@Autowired private PrecoLeiteService service;
	@Autowired private PrecoLeiteFormController formController;
		
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private LineChart<String,Number> lineChart;
	
	@FXML
	public void initialize() {
		
		mesReferenciaColumn.setCellValueFactory(new PropertyValueFactory<PrecoLeite,String>("mesReferencia"));
		anoReferenciaColumn.setCellValueFactory(new PropertyValueFactory<PrecoLeite,String>("anoReferencia"));
		valorMaximoPraticadoColumn.setCellValueFactory(new PropertyDecimalValueFactory<PrecoLeite, String>("valorMaximoPraticado", 3));
		valorRecebidoColumn.setCellValueFactory(new PropertyDecimalValueFactory<PrecoLeite, String>("valorRecebido", 3));
		
		lblAno.setText(String.valueOf(selectedAnoReferencia));
		
		//chart
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Meses");
        
        lineChart = new LineChart<String,Number>(xAxis,yAxis);
        
        lineChart.setTitle("Variação Preço do Leite");
        lineChart.setLegendVisible(true);
        
        VBox.setVgrow(lineChart, Priority.SOMETIMES);
        HBox.setHgrow(lineChart, Priority.SOMETIMES);
        
        lineChart.getData().addAll(service.getDataChart(selectedAnoReferencia));
        
        hBox.getChildren().add(lineChart);
        
        super.service = this.service;
		super.initialize(formController);
        
        //refreshTableOverview();
        
	}
	
	@Override
	protected void refreshTableOverview() {
		
		if ( data.size() <= 0 ){
			service.configuraMesesAnoReferencia(selectedAnoReferencia);	
			table.setItems(data);
		}
		
		super.data.clear();
		super.data.addAll(service.findAllByAnoAsObservableList(selectedAnoReferencia));
		
		if ( lineChart != null ){
			lineChart.getData().clear();
			lineChart.getData().addAll(service.getDataChart(selectedAnoReferencia));	
		}
		
	}
	
	/*
	 * Recupera o evento disparado pelo form ao salvar o objeto
	 * para ser possível atualizar o gráfico.
	 */
	@Override
	public void onApplicationEvent(ActionEvent event) {
		if ( event != null ){
			if ( ( event.getEventType().equals(ActionEvent.EVENT_INSERT) || event.getEventType().equals(ActionEvent.EVENT_UPDATE)) ){
				
				if ( event.getSource().getClass().isInstance(getObject()) ){
					refreshTableOverview();
				}
			}
		}
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		((PrecoLeiteService)service).configuraMesesAnoReferencia(selectedAnoReferencia);
		this.refreshTableOverview();
		lblAno.setText(String.valueOf(selectedAnoReferencia));
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		((PrecoLeiteService)service).configuraMesesAnoReferencia(selectedAnoReferencia);
		this.refreshTableOverview();
		lblAno.setText(String.valueOf(selectedAnoReferencia));
	}
	
	@Override
	public String getFormTitle() {
		return "Preço Leite";
	}
	
	@Override
	public String getFormName() {
		return "view/precoLeite/PrecoLeiteOverview.fxml";
	}
	
}
