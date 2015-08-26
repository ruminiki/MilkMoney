package br.com.milkmoney.controller.categoriaLancamentoFinanceiro;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;
import br.com.milkmoney.service.IService;

@Controller
public class CategoriaLancamentoFinanceiroFormController extends AbstractFormController<Integer, CategoriaLancamentoFinanceiro> {

	@FXML private UCTextField inputDescricao;
	@FXML private ComboBox<CategoriaLancamentoFinanceiro> inputCategoriaLancamentoFinanceiroSuperiora;

	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputCategoriaLancamentoFinanceiroSuperiora.setItems(service.findAllAsObservableList());
		inputCategoriaLancamentoFinanceiroSuperiora.valueProperty().bindBidirectional(getObject().categoriaSuperioraProperty());
	}

	@Override
	public String getFormName() {
		return "view/categoriaLancamentoFinanceiro/CategoriaLancamentoFinanceiroForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Categoria Despesa";
	}
	
	@Override
	@Resource(name = "categoriaLancamentoFinanceiroService")
	protected void setService(IService<Integer, CategoriaLancamentoFinanceiro> service) {
		super.setService(service);
	}

}
