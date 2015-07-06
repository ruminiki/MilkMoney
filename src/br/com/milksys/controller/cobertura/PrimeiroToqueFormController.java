package br.com.milksys.controller.cobertura;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.service.CoberturaService;
import br.com.milksys.service.IService;

@Controller
public class PrimeiroToqueFormController extends AbstractFormController<Integer, Cobertura> {

	@FXML private DatePicker inputData;
	@FXML private ComboBox<String> inputSituacaoCobertura;
	@FXML private UCTextField inputObservacao;
	
	@Autowired CoberturaService service;
	
	@FXML
	public void initialize() {
		
		inputData.valueProperty().bindBidirectional(getObject().dataPrimeiroToqueProperty());
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputSituacaoCobertura.valueProperty().bindBidirectional(getObject().situacaoPrimeiroToqueToqueProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoPrimeiroToqueProperty());
	
	}
	
	@FXML
	private void handleSavePrimeiroToque(){
		try {
			((CoberturaService)service).registrarPrimeiroToque(getObject());
			super.closeForm();
			//refreshObjectInTableView(getObject());
		} catch (ValidationException e) {
			CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
			return;
		}
	}
	
	@FXML
	private void handleRemoverRegistroPrimeiroToque(){
		try {
			Optional<ButtonType> result = CustomAlert.confirmarExclusao("Confirmar remoção registro", "Tem certeza que deseja remover o registro do primeiro toque?");
			if (result.get() == ButtonType.OK) {
				((CoberturaService)service).removerRegistroPrimeiroToque(getObject());
				super.closeForm();
				//refreshObjectInTableView(getObject());
			}
		} catch (ValidationException e) {
			CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
			return;
		}
	}
	
	@Override
	protected String getFormName() {
		return "view/cobertura/RegistrarPrimeiroToqueForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Primeiro Toque";
	}

	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}
	
}
