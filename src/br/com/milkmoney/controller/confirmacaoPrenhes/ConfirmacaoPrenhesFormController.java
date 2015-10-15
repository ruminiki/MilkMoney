package br.com.milkmoney.controller.confirmacaoPrenhes;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.MetodoConfirmacaoPrenhes;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;

@Controller
public class ConfirmacaoPrenhesFormController extends AbstractFormController<Integer, Cobertura> {

	@FXML private DatePicker       inputData;
	@FXML private Label            lblCobertura;
	@FXML private ComboBox<String> inputSituacaoCobertura, inputMetodoConfirmacao;
	@FXML private UCTextField      inputObservacao;
	@FXML private Button           btnSalvar, btnRemover;
	
	@FXML
	public void initialize() {
		
		lblCobertura.setText(getObject().toString());
		
		inputData.valueProperty().bindBidirectional(getObject().dataConfirmacaoPrenhesProperty());
		inputData.setValue(DateUtil.asLocalDate(getObject().getData()).plusDays(30));
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputSituacaoCobertura.valueProperty().bindBidirectional(getObject().situacaoCoberturaProperty());
		inputMetodoConfirmacao.setItems(MetodoConfirmacaoPrenhes.getItems());
		inputMetodoConfirmacao.valueProperty().bindBidirectional(getObject().metodoConfirmacaoPrenhesProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		btnSalvar.setDisable(getObject().getParto() != null);
		btnRemover.setDisable(getObject().getParto() != null);
		
	}
	
	@FXML
	private void handleDesfazerConfirmacao(){
		Optional<ButtonType> result = CustomAlert.confirmarExclusao("Desfazer Confirmação de Prenhes", "Tem certeza que deseja desfazer a confirmação de prenhes?");
		if (result.get() == ButtonType.OK) {
			((CoberturaService)service).desfazerConfirmacaoPrenhes(getObject());			
		}
	}
	
	@Override
	protected void beforeSave() {
		CoberturaValidation.validaConfirmacaoPrenhes(getObject());
	}
	
	@Override
	public String getFormName() {
		return "view/confirmacaoPrenhes/ConfirmacaoPrenhesForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Confirmação Prenhes";
	}
	
	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}

}
