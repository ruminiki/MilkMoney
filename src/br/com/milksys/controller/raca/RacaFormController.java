package br.com.milksys.controller.raca;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.Raca;
import br.com.milksys.service.IService;

@Controller
public class RacaFormController extends AbstractFormController<Integer, Raca> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	protected String getFormName() {
		return "view/raca/RacaForm.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Raça";
	}
	
	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}

}
