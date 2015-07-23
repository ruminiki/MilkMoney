package br.com.milkmoney.controller.finalidadeLote;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.FinalidadeLote;
import br.com.milkmoney.service.IService;

@Controller
public class FinalidadeLoteFormController extends AbstractFormController<Integer, FinalidadeLote> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	protected String getFormName() {
		return "view/finalidadeLote/FinalidadeLoteForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Finalidade Lote";
	}
	
	@Override
	@Resource(name = "finalidadeLoteService")
	protected void setService(IService<Integer, FinalidadeLote> service) {
		super.setService(service);
	}

}
