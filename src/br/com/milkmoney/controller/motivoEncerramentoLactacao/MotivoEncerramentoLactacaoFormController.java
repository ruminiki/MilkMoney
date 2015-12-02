package br.com.milkmoney.controller.motivoEncerramentoLactacao;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.MotivoEncerramentoLactacao;
import br.com.milkmoney.service.IService;

@Controller
public class MotivoEncerramentoLactacaoFormController extends AbstractFormController<Integer, MotivoEncerramentoLactacao> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	public String getFormName() {
		return "view/motivoEncerramentoLactacao/MotivoEncerramentoLactacaoForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Motivo Encerramento Lactação";
	}
	
	@Override
	@Resource(name = "motivoEncerramentoLactacaoService")
	protected void setService(IService<Integer, MotivoEncerramentoLactacao> service) {
		super.setService(service);
	}

}
