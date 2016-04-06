package br.com.milkmoney.controller.producaoLeite;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.precoLeite.PrecoLeiteFormController;
import br.com.milkmoney.model.PrecoLeite;
import br.com.milkmoney.model.ProducaoLeite;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.PrecoLeiteService;
import br.com.milkmoney.service.ProducaoLeiteService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;
import br.com.milkmoney.util.Util;

@Controller
public class ProducaoLeiteOverviewController extends AbstractOverviewController<Integer, ProducaoLeite> {

	@FXML private VBox vGroup;
	@FXML private TableColumn<ProducaoLeite, LocalDate> dataColumn;
	@FXML private TableColumn<ProducaoLeite, String> numeroVacasOrdenhadasColumn;
	@FXML private TableColumn<ProducaoLeite, String> volumeProduzidoColumn;
	@FXML private TableColumn<ProducaoLeite, String> volumeEntregueColumn;
	@FXML private TableColumn<ProducaoLeite, String> mediaProducaoColumn;
	@FXML private TableColumn<ProducaoLeite, String> valorColumn;
	@FXML private TableColumn<ProducaoLeite, String> observacaoColumn;
	@FXML private ToggleButton tbJan, tbFev, tbMar, tbAbr, tbMai, tbJun, tbJul, tbAgo, tbSet, tbOut, tbNov, tbDez;
	
	@FXML private Label lblTotalEntregue;
	@FXML private Label lblTotalProduzido;
	@FXML private Label lblMediaMes;
	@FXML private Label lblMediaProdutividadeMes;
	@FXML private Label lblTotalVacasOrdenhadas;
	@FXML private Label lblAno;
	@FXML private Hyperlink lblValorEstimado;
	
	@Autowired private AnimalService animalService;
	@Autowired private ProducaoLeiteService service;
	@Autowired private PrecoLeiteService precoLeiteService;
	@Autowired private PrecoLeiteFormController precoLeiteFormController;
	
	private int selectedAnoReferencia = LocalDate.now().getYear();
	private int selectedMesReferencia = LocalDate.now().getMonthValue();
	
	private ObservableList<String> meses = Util.generateListMonths();
	private ToggleGroup groupMes  = new ToggleGroup();
	
	private final CategoryAxis              xAxis     = new CategoryAxis();
    private final NumberAxis                yAxis     = new NumberAxis();
	private final LineChart<String, Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
	
