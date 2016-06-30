package br.com.milkmoney.controller.reports;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.Validator;

@Controller
public class RelatorioControleLeiteiroController extends AbstractSelectAnimalParametersReport {

	@FXML private DatePicker inputDataInicio, inputDataFim;
	
	@FXML
	public void initialize() {
		super.initialize();
		
		inputDataInicio.setValue(LocalDate.now().minusYears(1));
		inputDataFim.setValue(LocalDate.now());
		
	}
	
	@Override
	public void handleExecutar(){
		
		if ( listSelecionados.getItems().size() <= 0 ){
			throw new ValidationException(Validator.CAMPO_OBRIGATORIO, "Por favor, selecione os animais para executar o relatório.");
		}
		
		//os ids dos animais selecionados são passados como parâmetro
		StringBuilder sb = new StringBuilder();
		
		for ( Animal animal : listSelecionados.getItems() ){
			sb.append(animal.getId());
			sb.append(",");
		}
		
		sb.setLength(sb.length() - 1);
		
		Object[] params = new Object[]{
				sb.toString(),
				inputDataInicio.getValue() != null ? DateUtil.asDate(inputDataInicio.getValue()) : null,
				inputDataFim.getValue() != null ? DateUtil.asDate(inputDataFim.getValue()) : null
		};
		
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_CONTROLE_LEITEIRO, params), MainApp.primaryStage);
		}else{
			WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_CONTROLE_LEITEIRO, params), MainApp.primaryStage);
		}
		
		super.handleClose();
	}

}
