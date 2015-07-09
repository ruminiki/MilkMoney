package br.com.milksys.controller.causaMorteAnimal;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.CausaMorteAnimal;
import br.com.milksys.service.IService;

@Controller
public class CausaMorteAnimalFormController extends AbstractFormController<Integer, CausaMorteAnimal> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	protected String getFormName() {
		return "view/causaMorteAnimal/CausaMorteAnimalForm.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Causa Morte Animal";
	}
	
	@Override
	@Resource(name = "causaMorteAnimalService")
	protected void setService(IService<Integer, CausaMorteAnimal> service) {
		super.setService(service);
	}

}
