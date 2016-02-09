package br.com.milkmoney.controller.reports;

import java.time.LocalDate;
import java.util.Date;

import javafx.fxml.FXML;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class RelatorioProximosEncerramentosLactacaoController extends AbstractReport{

	@FXML private UCTextField inputDias;
	
	@FXML
	public void initialize(){
		
		inputDias.setText("30");
		MaskFieldUtil.numeroInteiroWithouMask(inputDias);
		
		super.initialize();
	}
	
	@Override
	protected void handleExecutar(){
		
		Date dataInicio = new Date();
		Date dataFim    = inputDias.getText().isEmpty() ? DateUtil.asDate(LocalDate.now().plusDays(30)) : DateUtil.asDate(LocalDate.now().plusDays(Integer.parseInt(inputDias.getText())));
		
		Object[] params = new Object[]{
				dataInicio,
				dataFim
		};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_PROXIMOS_ENCERRAMENTOS_LACTACAO, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_PROXIMOS_ENCERRAMENTOS_LACTACAO, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
		
	}

}
