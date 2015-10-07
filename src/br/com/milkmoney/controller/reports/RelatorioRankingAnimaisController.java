package br.com.milkmoney.controller.reports;

import javafx.fxml.FXML;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.validation.Validator;

@Controller
public class RelatorioRankingAnimaisController extends AbstractSelectAnimalParametersReport {

	@FXML
	private void handleExecutar(){
		
		if ( listSelecionados.getItems().size() <= 0 ){
			throw new ValidationException(Validator.CAMPO_OBRIGATORIO, "Por favor, selecione os animais para executar o relat�rio.");
		}
		
		//os ids dos animais selecionados s�o passados como par�metro
		StringBuilder sb = new StringBuilder();
		
		for ( Animal animal : listSelecionados.getItems() ){
			sb.append(animal.getId());
			sb.append(",");
		}
		
		sb.replace(sb.length(), sb.length(), "");
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_RANKING_ANIMAIS, sb.toString());
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_RANKING_ANIMAIS, sb.toString());
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relat�rio est� sendo executado...");
		
	}

}