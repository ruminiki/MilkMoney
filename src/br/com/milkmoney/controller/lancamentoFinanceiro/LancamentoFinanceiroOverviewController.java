package br.com.milkmoney.controller.lancamentoFinanceiro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.lancamentoFinanceiro.renderer.TableCellSituacaoLancamentoFinanceiroFactory;
import br.com.milkmoney.controller.lancamentoFinanceiro.renderer.TableCellTipoLancamentoFinanceiroFactory;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.model.SaldoCategoriaDespesa;
import br.com.milkmoney.model.State;
import br.com.milkmoney.model.TipoLancamentoFinanceiro;
import br.com.milkmoney.service.LancamentoFinanceiroService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;


@Controller
public class LancamentoFinanceiroOverviewController {
	
	//LANÇAMENTOS
	@FXML private TableView<LancamentoFinanceiro> tableReceitas;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataEmissaoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataVencimentoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> categoriaLancamentoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> centroCustoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> descricaoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> valorColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> tipoLancamentoFinanceiroColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> situacaoLancamentoFinanceiroColumn;
	
	@FXML private TableView<LancamentoFinanceiro> tableDespesas;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataEmissaoColumn1;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataVencimentoColumn1;
	@FXML private TableColumn<LancamentoFinanceiro, String> categoriaLancamentoColumn1;
	@FXML private TableColumn<LancamentoFinanceiro, String> centroCustoColumn1;
	@FXML private TableColumn<LancamentoFinanceiro, String> descricaoColumn1;
	@FXML private TableColumn<LancamentoFinanceiro, String> valorColumn1;
	@FXML private TableColumn<LancamentoFinanceiro, String> tipoLancamentoFinanceiroColumn1;
	@FXML private TableColumn<LancamentoFinanceiro, String> situacaoLancamentoFinanceiroColumn1;
	
	@FXML private ToggleButton tbJan, tbFev, tbMar, tbAbr, tbMai, tbJun, tbJul, tbAgo, tbSet, tbOut, tbNov, tbDez, tbReceita, tbDespesa;
	@FXML private Label lblAno, lblReceitas, lblDespesas, lblSaldo, lblCustoLitroLeite, lblMargemLucro;
	@FXML private VBox vbChart;
	
	@FXML private TextField inputPesquisa;
	
	@FXML private TableView<SaldoCategoriaDespesa> tableSaldoCategoriaDespesa;
	@FXML private TableColumn<SaldoCategoriaDespesa, String> categoriaColumn;
	@FXML private TableColumn<SaldoCategoriaDespesa, String> saldoColumn;
	@FXML private TableColumn<SaldoCategoriaDespesa, String> percentualColumn;
	
	@Autowired private LancamentoFinanceiroService service;
	@Autowired private LancamentoFinanceiroFormController formController;
	
	private int selectedAno = LocalDate.now().getYear();
	private int selectedMes = LocalDate.now().getMonthValue();

	private ToggleGroup groupMes  = new ToggleGroup();
	
	private final CategoryAxis              yAxis     = new CategoryAxis();
    private final NumberAxis                xAxis     = new NumberAxis();
	private final BarChart<String, Number>  barChart  = new BarChart<>(yAxis,xAxis);
	
	private ContextMenu contextMenu = new ContextMenu();
	
	MenuItem editar = new MenuItem("Editar");
	MenuItem remover = new MenuItem("Remover");
	
	private LancamentoFinanceiro lancamentoFinanceiroSelecionado;
	
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
		
