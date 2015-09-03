package br.com.milkmoney.controller.categoriaLancamentoFinanceiro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

	private CategoriaLancamentoFinanceiro categoriaNull = new CategoriaLancamentoFinanceiro("");
	private ObservableList<CategoriaLancamentoFinanceiro> data;
	
	@FXML
	public void initialize() {
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		data = FXCollections.observableArrayList(categoriaNull);
		data.addAll(service.findAllAsObservableList());
		inputCategoriaLancamentoFinanceiroSuperiora.setItems(data);
		inputCategoriaLancamentoFinanceiroSuperiora.valueProperty().bindBidirectional(getObject().categoriaSuperioraProperty());
	}
	
	@Override
	protected void beforeSave() {
		super.beforeSave();
		
		if ( getObject().getCategoriaSuperiora() != null && 
			getObject().getCategoriaSuperiora().getId() <= 0 ){
				getObject().setCategoriaSuperiora(null);
		}
		
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
