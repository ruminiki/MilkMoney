package br.com.milkmoney.controller.reports;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.service.RelatorioService;

@Controller
public class RelatorioPartosPrevistosController extends AbstractReport{

	@FXML private TextField inputQuantidadeDias;
	
	@FXML
	public void initialize(){
		super.initialize();
		inputQuantidadeDias.setText("30");
		MaskFieldUtil.numeroInteiroWithouMask(inputQuantidadeDias);
	}
	
	@Override
	protected void handleExecutar(){
		
		Object[] params = new Object[]{inputQuantidadeDias.getText() != "" ? Integer.parseInt(inputQuantidadeDias.getText()) : 30 };
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_PARTOS_PREVISTOS, params), MainApp.primaryStage);
		}else{
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_PARTOS_PREVISTOS, params), MainApp.primaryStage);
		}
		
		super.handleClose();
		
	}

}
