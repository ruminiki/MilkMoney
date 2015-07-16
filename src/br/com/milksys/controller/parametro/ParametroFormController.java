package br.com.milksys.controller.parametro;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.Parametro;
import br.com.milksys.service.IService;

@Controller
public class ParametroFormController extends AbstractFormController<Integer, Parametro> {

	@FXML private UCTextField inputDescricao;
	@FXML private UCTextField inputSigla;
	@FXML private UCTextField inputValor;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputSigla.textProperty().bindBidirectional(getObject().siglaProperty());
		inputValor.textProperty().bindBidirectional(getObject().valorProperty());
	}

	@Override
	protected String getFormName() {
		return "view/parametro/ParametroForm.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Par�metro";
	}
	
	@Override
	@Resource(name = "parametroService")
	protected void setService(IService<Integer, Parametro> service) {
		super.setService(service);
	}

}
