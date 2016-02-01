package br.com.milkmoney.controller.lancamentoFinanceiro;

import java.math.BigDecimal;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.lancamentoFinanceiro.renderer.TableCellSituacaoLancamentoFinanceiroFactory;
import br.com.milkmoney.controller.lancamentoFinanceiro.renderer.TableCellTipoLancamentoFinanceiroFactory;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.service.LancamentoFinanceiroService;
import br.com.milkmoney.util.NumberFormatUtil;


@Controller
public class ParcelasOverviewController{
	
	//LANÇAMENTOS
	@FXML private TableView<LancamentoFinanceiro> table;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataEmissaoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataVencimentoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> dataPagamentoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> categoriaLancamentoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> centroCustoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> descricaoColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> valorColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> tipoLancamentoFinanceiroColumn;
	@FXML private TableColumn<LancamentoFinanceiro, String> situacaoLancamentoFinanceiroColumn;
	
	@FXML private Label lblTotal, lblPago, lblSaldo;
	@Autowired private LancamentoFinanceiroService service;
	
	private String parcela = null;
	
	@FXML
	public void initialize() {
		
		situacaoLancamentoFinanceiroColumn.setCellFactory(new TableCellSituacaoLancamentoFinanceiroFactory<LancamentoFinanceiro,String>("dataEmissao"));
		tipoLancamentoFinanceiroColumn.setCellFactory(new TableCellTipoLancamentoFinanceiroFactory<LancamentoFinanceiro,String>("tipoLancamentoFormatado"));
		centroCustoColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("centroCusto"));
		dataEmissaoColumn.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataEmissao"));
		dataVencimentoColumn.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataVencimento"));
		dataPagamentoColumn.setCellFactory(new TableCellDateFactory<LancamentoFinanceiro,String>("dataPagamento"));
		categoriaLancamentoColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("categoria"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("descricao"));
		valorColumn.setCellValueFactory(new PropertyValueFactory<LancamentoFinanceiro,String>("valorTotalFormatado"));
		
		table.getItems().clear();
		table.getItems().addAll(service.findByParcela(parcela));
		updateResumo();
	}
	
	private void updateResumo(){
		
		BigDecimal total = new BigDecimal(0);
		BigDecimal pago = new BigDecimal(0);
		BigDecimal saldo = new BigDecimal(0);
		
		for ( LancamentoFinanceiro lancamento : table.getItems() ){
			total = total.add(lancamento.getValorTotal());
			if ( lancamento.getDataPagamento() != null ){
				pago = pago.add(lancamento.getValorTotal());
			}else{
				saldo = saldo.add(lancamento.getValorTotal());
			}
		}
		
		lblTotal.setText("R$ " + NumberFormatUtil.decimalFormat(total));
		lblPago.setText("R$ " + NumberFormatUtil.decimalFormat(pago));
		lblSaldo.setText("R$ " + NumberFormatUtil.decimalFormat(saldo));
		
	}
	
	public void showForm(){
    	
    	AnchorPane form = (AnchorPane) MainApp.load(getFormName());
    	
		Stage dialogStage = new Stage();
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		
		dialogStage.setTitle(getFormTitle());
		
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);

		dialogStage.setResizable(false);
		dialogStage.showAndWait();
		
    }
	
	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getFormName() {
		return "view/lancamentoFinanceiro/ParcelasOverview.fxml";
	}

	public String getFormTitle() {
		return "Parcelas";
	}

}
