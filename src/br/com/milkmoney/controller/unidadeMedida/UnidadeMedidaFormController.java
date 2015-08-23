package br.com.milkmoney.controller.unidadeMedida;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.UnidadeMedida;
import br.com.milkmoney.service.IService;

@Controller
public class UnidadeMedidaFormController extends AbstractFormController<Integer, UnidadeMedida> {

	@FXML private UCTextField inputDescricao, inputSigla;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputSigla.textProperty().bindBidirectional(getObject().siglaProperty());
	}

	@Override
	public String getFormName() {
		return "view/unidadeMedida/UnidadeMedidaForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Unidade de Medida";
	}
	
	@Override
	@Resource(name = "unidadeMedidaService")
	protected void setService(IService<Integer, UnidadeMedida> service) {
		super.setService(service);
	}

}
