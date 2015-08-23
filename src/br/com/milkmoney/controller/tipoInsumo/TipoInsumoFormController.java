package br.com.milkmoney.controller.tipoInsumo;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.TipoInsumo;
import br.com.milkmoney.service.IService;

@Controller
public class TipoInsumoFormController extends AbstractFormController<Integer, TipoInsumo> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	public String getFormName() {
		return "view/tipoInsumo/TipoInsumoForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Tipo de Insumo";
	}
	
	@Override
	@Resource(name = "tipoInsumoService")
	protected void setService(IService<Integer, TipoInsumo> service) {
		super.setService(service);
	}

}