		situacaoLancamentoFinanceiroColumn1.setCellFactory(new TableCellSituacaoLancamentoFinanceiroFactory<LancamentoFinanceiro,String>("dataEmissao"));
		tipoLancamentoFinanceiroColumn1.setCellFactory(new TableCellTipoLancamentoFinanceiroFactory<LancamentoFinanceiro,String>("tipoLancamentoFormatado"));
		centroCustoColumn1.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("centroCusto"));
		dataEmissaoColumn1.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataEmissao"));
		dataVencimentoColumn1.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataVencimento"));
		categoriaLancamentoColumn1.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("categoria"));
		descricaoColumn1.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("descricao"));
		valorColumn1.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("valorTotalFormatado"));		
		
		//tabela saldos categorias
		categoriaColumn.setCellValueFactory(new PropertyValueFactory<SaldoCategoriaDespesa,String>("categoria"));
		saldoColumn.setCellValueFactory(new PropertyValueFactory<SaldoCategoriaDespesa,String>("saldo"));
		percentualColumn.setCellValueFactory(new PropertyValueFactory<SaldoCategoriaDespesa,String>("percentual"));
		
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
        
		editar.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleEdit();
		    }
		});
		
		remover.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	handleDelete();
		    }
		});
		
		contextMenu.getItems().clear();
		contextMenu.getItems().addAll(editar, remover);
		contextMenu.setPrefWidth(120);
		tableReceitas.setContextMenu(contextMenu);
		tableDespesas.setContextMenu(contextMenu);
		
		tableReceitas.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if ( event.isPrimaryButtonDown()	&& event.getClickCount() == 2 ) {
					handleEdit();
				}
				
				if ( event.isPrimaryButtonDown()	&& event.getClickCount() == 1 || event.isSecondaryButtonDown() ) {
					lancamentoFinanceiroSelecionado = tableReceitas.getSelectionModel().getSelectedItem();
				}
				
			}

		});
		
		tableDespesas.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if ( event.isPrimaryButtonDown()	&& event.getClickCount() == 2 ) {
					handleEdit();
				}
				
				if ( event.isPrimaryButtonDown()	&& event.getClickCount() == 1 || event.isSecondaryButtonDown() ) {
					lancamentoFinanceiroSelecionado = tableDespesas.getSelectionModel().getSelectedItem();
				}
			}
		});
		
		inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> refreshTableOverview());
		
	}
	
	@FXML
	private void clearFilter(){
		
	}
	
	@FXML
	private void closeForm(){
		
	}
	
	private void refreshTela(){
		updateChart();
		updateResumo();
		updateSaldosCategorias();
	}
	
	public void handleNew() {

		formController.setState(State.INSERT);
		formController.setObject(new LancamentoFinanceiro());
		formController.showForm();
		addObjectInTableView(formController.getObject());
		
	}
	
	public void handleEdit() {
		
		if (lancamentoFinanceiroSelecionado != null) {
			
			formController.setState(State.UPDATE);
			formController.setObject(lancamentoFinanceiroSelecionado);
			formController.showForm();
			refreshObjectInTableView(formController.getObject());
			
		} else {
			CustomAlert.nenhumRegistroSelecionado();
		}
		
	}

	protected void handleDelete() {
		if ( lancamentoFinanceiroSelecionado != null ) {
			Optional<ButtonType> result = CustomAlert.confirmarExclusao();
			if (result.get() == ButtonType.OK) {
				
				try {
					service.remove(lancamentoFinanceiroSelecionado);
				} catch (Exception e) {
					CustomAlert.mensagemAlerta("", e.getMessage());
					return;
				}
				
				if ( lancamentoFinanceiroSelecionado.getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA) ){
					tableReceitas.getItems().remove(lancamentoFinanceiroSelecionado);
				}else{
					tableDespesas.getItems().remove(lancamentoFinanceiroSelecionado);
				}
				
				lancamentoFinanceiroSelecionado = null;
				refreshTela();
			}
		} else {
			CustomAlert.nenhumRegistroSelecionado();		
		}
	}
	
	public void addObjectInTableView(LancamentoFinanceiro lancamento) {
		if ( lancamento != null ){
			LocalDate data = lancamento.getDataPagamento() != null ? DateUtil.asLocalDate(lancamento.getDataPagamento()) : 
				DateUtil.asLocalDate(lancamento.getDataVencimento());
			if ( data.getMonthValue() == selectedMes ){
				//somente adiciona na tabela se o lançamento vence no mês que está sendo exibido
				if ( lancamento.getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA) ){
					tableReceitas.getItems().add(lancamento);
				}else{
					tableDespesas.getItems().add(lancamento);
				}
			}
		}
	}
	
	public void refreshObjectInTableView(LancamentoFinanceiro lancamento) {
		
		if ( lancamento != null ){
			LocalDate data = lancamento.getDataPagamento() != null ? DateUtil.asLocalDate(lancamento.getDataPagamento()) : 
				DateUtil.asLocalDate(lancamento.getDataVencimento());
			boolean atualizou = false;
			if ( data.getMonthValue() == selectedMes ){
				if ( lancamento.getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA) ){
					for (int index = 0; index < tableReceitas.getItems().size(); index++) {
						LancamentoFinanceiro l = tableReceitas.getItems().get(index);
						if (l.getId() == lancamento.getId()) {
							tableReceitas.getItems().set(index, lancamento);
							//tableReceitas.layout();
							atualizou = true;
							break;
						}
					}
					//se não encontrou o objeto em uma tabela, tenta localizar na outra.
					//nesse caso ele foi alterado de débito para crédito por exemplo
					if ( !atualizou ){
						for (int index = 0; index < tableDespesas.getItems().size(); index++) {
							LancamentoFinanceiro l = tableDespesas.getItems().get(index);
							if (l.getId() == lancamento.getId()) {
								tableDespesas.getItems().remove(lancamento);
								tableReceitas.getItems().add(lancamento);
							}
						}
					}
					
				}else{
					for (int index = 0; index < tableDespesas.getItems().size(); index++) {
						LancamentoFinanceiro l = tableDespesas.getItems().get(index);
						if (l.getId() == lancamento.getId()) {
							tableDespesas.getItems().set(index, lancamento);
							//tableDespesas.layout();
							atualizou = true;
							break;
						}
					}
					
					//se não encontrou o objeto em uma tabela, tenta localizar na outra.
					//nesse caso ele foi alterado de crédito para débito por exemplo
					if ( !atualizou ){
						for (int index = 0; index < tableReceitas.getItems().size(); index++) {
							LancamentoFinanceiro l = tableReceitas.getItems().get(index);
							if (l.getId() == lancamento.getId()) {
								tableReceitas.getItems().remove(lancamento);
								tableDespesas.getItems().add(lancamento);
							}
						}
					}
				}
			}else{
				if ( lancamento.getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA) ){
					tableReceitas.getItems().remove(lancamento);
				}else{
					tableDespesas.getItems().remove(lancamento);
				}
				lancamentoFinanceiroSelecionado = null;
			}
			
		}
		
		refreshTela();
	}
	
	protected void refreshTableOverview() {
		
		this.tableReceitas.getItems().clear();
		this.tableDespesas.getItems().clear();
		
		if ( inputPesquisa != null && inputPesquisa.getText() != null &&
				inputPesquisa.getText().length() > 0){
			handleDefaultSearch();
		}else{
			tableDespesas.setItems(service.findByMes(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()), TipoLancamentoFinanceiro.DESPESA));
			tableReceitas.setItems(service.findByMes(DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()), TipoLancamentoFinanceiro.RECEITA));
		}
		
		tableDespesas.layout();
		tableReceitas.layout();
		
		refreshTela();
	}
	
	public void handleDefaultSearch() {
		List<LancamentoFinanceiro> data = service.findByMes(inputPesquisa.getText(), DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		
		for ( LancamentoFinanceiro l : data ){
			if ( l.getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA) ){
				tableReceitas.getItems().add(l);
			}else{
				tableDespesas.getItems().add(l);
			}
		}
		
	}
	
	private void updateChart(){
		
		ObservableList<LancamentoFinanceiro> data = FXCollections.observableArrayList();
		data.addAll(tableReceitas.getItems());
		data.addAll(tableDespesas.getItems());
		
		barChart.getData().clear();
        barChart.getData().addAll(service.getDataChart(data));
	}
	
	private void updateResumo(){
		
		BigDecimal receitas = new BigDecimal(0);
		BigDecimal despesas = new BigDecimal(0);
		BigDecimal saldo = new BigDecimal(0);
		
		for ( LancamentoFinanceiro lancamento : tableReceitas.getItems() ){
			receitas = receitas.add(lancamento.getValorTotal());
		}
		
		for ( LancamentoFinanceiro lancamento : tableDespesas.getItems() ){
			despesas = despesas.add(lancamento.getValorTotal());
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
	
	public String getFormTitle() {
		return "Lançamento Financeiro";
	}
	
	public String getFormName() {
		return "view/cobertura/LancamentoFinanceiroOverview.fxml";
	}

}
