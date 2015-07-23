package br.com.milksys.controller.reports;

import javafx.fxml.FXML;

import org.springframework.stereotype.Controller;

import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Animal;
import br.com.milksys.service.RelatorioService;
import br.com.milksys.validation.Validator;

@Controller
public class RelatorioFichaAnimalController extends AbstractSelectAnimalParametersReport {

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
		
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FICHA_ANIMAL, sb.toString());
		
		super.handleClose();
	}

}
