package br.com.milkmoney.controller.confirmacaoPrenhez;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.MetodoConfirmacaoPrenhez;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;

@Controller
public class ConfirmacaoPrenhezFormController extends AbstractFormController<Integer, Cobertura> {

	@FXML private DatePicker       inputData;
	@FXML private Label            lblCobertura;
	@FXML private ChoiceBox<String> inputSituacaoCobertura, inputMetodoConfirmacao;
	@FXML private UCTextField      inputObservacao;
	@FXML private Button           btnSalvar, btnRemover;
	
	@FXML
	public void initialize() {
		
		lblCobertura.setText(getObject().toString());
		
		inputData.valueProperty().bindBidirectional(getObject().dataConfirmacaoPrenhezProperty());
		inputData.setValue(DateUtil.asLocalDate(getObject().getData()).plusDays(30));
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputSituacaoCobertura.valueProperty().bindBidirectional(getObject().situacaoConfirmacaoPrenhezProperty());
		inputMetodoConfirmacao.setItems(MetodoConfirmacaoPrenhez.getItems());
		inputMetodoConfirmacao.valueProperty().bindBidirectional(getObject().metodoConfirmacaoPrenhezProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		btnSalvar.setDisable(getObject().getParto() != null);
		btnRemover.setDisable(getObject().getParto() != null);
		
	}
	
	@FXML
	private void handleDesfazerConfirmacao(){
		CustomAlert.confirmarExclusao("Desfazer Confirma��o de Prenhez", "Tem certeza que deseja desfazer a confirma��o de prenhez?");
		if (CustomAlert.response == ButtonType.OK) {
			((CoberturaService)service).desfazerConfirmacaoPrenhez(getObject());
			closeForm();
		}
	}
	
	@Override
	protected void beforeSave() {
		CoberturaValidation.validaConfirmacaoPrenhez(getObject());
		getObject().setSituacaoCobertura(getObject().getSituacaoConfirmacaoPrenhez());
	}
	
	@Override
	public String getFormName() {
		return "view/confirmacaoPrenhez/ConfirmacaoPrenhezForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Confirma��o Prenhez";
	}
	
	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}

}