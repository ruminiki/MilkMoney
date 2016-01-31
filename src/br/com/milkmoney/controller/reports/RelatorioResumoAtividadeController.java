package br.com.milkmoney.controller.reports;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.service.RelatorioService;

@Controller
public class RelatorioResumoAtividadeController extends AbstractReport{

	@FXML private TextField inputAno;
	
	@FXML
	public void initialize(){
		
		inputAno.setText(String.valueOf(LocalDate.now().getYear()));
		MaskFieldUtil.numeroInteiroWithouMask(inputAno);
		
		super.initialize();
	}
	
	@Override
	protected void handleExecutar(){
		
		Object[] params = new Object[]{inputAno.getText() != "" ? Integer.parseInt(inputAno.getText()) : LocalDate.now().getYear() };
		
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
