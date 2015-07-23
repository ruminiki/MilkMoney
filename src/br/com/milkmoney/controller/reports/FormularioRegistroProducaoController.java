package br.com.milkmoney.controller.reports;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.util.Util;
import br.com.milkmoney.validation.Validator;

@Controller
public class FormularioRegistroProducaoController{

	@FXML private ListView<String> listMeses;
	@Autowired private RelatorioService relatorioService;
	
	
	@FXML
	public void initialize() {
		listMeses.setItems(Util.generateListMonths());
	}
	
	@FXML
	private void handleClose(){
		if ( listMeses != null ){
			Stage stage = (Stage)listMeses.getScene().getWindow();
			// se for popup
			if ( stage.getModality().equals(Modality.APPLICATION_MODAL) ){
				((Stage)listMeses.getScene().getWindow()).close();	
			}else{
				MainApp.resetLayout();
			}
		}
	}
	
	@FXML
	private void handleExecutar(){
		
		if ( listMeses.getItems().size() <= 0 ){
			throw new ValidationException(Validator.CAMPO_OBRIGATORIO, "Por favor, selecione um mês para executar o relatório.");
		}
		
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FORMULARIO_CAMPO_REGISTRO_PRODUCAO, listMeses.getSelectionModel().getSelectedIndex() + 1);
		
		handleClose();
	}

}
