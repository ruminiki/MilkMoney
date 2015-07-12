package br.com.milksys.controller.touro;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.Touro;
import br.com.milksys.service.IService;

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
	protected String getFormName() {
		return "view/touro/TouroForm.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Touro";
	}
	
	@Override
	@Resource(name = "touroService")
	protected void setService(IService<Integer, Touro> service) {
		super.setService(service);
	}

}
