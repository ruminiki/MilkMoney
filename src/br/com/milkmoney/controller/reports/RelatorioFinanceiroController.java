package br.com.milkmoney.controller.reports;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.categoriaLancamentoFinanceiro.CategoriaLancamentoFinanceiroReducedOverviewController;
import br.com.milkmoney.controller.centroCusto.CentroCustoReducedOverviewController;
import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;
import br.com.milkmoney.model.CentroCusto;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class RelatorioFinanceiroController extends AbstractReport{

	@FXML private DatePicker inputDataDe, inputDataAte;
	@FXML private UCTextField inputCentroCusto, inputCategoria, inputDescricao;
	
	@Autowired private CentroCustoReducedOverviewController centroCustoReducedOverviewController;
	@Autowired private CategoriaLancamentoFinanceiroReducedOverviewController categoriaReducedOverviewController;
	
	private CategoriaLancamentoFinanceiro categoria;
	private CentroCusto centroCusto;
	
	@FXML
	public void initialize(){
		super.initialize();
		inputDataDe.setValue(LocalDate.now().minusMonths(1));
		inputDataAte.setValue(LocalDate.now());
		categoria = null;
		centroCusto = null;
	}
	
	@FXML
	private void handleSelecionarCentroCusto() {
		
		centroCustoReducedOverviewController.showForm();
		centroCusto = centroCustoReducedOverviewController.getObject();
		inputCentroCusto.textProperty().set(centroCusto != null ? centroCusto.toString() : "");	
		
	}
	
	@FXML
	private void handleSelecionarCategoria() {
		
		categoriaReducedOverviewController.showForm();
		categoria = categoriaReducedOverviewController.getObject();
		inputCategoria.textProperty().set(categoria != null ? categoria.toString() : "");	
		
	}
	
	@FXML
	private void handleExecutar(){
		
		Object[] params = new Object[]{
				DateUtil.asDate(inputDataDe.getValue()),
				DateUtil.asDate(inputDataAte.getValue()),
				inputDescricao.getText(),
				categoria != null ? categoria.getId() : 0,
				centroCusto != null ? centroCusto.getId() : 0
		};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_LANCAMENTOS_FINANCEIROS, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_LANCAMENTOS_FINANCEIROS, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
		
	}

}
