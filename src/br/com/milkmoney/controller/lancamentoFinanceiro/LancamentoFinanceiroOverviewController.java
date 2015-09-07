package br.com.milkmoney.controller.lancamentoFinanceiro;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.lancamentoFinanceiro.renderer.TableCellSituacaoLancamentoFinanceiroFactory;
import br.com.milkmoney.controller.lancamentoFinanceiro.renderer.TableCellTipoLancamentoFinanceiroFactory;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.model.SaldoCategoriaDespesa;
import br.com.milkmoney.model.TipoLancamentoFinanceiro;
import br.com.milkmoney.service.LancamentoFinanceiroService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;


@Controller
public class LancamentoFinanceiroOverviewController extends AbstractOverviewController<Integer, LancamentoFinanceiro> {

	//LANÇAMENTOS
	@FXML private TableColumn<LancamentoFinanceiro, String> dataEmissaoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataVencimentoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> categoriaLancamentoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> centroCustoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> descricaoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> valorColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> tipoLancamentoFinanceiroColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> situacaoLancamentoFinanceiroColumn;
	@FXML private ToggleButton tbJan, tbFev, tbMar, tbAbr, tbMai, tbJun, tbJul, tbAgo, tbSet, tbOut, tbNov, tbDez, tbReceita, tbDespesa;
	@FXML private Label lblAno, lblReceitas, lblDespesas, lblSaldo;
	@FXML private VBox vbChart;
	
	@FXML private TableView<SaldoCategoriaDespesa> tableSaldoCategoriaDespesa;
	@FXML private TableColumn<SaldoCategoriaDespesa, String> categoriaColumn;
	@FXML private TableColumn<SaldoCategoriaDespesa, String> saldoColumn;
	@FXML private TableColumn<SaldoCategoriaDespesa, String> percentualColumn;
	
	@Autowired private LancamentoFinanceiroService service;
	
	private int selectedAno = LocalDate.now().getYear();
	private int selectedMes = LocalDate.now().getMonthValue();

	private ToggleGroup groupMes  = new ToggleGroup();
	
	private final CategoryAxis              yAxis     = new CategoryAxis();
    private final NumberAxis                xAxis     = new NumberAxis();
	private final BarChart<String, Number>  barChart  = new BarChart<>(yAxis,xAxis);
	
