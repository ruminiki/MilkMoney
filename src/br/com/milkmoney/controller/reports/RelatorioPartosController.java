package br.com.milkmoney.controller.reports;

import java.time.LocalDate;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.model.ComplicacaoParto;
import br.com.milkmoney.model.TipoParto;
import br.com.milkmoney.service.ComplicacaoPartoService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class RelatorioPartosController extends AbstractReport{

	@FXML private DatePicker inputDataDe, inputDataAte;
	@FXML private ChoiceBox<String> inputTipoParto;
	@FXML private ChoiceBox<ComplicacaoParto> inputComplicacaoParto;
	
	@Autowired private ComplicacaoPartoService complicacaoPartoService;
	
	@FXML
	public void initialize(){
		
		inputDataDe.setValue(LocalDate.now().minusDays(365));
		inputDataAte.setValue(LocalDate.now());
		
		inputTipoParto.setItems(TipoParto.getItems());
		inputComplicacaoParto.setItems(complicacaoPartoService.findAllAsObservableList());
		
		super.initialize();
	}
	
	@FXML
	private void handleExecutar(){
		
		Object[] params = new Object[]{
				inputDataDe.getValue() == null ? new Date() : DateUtil.asDate(inputDataDe.getValue()),
				inputDataAte.getValue() == null ? new Date() : DateUtil.asDate(inputDataAte.getValue()),
				inputTipoParto.getValue() == null ? "" : inputTipoParto.getValue(),
				inputComplicacaoParto.getValue() == null ? 0 : inputComplicacaoParto.getValue().getId()
		};
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_PARTOS, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_PARTOS, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
		
	}

}
