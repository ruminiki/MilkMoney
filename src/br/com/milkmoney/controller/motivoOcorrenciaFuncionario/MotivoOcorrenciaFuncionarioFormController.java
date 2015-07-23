package br.com.milkmoney.controller.motivoOcorrenciaFuncionario;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.MotivoOcorrenciaFuncionario;
import br.com.milkmoney.service.IService;

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
