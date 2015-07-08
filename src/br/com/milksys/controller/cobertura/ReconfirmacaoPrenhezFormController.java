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
public class ReconfirmacaoPrenhezFormController extends AbstractFormController<Integer, Cobertura> {

	@FXML private DatePicker inputData;
	@FXML private ComboBox<String> inputSituacaoCobertura;
	@FXML private UCTextField inputObservacao;
	
	@Autowired CoberturaService service;
	@Autowired CoberturaOverviewController coberturaOverviewController;

	@FXML
	public void initialize() {
		
		inputData.valueProperty().bindBidirectional(getObject().dataReconfirmacaoPrenhezProperty());
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputSituacaoCobertura.valueProperty().bindBidirectional(getObject().situacaoReconfirmacaoPrenhezProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoReconfirmacaoPrenhezProperty());
	
	}
	
	@FXML
	private void handleSaveReconfirmacaoPrenhez(){
		try {
			((CoberturaService)service).registrarReconfirmacaoPrenhez(getObject());
			super.closeForm();
			coberturaOverviewController.refreshObjectInTableView(getObject());
		} catch (ValidationException e) {
			CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
			return;
		}
	}
	
	@FXML
	private void handleRemoverRegistroReconfirmacaoPrenhez(){
		try {
			Optional<ButtonType> result = CustomAlert.confirmarExclusao("Confirmar remoção registro", "Tem certeza que deseja remover o registro da reconfirmação?");
			if (result.get() == ButtonType.OK) {
				((CoberturaService)service).removerRegistroReconfirmacaoPrenhez(getObject());
				super.closeForm();
				coberturaOverviewController.refreshObjectInTableView(getObject());
			}
		} catch (ValidationException e) {
			CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
			return;
		}
	}
	
	@Override
	protected String getFormName() {
		return "view/cobertura/ReconfirmacaoPrenhezForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Reconfirmação";
	}

	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}
	
}
