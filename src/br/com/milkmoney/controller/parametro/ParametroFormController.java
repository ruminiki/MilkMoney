package br.com.milkmoney.controller.parametro;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Parametro;
import br.com.milkmoney.service.IService;

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
	public String getFormName() {
		return "view/parametro/ParametroForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Parâmetro";
	}
	
	@Override
	@Resource(name = "parametroService")
	protected void setService(IService<Integer, Parametro> service) {
		super.setService(service);
	}

}
