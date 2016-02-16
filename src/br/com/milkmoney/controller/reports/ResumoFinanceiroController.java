package br.com.milkmoney.controller.reports;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.controller.centroCusto.CentroCustoReducedOverviewController;
import br.com.milkmoney.model.CentroCusto;
import br.com.milkmoney.service.RelatorioService;

@Controller
public class ResumoFinanceiroController extends AbstractReport{

	@FXML private TextField inputAno;
	@FXML private UCTextField inputCentroCusto;
	
	@Autowired private CentroCustoReducedOverviewController centroCustoReducedOverviewController;
	
	private CentroCusto centroCusto;
	
	@FXML
	public void initialize(){
		super.initialize();
		inputAno.setText(""+LocalDate.now().getYear());
		MaskFieldUtil.numeroInteiroWithouMask(inputAno);
		centroCusto = null;
	}
	
	@FXML
	private void handleSelecionarCentroCusto() {
		
		centroCustoReducedOverviewController.showForm();
		centroCusto = centroCustoReducedOverviewController.getObject();
		inputCentroCusto.textProperty().set(centroCusto != null ? centroCusto.toString() : "");	
		
	}
	
	@Override
	protected void handleExecutar(){
		
		Object[] params = new Object[]{
				inputAno.getText() != "" ? Integer.parseInt(inputAno.getText()) : LocalDate.now().getYear(),
				centroCusto != null ? centroCusto.getId() : 0,
				centroCusto != null ? centroCusto.getDescricao() : "TODOS"};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RESUMO_FINANCEIRO, params), MainApp.primaryStage);
		}else{
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RESUMO_FINANCEIRO, params), MainApp.primaryStage);
		}
		
		super.handleClose();
		
	}

}
