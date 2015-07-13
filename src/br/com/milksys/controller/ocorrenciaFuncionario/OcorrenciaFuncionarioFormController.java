package br.com.milksys.controller.ocorrenciaFuncionario;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.motivoOcorrenciaFuncionario.MotivoOcorrenciaFuncionarioReducedOverviewController;
import br.com.milksys.model.Funcionario;
import br.com.milksys.model.MotivoOcorrenciaFuncionario;
import br.com.milksys.model.OcorrenciaFuncionario;

@Controller
public class OcorrenciaFuncionarioFormController extends AbstractFormController<Integer, OcorrenciaFuncionario> {

	@FXML private DatePicker inputData;
	@FXML private UCTextField inputDescricao, inputMotivo, inputFuncionario;
	@FXML private UCTextField inputJustificativa;
	
	@Autowired private MotivoOcorrenciaFuncionarioReducedOverviewController motivoOcorrenciaFuncionarioReducedOverviewController;
	
	private Funcionario selectedFuncionario;
	
	@FXML
	public void initialize() {
		
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputFuncionario.setText(selectedFuncionario.getNome());
		getObject().setFuncionario(selectedFuncionario);
		
		if ( getObject().getMotivoOcorrenciaFuncionario() != null ){
			inputMotivo.setText(getObject().getMotivoOcorrenciaFuncionario().toString());
		}
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputJustificativa.textProperty().bindBidirectional(getObject().justificativaProperty());
	
	}
	
	@FXML
	protected void handleSelecionarMotivoOcorrencia() {
		motivoOcorrenciaFuncionarioReducedOverviewController.setObject(new MotivoOcorrenciaFuncionario());
		motivoOcorrenciaFuncionarioReducedOverviewController.showForm();
		if ( motivoOcorrenciaFuncionarioReducedOverviewController.getObject() != null && motivoOcorrenciaFuncionarioReducedOverviewController.getObject().getId() > 0 ){
			getObject().setMotivoOcorrenciaFuncionario(motivoOcorrenciaFuncionarioReducedOverviewController.getObject());
		}
		
		if ( getObject().getMotivoOcorrenciaFuncionario() != null ) {
			inputMotivo.setText(getObject().getMotivoOcorrenciaFuncionario().toString());
		}else{
			inputMotivo.setText(null);
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
