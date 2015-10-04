package br.com.milkmoney.controller.reports;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.tipoProcedimento.TipoProcedimentoReducedOverviewController;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.Validator;

@Controller
public class RelatorioProcedimentosController extends AbstractSelectAnimalParametersReport {

	@FXML private DatePicker inputDataInicio, inputDataFim;
	@FXML private UCTextField inputTipoProcedimento, inputObservacao;
	
	@Autowired private TipoProcedimentoReducedOverviewController tipoProcedimentoReducedOverviewController;
	
	@FXML
	public void initialize() {
		super.initialize();
		
		inputDataInicio.setValue(LocalDate.now().minusDays(30));
		inputDataFim.setValue(LocalDate.now());
		
	}
	
	@FXML
	private void handleSelecionarTipoProcedimento(){
	    	
		tipoProcedimentoReducedOverviewController.showForm();
		if ( tipoProcedimentoReducedOverviewController.getObject() != null ){
			inputTipoProcedimento.setText(tipoProcedimentoReducedOverviewController.getObject().toString());	
		}else{
			inputTipoProcedimento.setText("");
		}
		
	}
	
	@FXML
	private void handleExecutar(){
		
		if ( listSelecionados.getItems().size() <= 0 ){
			throw new ValidationException(Validator.CAMPO_OBRIGATORIO, "Por favor, selecione os animais para executar o relatório.");
		}
		
		//os ids dos animais selecionados são passados como parâmetro
		StringBuilder sb = new StringBuilder();
		
		for ( Animal animal : listSelecionados.getItems() ){
			sb.append(animal.getId());
			sb.append(",");
		}
		
		sb.replace(sb.length(), sb.length(), "");
		
		Object[] params = new Object[]{
				sb.toString(),
				inputDataInicio.getValue() != null ? DateUtil.asDate(inputDataInicio.getValue()) : null,
				inputDataFim.getValue() != null ? DateUtil.asDate(inputDataFim.getValue()) : null,
				inputTipoProcedimento.getText() == null ? "" : inputTipoProcedimento.getText(),
				inputObservacao.getText() == null ? "" : inputObservacao.getText()
		};
		
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_PROCEDIMENTOS, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_PROCEDIMENTOS, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
	}

}
