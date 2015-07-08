package br.com.milksys.controller.motivoVendaAnimal;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.model.MotivoVendaAnimal;
import br.com.milksys.service.IService;

@Controller
public class MotivoVendaAnimalFormController extends AbstractFormController<Integer, MotivoVendaAnimal> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	protected String getFormName() {
		return "view/motivoVendaAnimal/MotivoVendaAnimalForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Motivo Venda Animal";
	}
	
	@Override
	@Resource(name = "motivoVendaAnimalService")
	protected void setService(IService<Integer, MotivoVendaAnimal> service) {
		super.setService(service);
	}

}
