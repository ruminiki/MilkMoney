package br.com.milkmoney.controller.lancamentoFinanceiro;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.lancamentoFinanceiro.renderer.TableCellSituacaoLancamentoFinanceiroFactory;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.service.LancamentoFinanceiroService;
import br.com.milkmoney.util.DateUtil;


@Controller
public class LancamentoFinanceiroOverviewController extends AbstractOverviewController<Integer, LancamentoFinanceiro> {

	//LANÇAMENTOS
	@FXML private TableColumn<LancamentoFinanceiro, String> dataEmissaoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataVencimentoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataPagamentoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> centroCustoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> descricaoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> valorColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> tipoLancamentoFinanceiroColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> situacaoLancamentoFinanceiroColumn;
	@FXML private ToggleButton tbJan, tbFev, tbMar, tbAbr, tbMai, tbJun, tbJul, tbAgo, tbSet, tbOut, tbNov, tbDez;
	@FXML private Label lblAno;
	
	@Autowired private LancamentoFinanceiroService service;
	
	private int selectedAno = LocalDate.now().getYear();
	private int selectedMes = LocalDate.now().getMonthValue();

	private ToggleGroup groupMes = new ToggleGroup();
	
	@FXML
	public void initialize() {
		
		situacaoLancamentoFinanceiroColumn.setCellFactory(new TableCellSituacaoLancamentoFinanceiroFactory<LancamentoFinanceiro,String>("dataEmissao"));
		tipoLancamentoFinanceiroColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("tipoLancamentoFormatado"));
		centroCustoColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("centroCusto"));
		dataEmissaoColumn.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataEmissao"));
		dataVencimentoColumn.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataVencimento"));
		dataPagamentoColumn.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataPagamento"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("descricao"));
		valorColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("valorTotal"));
		super.initialize((LancamentoFinanceiroFormController) MainApp.getBean(LancamentoFinanceiroFormController.class));
		super.setService(service);
		
		groupMes.getToggles().clear();
		groupMes.getToggles().addAll(tbJan, tbFev, tbMar, tbAbr, tbMai, tbJun, tbJul, tbAgo, tbSet, tbOut, tbNov, tbDez);
		groupMes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> selectToggle( newValue ));
		groupMes.getToggles().get(selectedMes - 1).setSelected(true);
		
		lblAno.setText(String.valueOf(selectedAno));
		
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
		updateLabelNumRegistros();
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
