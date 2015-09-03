package br.com.milkmoney.controller.complicacaoParto;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.ComplicacaoParto;
import br.com.milkmoney.service.IService;

@Controller
public class ComplicacaoPartoFormController extends AbstractFormController<Integer, ComplicacaoParto> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	public String getFormName() {
		return "view/complicacaoParto/ComplicacaoPartoForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Complicação Parto";
	}
	
	@Override
	@Resource(name = "complicacaoPartoService")
	protected void setService(IService<Integer, ComplicacaoParto> service) {
		super.setService(service);
	}

}