	@FXML
	public void initialize() {
		
		dataColumn.setCellFactory(new TableCellDateFactory<ProducaoLeite, LocalDate>("data"));
		volumeProduzidoColumn.setCellValueFactory(new PropertyValueFactory<ProducaoLeite, String>("volumeProduzidoFormatado"));
		volumeEntregueColumn.setCellValueFactory(new PropertyValueFactory<ProducaoLeite, String>("volumeEntregueFormatado"));
		mediaProducaoColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoLeite, String>("mediaProducao", 2));
		valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoLeite, String>("valor", 3));
		observacaoColumn.setCellValueFactory(new PropertyValueFactory<ProducaoLeite, String>("observacao"));
		
		numeroVacasOrdenhadasColumn.setCellValueFactory(new PropertyValueFactory<ProducaoLeite, String>("numeroVacasOrdenhadas"));

		groupMes.getToggles().clear();
		groupMes.getToggles().addAll(tbJan, tbFev, tbMar, tbAbr, tbMai, tbJun, tbJul, tbAgo, tbSet, tbOut, tbNov, tbDez);
		groupMes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> changeMesReferenciaListener( newValue ));
		groupMes.getToggles().get(selectedMesReferencia - 1).setSelected(true);
		
		super.service = this.service;
		service.configuraTabelaDiasMesSelecionado(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		super.initialize((ProducaoLeiteFormController)MainApp.getBean(ProducaoLeiteFormController.class));
		super.getTable().setContextMenu(null);
		
        xAxis.setLabel("Meses");
        lineChart.setTitle("Variação Produção de Leite");
        lineChart.setLegendVisible(true);
        
        VBox.setVgrow(lineChart, Priority.SOMETIMES);
        HBox.setHgrow(lineChart, Priority.SOMETIMES);
        
        vGroup.getChildren().add(lineChart);

		this.resume();
		
	}
	
	@Override
	public void handleEdit() {
		//localiza o número de animais em lactação e popula o objeto
		BigInteger animaisEmLactacaoData = animalService.countAnimaisEmLactacao(getObject().getData());
		
		if ( getObject().getNumeroVacasOrdenhadas() <= 0 ){
			getObject().setNumeroVacasOrdenhadas(animaisEmLactacaoData.intValue());			
		}
		
		super.handleEdit();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAnoReferencia++;
		service.configuraTabelaDiasMesSelecionado(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		refreshTableOverview();
		this.resume();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAnoReferencia--;
		service.configuraTabelaDiasMesSelecionado(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		refreshTableOverview();
		this.resume();
	}
	
	/**
	 * Ao alterar o mês de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	private void changeMesReferenciaListener(Toggle newValue) {
		
		int index = 1;
		for ( Toggle t : groupMes.getToggles() ){
			if ( t.isSelected() ){
				selectedMesReferencia = index;
				break;
			}
			index ++;
		}
		service.configuraTabelaDiasMesSelecionado(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		refreshTableOverview();
		this.resume();
		
	}    
	
	@Override
	protected void refreshTableOverview() {
		super.data.clear();
		super.data.addAll(service.findAllByPeriodoAsObservableList(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
		service.recarregaPrecoLeite(data, meses.get(selectedMesReferencia-1), selectedAnoReferencia);
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
	
	/**
	 * Faz a somatória da produção
	 * Média
	 * Valor Estimado
	 * Total Entregue
	 */
	protected void resume(){
		
		if ( data != null && data.size() > 0 ){
			
			int dias = 0;
			BigDecimal totalEntregue = new BigDecimal(0);
			BigDecimal totalProduzido = new BigDecimal(0);
			BigDecimal valor = new BigDecimal(0);
			int totalVacasOrdenhadas = 0;
			
			for (int i = 0; i < data.size(); i++){
				
				ProducaoLeite e = data.get(i);
				
				if ( e.getVolumeProduzido().compareTo(BigDecimal.ZERO) > 0 ){
					dias++;
					totalProduzido = totalProduzido.add(e.getVolumeProduzido());
					totalVacasOrdenhadas += e.getNumeroVacasOrdenhadas();
				}
				
				totalEntregue = totalEntregue.add(e.getVolumeEntregue());
				valor = valor.add(e.getValor());
				
			}
			
			lblTotalProduzido.setText(NumberFormatUtil.decimalFormat(totalProduzido));
			lblTotalEntregue.setText(NumberFormatUtil.decimalFormat(totalEntregue));
			lblTotalVacasOrdenhadas.setText(String.valueOf(totalVacasOrdenhadas));
			if ( totalProduzido.compareTo(BigDecimal.ZERO) > 0 && dias > 0 ){
				lblMediaMes.setText(NumberFormatUtil.decimalFormat(totalProduzido.divide(new BigDecimal(dias), 2, RoundingMode.HALF_UP)));
				if ( totalVacasOrdenhadas > 0 )  
					lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(totalProduzido.divide(new BigDecimal(totalVacasOrdenhadas), 2, RoundingMode.HALF_UP)));
				else
					lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
			}else{
				lblMediaMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
				lblMediaProdutividadeMes.setText(NumberFormatUtil.decimalFormat(BigDecimal.ZERO));
			}
			
			lblValorEstimado.setText(NumberFormatUtil.decimalFormat(valor));
			if ( !precoLeiteService.isPrecoCadastrado(meses.get(selectedMesReferencia-1), selectedAnoReferencia) ){
				lblValorEstimado.setText("Cadastrar Preço");
			}
			lblAno.setText(String.valueOf(selectedAnoReferencia));
		}
		
		setChartData();
		
	}
	
	private void setChartData(){
		lineChart.getData().clear();
		lineChart.getData().addAll(service.getDataChart(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
	}
	
	/**
	 * Quando não houver preço de leite informado para o mês
	 * habilita o cadastro pela tela de produção.
	 */
	@FXML
	private void handleCadastrarPrecoLeite(){
		
		precoLeiteFormController.setState(State.INSERT_TO_SELECT);
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(meses.get(selectedMesReferencia-1), selectedAnoReferencia);
		
		if ( precoLeite == null ){ 
			precoLeite = new PrecoLeite();
			precoLeite.setMesReferencia(meses.get(selectedMesReferencia-1));
			precoLeite.setAnoReferencia(selectedAnoReferencia);
			precoLeite.setCodigoMes(selectedMesReferencia);
		}
		
		precoLeiteFormController.setObject(precoLeite);
		precoLeiteFormController.showForm();
		if ( precoLeiteFormController.getObject() != null && precoLeiteFormController.getObject().getId() > 0 ){
			service.recarregaPrecoLeite(data, meses.get(selectedMesReferencia-1), selectedAnoReferencia);
			this.resume();
		}
		
	}

	@Override
	public String getFormName() {
		return "view/producaoLeite/ProducaoLeiteForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Produção Leite";
	}
	
}
