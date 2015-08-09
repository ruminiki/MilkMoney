package br.com.milkmoney.controller.raca;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.service.IService;

@Controller
public class RacaFormController extends AbstractFormController<Integer, Raca> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	public String getFormName() {
		return "view/raca/RacaForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Raça";
	}
	
	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}

}
