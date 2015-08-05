package br.com.milkmoney.controller.tipoProcedimento;

import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.TipoProcedimento;
import br.com.milkmoney.service.IService;

@Controller
public class TipoProcedimentoFormController extends AbstractFormController<Integer, TipoProcedimento> {

	@FXML private UCTextField inputDescricao;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
	}

	@Override
	protected String getFormName() {
		return "view/tipoProcedimento/TipoProcedimentoForm.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Tipo Procedimento";
	}
	
	@Override
	@Resource(name = "tipoProcedimentoService")
	protected void setService(IService<Integer, TipoProcedimento> service) {
		super.setService(service);
	}

}
