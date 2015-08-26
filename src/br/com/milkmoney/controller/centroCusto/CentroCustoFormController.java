package br.com.milkmoney.controller.centroCusto;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.CentroCusto;
import br.com.milkmoney.service.IService;

@Controller
public class CentroCustoFormController extends AbstractFormController<Integer, CentroCusto> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	public String getFormName() {
		return "view/centroCusto/CentroCustoForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Centro de Custo";
	}
	
	@Override
	@Resource(name = "centroCustoService")
	protected void setService(IService<Integer, CentroCusto> service) {
		super.setService(service);
	}

}
