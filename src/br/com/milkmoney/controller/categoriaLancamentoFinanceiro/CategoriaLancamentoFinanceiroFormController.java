package br.com.milkmoney.controller.categoriaLancamentoFinanceiro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.CategoriaLancamentoFinanceiro;
import br.com.milkmoney.service.CategoriaLancamentoFinanceiroService;
import br.com.milkmoney.service.IService;

@Controller
public class CategoriaLancamentoFinanceiroFormController extends AbstractFormController<Integer, CategoriaLancamentoFinanceiro> {

	@FXML private UCTextField inputDescricao;

	private CategoriaLancamentoFinanceiro categoriaNull = new CategoriaLancamentoFinanceiro("");
	private ObservableList<CategoriaLancamentoFinanceiro> data;
	
	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		data = FXCollections.observableArrayList(categoriaNull);
		data.addAll(((CategoriaLancamentoFinanceiroService)service).findAllAsObservableListOrderly());
	}
	
	@Override
	public String getFormName() {
		return "view/categoriaLancamentoFinanceiro/CategoriaLancamentoFinanceiroForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Categoria Lançamento Financeiro";
	}
	
	@Override
	@Resource(name = "categoriaLancamentoFinanceiroService")
	protected void setService(IService<Integer, CategoriaLancamentoFinanceiro> service) {
		super.setService(service);
	}

}
