package br.com.milkmoney.controller.reports;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.validation.Validator;

@Controller
public class RelatorioRankingAnimaisController extends AbstractSelectAnimalParametersReport {

	@Override
	protected void handleExecutar(){
		
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
			listSelecionados.getItems(),
			sb.toString()
		};

		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_RANKING_ANIMAIS, params);
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_RANKING_ANIMAIS, params);
		}
		
		super.handleClose();
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
	}

}
