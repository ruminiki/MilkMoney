package br.com.milksys.controller.ocorrenciaFuncionario;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.motivoOcorrenciaFuncionario.MotivoOcorrenciaFuncionarioFormController;
import br.com.milksys.model.Funcionario;
import br.com.milksys.model.MotivoOcorrenciaFuncionario;
import br.com.milksys.model.OcorrenciaFuncionario;
import br.com.milksys.model.State;
import br.com.milksys.service.FuncionarioService;
import br.com.milksys.service.MotivoOcorrenciaFuncionarioService;
import br.com.milksys.service.OcorrenciaFuncionarioService;

@Controller
public class OcorrenciaFuncionarioFormController extends AbstractFormController<Integer, OcorrenciaFuncionario> {

	@FXML private DatePicker inputData;
	@FXML private UCTextField inputDescricao;
	@FXML private UCTextField inputFuncionario;
	@FXML private ComboBox<MotivoOcorrenciaFuncionario> inputMotivo;
	@FXML private UCTextField inputJustificativa;
	
	@FXML private ComboBox<Funcionario> inputFuncionarioComboBox;
	
	@Autowired private FuncionarioService funcionarioService;
	@Autowired private OcorrenciaFuncionarioService ocorrenciaFuncionarioService;
	@Autowired private MotivoOcorrenciaFuncionarioService motivoOcorrenciaFuncionarioService;
	@Autowired private MotivoOcorrenciaFuncionarioFormController motivoOcorrenciaFuncionarioFormController;
	
	private Funcionario selectedFuncionario;
	
	@FXML
	public void initialize() {
		
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputFuncionario.setText(selectedFuncionario.getNome());
		getObject().setFuncionario(selectedFuncionario);
		
		inputMotivo.valueProperty().bindBidirectional(getObject().motivoOcorrenciaFuncionarioProperty());
		inputMotivo.setItems(motivoOcorrenciaFuncionarioService.findAllAsObservableList());
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputJustificativa.textProperty().bindBidirectional(getObject().justificativaProperty());
	
	}
	
	@FXML
	protected void novoMotivoOcorrenciaFuncionario() {
		motivoOcorrenciaFuncionarioFormController.setState(State.INSERT_TO_SELECT);
		motivoOcorrenciaFuncionarioFormController.setObject(new MotivoOcorrenciaFuncionario());
		motivoOcorrenciaFuncionarioFormController.showForm();
		if ( motivoOcorrenciaFuncionarioFormController.getObject() != null ){
			inputMotivo.getItems().add(motivoOcorrenciaFuncionarioFormController.getObject());
			inputMotivo.getSelectionModel().select(motivoOcorrenciaFuncionarioFormController.getObject());
		}
	}

	public String getFormOverviewName() {
		return "view/ocorrenciaFuncionario/OcorrenciaFuncionarioOverview.fxml";
	}
	
	@Override
	protected String getFormName() {
		return "view/ocorrenciaFuncionario/OcorrenciaFuncionarioForm.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Ocorrências Funcionário";
	}

}
