package br.com.milkmoney.controller.reports;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.service.RelatorioService;

@Controller
public class RelatorioProducaoController extends AbstractReport{

	@FXML private TextField inputAno;
	
	@FXML
	public void initialize(){
		super.initialize();
		inputAno.setText(""+LocalDate.now().getYear());
		MaskFieldUtil.numeroInteiro(inputAno);
	}
	
	@FXML
	private void handleExecutar(){
		
		Object[] params = new Object[]{inputAno.getText() != "" ? Integer.parseInt(inputAno.getText()) : LocalDate.now().getYear()};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_PRODUCAO, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_PRODUCAO, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
		
	}

}
