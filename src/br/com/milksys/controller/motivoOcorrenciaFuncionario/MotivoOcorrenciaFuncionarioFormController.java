package br.com.milksys.controller.motivoOcorrenciaFuncionario;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.MotivoOcorrenciaFuncionario;
import br.com.milksys.service.IService;

@Controller
public class MotivoOcorrenciaFuncionarioFormController extends AbstractFormController<Integer, MotivoOcorrenciaFuncionario> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	protected String getFormName() {
		return "view/motivoOcorrenciaFuncionario/MotivoOcorrenciaFuncionarioForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Motivo Ocorrência Funcionário";
	}
	
	@Override
	@Resource(name = "motivoOcorrenciaFuncionarioService")
	protected void setService(IService<Integer, MotivoOcorrenciaFuncionario> service) {
		super.setService(service);
	}

}
