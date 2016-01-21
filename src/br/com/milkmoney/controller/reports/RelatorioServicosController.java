package br.com.milkmoney.controller.reports;

import java.time.LocalDate;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.model.PrestadorServico;
import br.com.milkmoney.service.PrestadorServicoService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class RelatorioServicosController extends AbstractReport{

	@FXML private DatePicker inputDataDe, inputDataAte;
	@FXML private ChoiceBox<PrestadorServico> inputPrestadorServico;

	@Autowired private PrestadorServicoService prestadorServicoService;
	
	@FXML
	public void initialize(){
		
		inputDataDe.setValue(LocalDate.now().minusDays(365));
		inputDataAte.setValue(LocalDate.now());
		
		inputPrestadorServico.setItems(prestadorServicoService.findAllAsObservableList());
		
		super.initialize();
	}
	
	@Override
	protected void handleExecutar(){
		
		Object[] params = new Object[]{
				inputDataDe.getValue() == null ? new Date() : DateUtil.asDate(inputDataDe.getValue()),
				inputDataAte.getValue() == null ? new Date() : DateUtil.asDate(inputDataAte.getValue()),
				inputPrestadorServico.getValue() == null ? 0 : inputPrestadorServico.getValue().getId()
		};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_SERVICOS, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_SERVICOS, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
		
	}

}
