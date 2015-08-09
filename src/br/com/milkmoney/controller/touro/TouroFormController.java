package br.com.milkmoney.controller.touro;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Touro;
import br.com.milkmoney.service.IService;

@Controller
public class TouroFormController extends AbstractFormController<Integer, Touro> {

	@FXML private UCTextField inputNome;
	@FXML private UCTextField inputCodigo;

	@FXML
	public void initialize() {
		inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
		inputCodigo.textProperty().bindBidirectional(getObject().codigoProperty());
	}

	@Override
	public String getFormName() {
		return "view/touro/TouroForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Touro";
	}
	
	@Override
	@Resource(name = "touroService")
	protected void setService(IService<Integer, Touro> service) {
		super.setService(service);
	}

}
