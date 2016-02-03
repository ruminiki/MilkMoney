package br.com.milkmoney.controller.lancamentoFinanceiro;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

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
	@FXML private ToggleButton tbReceita, tbDespesa;
	@FXML private Button btnParcelar;
	@Autowired private CentroCustoReducedOverviewController centroCustoReducedOverviewController;
	@Autowired private CategoriaLancamentoFinanceiroReducedOverviewController categoriaLancamentoFinanceiroReducedOverviewController;
	@Autowired private ParcelasFormController parcelasFormController;
	@Autowired private ParcelasOverviewController parcelasOverviewController;
	
	private boolean gerouParcelas = false;
	private ToggleGroup groupTipo = new ToggleGroup();
	
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
		
		MaskFieldUtil.decimal(inputValor);
		MaskFieldUtil.decimal(inputJuros);
		MaskFieldUtil.decimal(inputMulta);
		
		if ( getObject().getCategoria() != null ){
			inputCategoria.setText(getObject().getCategoria().toString());
		}
		
		if ( getObject().getCentroCusto() != null ){
			inputCentroCusto.setText(getObject().getCentroCusto().toString());
		}
		
		groupTipo.getToggles().clear();
		groupTipo.getToggles().addAll(tbReceita, tbDespesa);
		
		tbDespesa.setSelected(getObject().getTipoLancamento().equals(TipoLancamentoFinanceiro.DESPESA));
		tbReceita.setSelected(getObject().getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA));
		this.setStyleButtonReceitaDespesa();
		
		tbDespesa.selectedProperty().addListener((observable, oldValue, newValue) -> {
			tbReceita.setSelected(!tbDespesa.isSelected());
			setStyleButtonReceitaDespesa();
		});
		
		tbReceita.selectedProperty().addListener((observable, oldValue, newValue) -> {
			tbDespesa.setSelected(!tbReceita.isSelected());
			setStyleButtonReceitaDespesa();
		});
		
		if ( getObject().getId() > 0 && getObject().getParcela() != null && !getObject().getParcela().isEmpty() ){
			btnParcelar.setText("Visualizar Parcelas");
			btnParcelar.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					parcelasOverviewController.setParcela(getObject().getParcela());
					parcelasOverviewController.showForm();
				}
			});
		}else{
			btnParcelar.setDisable(getObject().getId() > 0);
		}
		
	}
	
	private void setStyleButtonReceitaDespesa(){
		if ( tbDespesa.isSelected() ){
			tbDespesa.setStyle("-fx-background-color: #FF3333");
			tbReceita.setStyle(null);
		}
		
		if ( tbReceita.isSelected() ){
			tbReceita.setStyle("-fx-background-color: #00CC66");
			tbDespesa.setStyle(null);
		}
	}
	
	@Override
	protected void beforeSave() {
		super.beforeSave();
		
		if ( tbDespesa.isSelected() ){
			getObject().setTipoLancamento(TipoLancamentoFinanceiro.DESPESA);
		}else{
			getObject().setTipoLancamento(TipoLancamentoFinanceiro.RECEITA);
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
	
	@FXML
	private void handleParcelar() {
		
		parcelasFormController.setObject(getObject());
		parcelasFormController.showForm();
		gerouParcelas = parcelasFormController.gerouParcelas();
		
		if ( gerouParcelas ){
			closeForm();
		}
	}
	
	public boolean gerouParcelas() {
		return gerouParcelas;
	}

	@Override
	public String getFormName() {
		return "view/lancamentoFinanceiro/LancamentoFinanceiroForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Lançamento Financeiro";
	}

	@Override
	@Resource(name = "lancamentoFinanceiroService")
	protected void setService(IService<Integer, LancamentoFinanceiro> service) {
		super.setService(service);
	}
	
}
