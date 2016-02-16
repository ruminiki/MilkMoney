package br.com.milkmoney.controller.reports;

import java.time.LocalDate;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class RelatorioAbortosController extends AbstractReport{

	@FXML private DatePicker inputDataDe, inputDataAte;
	
	@FXML
	public void initialize(){
		
		inputDataDe.setValue(LocalDate.now().minusDays(365));
		inputDataAte.setValue(LocalDate.now());
		
		super.initialize();
	}
	
	@Override
	protected void handleExecutar(){
		
		Object[] params = new Object[]{
				inputDataDe.getValue() == null ? new Date() : DateUtil.asDate(inputDataDe.getValue()),
				inputDataAte.getValue() == null ? new Date() : DateUtil.asDate(inputDataAte.getValue())
		};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_ABORTOS, params), MainApp.primaryStage);
		}else{
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_ABORTOS, params), MainApp.primaryStage);
		}
		
		super.handleClose();
		
	}

}
