package br.com.milkmoney.controller.reports;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.service.RelatorioService;

@Controller
public class RelatorioPartosPrevistosController extends AbstractReport{

	@FXML private TextField inputQuantidadeDias;
	
	@FXML
	public void initialize(){
		super.initialize();
		inputQuantidadeDias.setText("30");
		MaskFieldUtil.numeroInteiro(inputQuantidadeDias);
	}
	
	@Override
	protected void handleExecutar(){
		
		Object[] params = new Object[]{inputQuantidadeDias.getText() != "" ? Integer.parseInt(inputQuantidadeDias.getText()) : 30 };
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_RESUMO_ATIVIDADE, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_RESUMO_ATIVIDADE, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
		
	}

}
