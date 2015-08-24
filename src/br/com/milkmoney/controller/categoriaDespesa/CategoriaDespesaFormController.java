package br.com.milkmoney.controller.categoriaDespesa;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.CategoriaDespesa;
import br.com.milkmoney.service.IService;

@Controller
public class CategoriaDespesaFormController extends AbstractFormController<Integer, CategoriaDespesa> {

	@FXML private UCTextField inputDescricao;
	@FXML private ComboBox<CategoriaDespesa> inputCategoriaDespesaSuperiora;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputCategoriaDespesaSuperiora.setItems(service.findAllAsObservableList());
		inputCategoriaDespesaSuperiora.valueProperty().bindBidirectional(getObject().categoriaSuperioraProperty());
	}

	@Override
	public String getFormName() {
		return "view/categoriaDespesa/CategoriaDespesaForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Categoria Despesa";
	}
	
	@Override
	@Resource(name = "categoriaDespesaService")
	protected void setService(IService<Integer, CategoriaDespesa> service) {
		super.setService(service);
	}

}
