package br.com.milkmoney.controller.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.WaitReport;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.validation.Validator;

@Controller
public class FormularioRegistroPartoController extends AbstractSelectAnimalParametersReport {
	
	@Autowired private RootLayoutController rootLayoutController;

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
		
		sb.replace(sb.length(), sb.length(), "");
		
		WaitReport.wait(relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FORMULARIO_CAMPO_REGISTRO_PARTO, sb.toString()), MainApp.primaryStage);
		
		super.handleClose();
		
	}

}
