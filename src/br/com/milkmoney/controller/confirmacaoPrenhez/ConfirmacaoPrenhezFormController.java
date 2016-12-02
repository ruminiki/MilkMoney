package br.com.milkmoney.controller.confirmacaoPrenhez;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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

	@FXML private DatePicker        inputData;
	@FXML private Label             lblCobertura;
	@FXML private UCTextField       inputObservacao;
	@FXML private Button            btnSalvar, btnRemover;
	@FXML private CheckBox			cbPrenha, cbVazia, cbNaoConfirmada, cbUltrassonografia, cbToque, cbObservacao;
	
	@FXML
	public void initialize() {
		
		lblCobertura.setText(getObject().toString());
		inputData.valueProperty().bindBidirectional(getObject().dataConfirmacaoPrenhezProperty());
		if ( getObject().getDataConfirmacaoPrenhez() == null ){
			inputData.setValue(DateUtil.asLocalDate(getObject().getData()).plusDays(30));	
		}
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		btnSalvar.setDisable(getObject().getParto() != null);
		btnRemover.setDisable(getObject().getParto() != null);
		
		//checkboxes situação
		cbPrenha.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if ( isNowSelected ) {
				getObject().setSituacaoConfirmacaoPrenhez(SituacaoCobertura.PRENHA);
				cbVazia.setSelected(false);
				cbNaoConfirmada.setSelected(false);
			}
		});
		
		cbVazia.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if ( isNowSelected ) {
				getObject().setSituacaoConfirmacaoPrenhez(SituacaoCobertura.VAZIA);
				cbPrenha.setSelected(false);
				cbNaoConfirmada.setSelected(false);
			}
		});
		
		cbNaoConfirmada.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if ( isNowSelected ) {
				getObject().setSituacaoConfirmacaoPrenhez(SituacaoCobertura.NAO_CONFIRMADA);
				cbPrenha.setSelected(false);
				cbVazia.setSelected(false);
			}
		});
		

		//checkboxes método
		cbUltrassonografia.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if ( isNowSelected ) {
				getObject().setMetodoConfirmacaoPrenhez(MetodoConfirmacaoPrenhez.ULTRASSONOGRAFIA);
				cbToque.setSelected(false);
				cbObservacao.setSelected(false);
			}
		});
		
		cbToque.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if ( isNowSelected ) {
				getObject().setMetodoConfirmacaoPrenhez(MetodoConfirmacaoPrenhez.TOQUE);
				cbUltrassonografia.setSelected(false);
				cbObservacao.setSelected(false);
			}
		});
		
		cbObservacao.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if ( isNowSelected ) {
				getObject().setMetodoConfirmacaoPrenhez(MetodoConfirmacaoPrenhez.OBSERVACAO);
				cbToque.setSelected(false);
				cbUltrassonografia.setSelected(false);
			}
		});
		
		cbPrenha.setSelected(getObject().getSituacaoConfirmacaoPrenhez().equals(SituacaoCobertura.PRENHA) ? true : false);
		cbVazia.setSelected(getObject().getSituacaoConfirmacaoPrenhez().equals(SituacaoCobertura.VAZIA) ? true : false);
		cbNaoConfirmada.setSelected(getObject().getSituacaoConfirmacaoPrenhez().equals(SituacaoCobertura.NAO_CONFIRMADA) ? true : false);
		
		cbUltrassonografia.setSelected(getObject().getMetodoConfirmacaoPrenhez().equals(MetodoConfirmacaoPrenhez.ULTRASSONOGRAFIA) ? true : false);
		cbToque.setSelected(getObject().getMetodoConfirmacaoPrenhez().equals(MetodoConfirmacaoPrenhez.TOQUE) ? true : false);
		cbObservacao.setSelected(getObject().getMetodoConfirmacaoPrenhez().equals(MetodoConfirmacaoPrenhez.OBSERVACAO) ? true : false);
		
	}
	
	@FXML
	private void handleDesfazerConfirmacao(){
		CustomAlert.confirmarExclusao("Desfazer Confirmação de Prenhez", "Tem certeza que deseja desfazer a confirmação de prenhez?");
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
		return "Confirmação Prenhez";
	}
	
	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}

}
