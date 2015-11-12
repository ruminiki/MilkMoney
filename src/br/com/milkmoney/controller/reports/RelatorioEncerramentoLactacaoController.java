package br.com.milkmoney.controller.reports;

import java.time.LocalDate;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.model.MotivoEncerramentoLactacao;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class RelatorioEncerramentoLactacaoController extends AbstractReport{

	@FXML private DatePicker inputDataDe, inputDataAte;
	@FXML private ChoiceBox<String> inputMotivoEncerramentoLactacao;
	
	@FXML
	public void initialize(){
		
		inputDataDe.setValue(LocalDate.now().minusDays(365));
		inputDataAte.setValue(LocalDate.now());
		
		inputMotivoEncerramentoLactacao.getItems().clear();
		inputMotivoEncerramentoLactacao.getItems().addAll(MotivoEncerramentoLactacao.getItems());
		
		super.initialize();
	}
	
	@FXML
	private void handleExecutar(){
		
		Object[] params = new Object[]{
				inputDataDe.getValue() == null ? new Date() : DateUtil.asDate(inputDataDe.getValue()),
				inputDataAte.getValue() == null ? new Date() : DateUtil.asDate(inputDataAte.getValue()),
				inputMotivoEncerramentoLactacao.getSelectionModel().getSelectedItem()
		};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_ENCERRAMENTO_LACTACAO, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_ENCERRAMENTO_LACTACAO, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
		
	}

}