	@FXML
	public void initialize() {
		
		situacaoLancamentoFinanceiroColumn.setCellFactory(new TableCellSituacaoLancamentoFinanceiroFactory<LancamentoFinanceiro,String>("dataEmissao"));
		tipoLancamentoFinanceiroColumn.setCellFactory(new TableCellTipoLancamentoFinanceiroFactory<LancamentoFinanceiro,String>("tipoLancamentoFormatado"));
		centroCustoColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("centroCusto"));
		dataEmissaoColumn.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataEmissao"));
		dataVencimentoColumn.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataVencimento"));
		categoriaLancamentoColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("categoria"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("descricao"));
		valorColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("valorTotalFormatado"));
		
		//tabela saldos categorias
		categoriaColumn.setCellValueFactory(new PropertyValueFactory<SaldoCategoriaDespesa,String>("categoria"));
		saldoColumn.setCellValueFactory(new PropertyValueFactory<SaldoCategoriaDespesa,String>("saldo"));
		percentualColumn.setCellValueFactory(new PropertyValueFactory<SaldoCategoriaDespesa,String>("percentual"));
		
		super.initialize((LancamentoFinanceiroFormController) MainApp.getBean(LancamentoFinanceiroFormController.class));
		super.setService(service);
		
		groupMes.getToggles().clear();
		groupMes.getToggles().addAll(tbJan, tbFev, tbMar, tbAbr, tbMai, tbJun, tbJul, tbAgo, tbSet, tbOut, tbNov, tbDez);
		groupMes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> selectToggle( newValue ));
		groupMes.getToggles().get(selectedMes - 1).setSelected(true);
		
		lblAno.setText(String.valueOf(selectedAno));
		xAxis.setCacheShape(true);
		yAxis.setCenterShape(true);
        barChart.setTitle("Receitas x Despesas");
        barChart.setLegendVisible(false);
        VBox.setVgrow(barChart, Priority.SOMETIMES);
        HBox.setHgrow(barChart, Priority.SOMETIMES);
        
        vbChart.getChildren().add(barChart);
        
        /*final Tooltip caption = new Tooltip("");
        caption.setStyle("-fx-font: 10 arial;");
        
        for (final Series<String, Number> data : barChart.getData() ) {
        	for ( Data<String, Number> serie : data.getData() ){
        		serie.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        caption.setAnchorX(e.getSceneX());
                        caption.setAnchorY(e.getSceneY());
                        caption.setText("R$ " + serie.getYValue());
                        caption.show(barChart.getScene().getWindow());
                        
                     }
                });
        	}
        }*/
		
	}
	
	private void refreshTela(){
		updateChart();
		updateResumo();
		updateLabelNumRegistros();
		updateSaldosCategorias();
	}
	
	@Override
	protected void handleDelete() {
		super.handleDelete();
		refreshTela();
	}
	
	@Override
	public void addObjectInTableView(LancamentoFinanceiro lancamento) {
		LocalDate dataVencimento = DateUtil.asLocalDate(lancamento.getDataVencimento());
		if ( dataVencimento.getMonthValue() == selectedMes ){
			//somente adiciona na tabela se o lançamento vence no mês que está sendo exibido
			super.addObjectInTableView(lancamento);
		}
	}
	
	@Override
	public void refreshObjectInTableView(LancamentoFinanceiro lancamento) {
		super.refreshObjectInTableView(lancamento);
		refreshTela();
	}
	
	@Override
	protected void refreshTableOverview() {
		this.data.clear();
		this.table.getItems().clear();
		
		if ( inputPesquisa != null && inputPesquisa.getText() != null &&
				inputPesquisa.getText().length() > 0){
			data.addAll(handleDefaultSearch());
			setSearch(null);
		}else{
			data.addAll(service.findByMes(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
		}
		
		table.setItems(data);
		table.layout();
		
		refreshTela();
	}
	
	@Override
	public ObservableList<LancamentoFinanceiro> handleDefaultSearch() {
		return service.findByMes(inputPesquisa.getText(), DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
	}
	
	private void updateChart(){
		barChart.getData().clear();
        barChart.getData().addAll(service.getDataChart(data));
	}
	
	private void updateResumo(){
		
		BigDecimal receitas = new BigDecimal(0);
		BigDecimal despesas = new BigDecimal(0);
		BigDecimal saldo = new BigDecimal(0);
		
		for ( LancamentoFinanceiro lancamento : table.getItems() ){
			if ( lancamento.getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA) ){
				receitas = receitas.add(lancamento.getValorTotal());
			}else{
				despesas = despesas.add(lancamento.getValorTotal());
			}
		}
		
		saldo = receitas.subtract(despesas);
		
		lblReceitas.setText("R$ " + NumberFormatUtil.decimalFormat(receitas));
		lblDespesas.setText("R$ " + NumberFormatUtil.decimalFormat(despesas));
		lblSaldo.setText("R$ " + NumberFormatUtil.decimalFormat(saldo));
		
	}
	
	private void updateSaldosCategorias(){
		tableSaldoCategoriaDespesa.getItems().clear();
		tableSaldoCategoriaDespesa.getItems().addAll(service.getSaldoByCategoriaDespesa(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes())));
	}
	
	/**
	 * Retorna a data do primeiro dia do mês selecionado
	 * @return
	 */
	private LocalDate dataInicioMes(){
		return LocalDate.of(selectedAno, selectedMes, 01);
	}
	
	/**
	 * Retorna a data do último dia do mês selecionado
	 * @return
	 */
	private LocalDate dataFimMes(){
		return LocalDate.of(selectedAno, selectedMes, dataInicioMes().lengthOfMonth());
	}
	
	private void selectToggle(Toggle newValue){
		int index = 1;
		for ( Toggle t : groupMes.getToggles() ){
			if ( t.isSelected() ){
				selectedMes = index;
				break;
			}
			index ++;
		}
		refreshTableOverview();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleIncreaseAnoReferencia() {
		selectedAno++;
		lblAno.setText(String.valueOf(selectedAno));
		this.refreshTableOverview();
	}
	
	/**
	 * Ao alterar o ano de referência carrega o respectivo calendário de entrega.
	 * @param newValue
	 */
	@FXML
	private void handleDecreaseAnoReferencia() {
		selectedAno--;
		lblAno.setText(String.valueOf(selectedAno));
		this.refreshTableOverview();
	}
	
	@Override
	public String getFormTitle() {
		return "Lançamento Financeiro";
	}
	
	@Override
	public String getFormName() {
		return "view/cobertura/LancamentoFinanceiroOverview.fxml";
	}

}
