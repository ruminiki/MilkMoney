package br.com.milkmoney.controller.aborto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Aborto;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class AbortoFormController extends AbstractFormController<Integer, Cobertura> {

	@FXML private DatePicker        inputData;
	@FXML private Label             lblCobertura, lblDuracaoGestacao;
	@FXML private UCTextField       inputObservacao;
	@FXML private Button            btnSalvar, btnRemover;
	@FXML private ChoiceBox<String> inputSexo;
	
	@Autowired CoberturaService coberturaService;
	
	@FXML
	public void initialize() {
		
		lblCobertura.setText(getObject().toString());
		
		if ( getObject().getAborto() == null ){
			getObject().setAborto(new Aborto());
		}
		
		inputData.valueProperty().bindBidirectional(getObject().getAborto().dataProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().getAborto().observacaoProperty());
		inputSexo.setItems(Sexo.getAllItems());
		inputSexo.valueProperty().bindBidirectional(getObject().getAborto().sexoCriaProperty());
		
		btnSalvar.setDisable(getObject().getParto() != null);
		btnRemover.setDisable(getObject().getParto() != null);
		
		lblDuracaoGestacao.setText(ChronoUnit.DAYS.between(DateUtil.asLocalDate(getObject().getData()), LocalDate.now()) + " dias");
		
	}
	
	@FXML
	private void handleDesfazerRegistro(){
		CustomAlert.confirmarExclusao("Desfazer registro de aborto", "Tem certeza que deseja desfazer o registro do aborto?");
		if (CustomAlert.response == ButtonType.OK) {
			coberturaService.desfazerRegistroAborto(getObject());
			closeForm();
		}
	}
	
	@Override
	protected void beforeSave() {
		getObject().setSituacaoCobertura(SituacaoCobertura.ABORTADA);
	}
	
	@Override
	public String getFormName() {
		return "view/aborto/AbortoForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Aborto";
	}
	
	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}

}
