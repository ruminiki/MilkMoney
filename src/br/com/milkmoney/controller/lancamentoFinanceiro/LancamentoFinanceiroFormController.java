package br.com.milkmoney.controller.lancamentoFinanceiro;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.categoriaLancamentoFinanceiro.CategoriaLancamentoFinanceiroReducedOverviewController;
import br.com.milkmoney.controller.centroCusto.CentroCustoReducedOverviewController;
import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.model.TipoLancamentoFinanceiro;
import br.com.milkmoney.service.IService;

@Controller
public class LancamentoFinanceiroFormController extends AbstractFormController<Integer, LancamentoFinanceiro> {

	@FXML private DatePicker inputDataEmissao, inputDataVencimento, inputDataPagamento;
	@FXML private UCTextField inputCategoria, inputCentroCusto, inputValor, inputJuros, inputMulta, inputDescricao, inputObservacao;
	@FXML private ComboBox<String> inputTipoLancamento;
	
	@Autowired private CentroCustoReducedOverviewController centroCustoReducedOverviewController;
	@Autowired private CategoriaLancamentoFinanceiroReducedOverviewController categoriaLancamentoFinanceiroReducedOverviewController;
	
	
	@FXML
	public void initialize() {
	
		inputDataEmissao.valueProperty().bindBidirectional(getObject().dataEmissaoProperty());
		inputDataVencimento.valueProperty().bindBidirectional(getObject().dataVencimentoProperty());
		inputDataPagamento.valueProperty().bindBidirectional(getObject().dataPagamentoProperty());
		inputValor.textProperty().bindBidirectional(getObject().valorProperty());
		inputJuros.textProperty().bindBidirectional(getObject().jurosProperty());
		inputMulta.textProperty().bindBidirectional(getObject().multaProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputTipoLancamento.getItems().addAll(TipoLancamentoFinanceiro.getItems());
		inputTipoLancamento.valueProperty().bindBidirectional(getObject().tipoLancamentoProperty());
		
		MaskFieldUtil.decimal(inputValor);
		MaskFieldUtil.decimal(inputJuros);
		MaskFieldUtil.decimal(inputMulta);
		
		if ( getObject().getCategoria() != null ){
			inputCategoria.setText(getObject().getCategoria().toString());
		}
		
		if ( getObject().getCentroCusto() != null ){
			inputCentroCusto.setText(getObject().getCentroCusto().toString());
		}
		
	}
	
	
	@FXML
	private void handleSelecionarCategoriaLancamento() {
		
		categoriaLancamentoFinanceiroReducedOverviewController.showForm();
		getObject().setCategoria(categoriaLancamentoFinanceiroReducedOverviewController.getObject());
		
		if ( getObject().getCategoria() != null ){
			inputCategoria.textProperty().set(getObject().getCategoria().toString());	
		}else{
			inputCategoria.textProperty().set("");
		}
		
	}
	
	
	@FXML
	private void handleSelecionarCentroCusto() {
		
		centroCustoReducedOverviewController.showForm();
		getObject().setCentroCusto(centroCustoReducedOverviewController.getObject());
		
		if ( getObject().getCentroCusto() != null ){
			inputCentroCusto.textProperty().set(getObject().getCentroCusto().toString());	
		}else{
			inputCentroCusto.textProperty().set("");
		}
		
	}
	
	@Override
	public String getFormName() {
		return "view/lancamentoFinanceiro/LancamentoFinanceiroForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Lan�amento Financeiro";
	}

	@Override
	@Resource(name = "lancamentoFinanceiroService")
	protected void setService(IService<Integer, LancamentoFinanceiro> service) {
		super.setService(service);
	}
	
}