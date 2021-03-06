package br.com.milkmoney.controller.entregaLeite;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.precoLeite.PrecoLeiteFormController;
import br.com.milkmoney.model.EntregaLeite;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.EntregaLeiteService;
import br.com.milkmoney.service.PrecoLeiteService;
import br.com.milkmoney.util.NumberFormatUtil;
import br.com.milkmoney.util.Util;

@Controller
public class EntregaLeiteOverviewController extends AbstractOverviewController<Integer, EntregaLeite> {

	@FXML private VBox vGroup;
	@FXML private TableColumn<EntregaLeite, String> mesReferenciaColumn;
	@FXML private TableColumn<EntregaLeite, LocalDate> dataInicioColumn;
	@FXML private TableColumn<EntregaLeite, LocalDate> dataFimColumn;
	@FXML private TableColumn<EntregaLeite, String> volumeColumn;
	@FXML private TableColumn<EntregaLeite, String> valorMaximoPraticadoColumn;
	@FXML private TableColumn<EntregaLeite, String> valorRecebidoColumn;
	@FXML private TableColumn<EntregaLeite, String> valorTotalColumn;
	@FXML private TableColumn<EntregaLeite, String> observacaoColumn;
	
	@FXML private Button btnIncrease;
	@FXML private Button btnDecrease;
	
	@FXML private Label lblAno;
	@FXML private Label lblTotalEntregue;
	@FXML private Label lblTotalRecebido;
	
	@Autowired private EntregaLeiteService service;
	@Autowired private PrecoLeiteService precoLeiteService;
	@Autowired private PrecoLeiteFormController precoLeiteFormController;
	
	private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private final LineChart<String, Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private ObservableList<String> meses = Util.generateListMonths();
	
	@FXML
	public void initialize() {
		mesReferenciaColumn.setCellValueFactory(new PropertyValueFactory<EntregaLeite, String>("mesReferencia"));
		dataInicioColumn.setCellFactory(new TableCellDateFactory<EntregaLeite, LocalDate>("dataInicio"));
		dataFimColumn.setCellFactory(new TableCellDateFactory<EntregaLeite, LocalDate>("dataFim"));
		volumeColumn.setCellValueFactory(new PropertyDecimalValueFactory<EntregaLeite, String>("volume", 2));
		valorMaximoPraticadoColumn.setCellValueFactory(new PropertyDecimalValueFactory<EntregaLeite,String>("valorMaximoPraticado", 3));
		valorRecebidoColumn.setCellValueFactory(new PropertyDecimalValueFactory<EntregaLeite, String>("valorRecebido", 3));
		valorTotalColumn.setCellValueFactory(new PropertyDecimalValueFactory<EntregaLeite, String>("valorTotal", 3));
		observacaoColumn.setCellValueFactory(new PropertyValueFactory<EntregaLeite, String>("observacao"));
		valorRecebidoColumn.setCellFactory(new Callback<TableColumn<EntregaLeite,String>, TableCell<EntregaLeite,String>>(){
			@Override
			public TableCell<EntregaLeite, String> call(TableColumn<EntregaLeite, String> param) {
				TableCell<EntregaLeite, String> cell = new TableCell<EntregaLeite, String>(){
					@Override
					public void updateItem(String item, boolean empty) {
						if(item!=null){
							Hyperlink link = new Hyperlink();
							link.setText(item);
							link.setFocusTraversable(false);
							link.setOnAction(new EventHandler<ActionEvent>() {
							    @Override
							    public void handle(ActionEvent e) {
							    	object = data.get(getTableRow().getIndex());
							    	handleCadastrarPrecoLeite();
							    }
							});
							setGraphic(link);
						}else{
							setGraphic(null);
						}
					}
				};                           
				return cell;
			}
		});
		
		super.service = this.service;
		service.configuraMesesEntregaAnoReferencia(selectedAnoReferencia);
		super.initialize((EntregaLeiteFormController) MainApp.getBean(EntregaLeiteFormController.class));
		super.getTable().setContextMenu(null);
		
        xAxis.setLabel("Meses");
        lineChart.setTitle("Variação Volume Entrega de Leite");
        lineChart.setLegendVisible(true);
        
        VBox.setVgrow(lineChart, Priority.SOMETIMES);
        HBox.setHgrow(lineChart, Priority.SOMETIMES);
        
        vGroup.getChildren().add(lineChart);
        
        resume();
		
	}
	
	private void setChartData(){
		lineChart.getData().clear();
		lineChart.getData().addAll(service.getDataChart(selectedAnoReferencia));
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		service.configuraMesesEntregaAnoReferencia(selectedAnoReferencia);
		refreshTableOverview();
		resume();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		service.configuraMesesEntregaAnoReferencia(selectedAnoReferencia);
		refreshTableOverview();
		resume();
	}
	
	@Override
	protected void refreshTableOverview() {
		super.data.clear();
		super.data.addAll(service.findAllByAnoAsObservableList(selectedAnoReferencia));
	}

	/**
	 * Atualiza os totais entregues e valor recebido
	 */
	public void resume(){
		if ( data != null && data.size() > 0 ){
			BigDecimal totalEntregue = new BigDecimal(0);
			BigDecimal valorRecebido = new BigDecimal(0);
			
			for (int i = 0; i < data.size(); i++){
				
				EntregaLeite e = data.get(i);
				
				totalEntregue = totalEntregue.add(e.getVolume());
				valorRecebido = valorRecebido.add(e.getValorTotal());
				
			}
			
			lblTotalEntregue.setText(NumberFormatUtil.decimalFormat(totalEntregue));
			lblTotalRecebido.setText("R$ " + NumberFormatUtil.decimalFormat(valorRecebido));
		}
		lblAno.setText(String.valueOf(selectedAnoReferencia));
		table.getSelectionModel().clearSelection();
		table.setFocusTraversable(false);
		
		setChartData();
	}
	
	@FXML
	private void handleCadastrarPrecoLeite(){
		
		precoLeiteFormController.setState(State.INSERT_TO_SELECT);

		PrecoLeite precoLeite = precoLeiteService.findByMesAno(getObject().getMesReferencia(), getObject().getAnoReferencia());
		
		if ( precoLeite == null ){
			precoLeite = new PrecoLeite();
			precoLeite.setMesReferencia(getObject().getMesReferencia());
			precoLeite.setAnoReferencia(getObject().getAnoReferencia());
			precoLeite.setCodigoMes(meses.indexOf(getObject().getMesReferencia())+1);
		}
		
		precoLeiteFormController.setObject(precoLeite);
		precoLeiteFormController.showForm();
		
		if ( precoLeiteFormController.getObject() != null ){
			refreshTableOverview();
			resume();
		}
		
	}
	
	@Override
	public String getFormName() {
		return "view/entregaLeite/EntregaLeiteOverview.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Entrega Leite";
	}
	
}
