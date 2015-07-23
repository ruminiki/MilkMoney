package br.com.milkmoney.controller.propriedade;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Propriedade;
import br.com.milkmoney.service.IService;

@Controller
public class PropriedadeFormController extends AbstractFormController<Integer, Propriedade> {

	@FXML private UCTextField inputDescricao, inputEndereco, inputArea;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputEndereco.textProperty().bindBidirectional(getObject().enderecoProperty());
		inputArea.textProperty().bindBidirectional(getObject().areaProperty());
	}

	@Override
	protected String getFormName() {
		return "view/propriedade/PropriedadeForm.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Propriedade";
	}
	
	@Override
	@Resource(name = "propriedadeService")
	protected void setService(IService<Integer, Propriedade> service) {
		super.setService(service);
	}

}
