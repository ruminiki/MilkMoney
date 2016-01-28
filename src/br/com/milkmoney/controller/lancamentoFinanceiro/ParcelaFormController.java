package br.com.milkmoney.controller.lancamentoFinanceiro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.fxml.FXML;
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
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LancamentoFinanceiroService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;

@Controller
public class ParcelaFormController extends AbstractFormController<Integer, LancamentoFinanceiro> {

	@FXML private UCTextField inputNumeroParcelas, inputDiaVencimento, inputValorParcelas, 
							  inputDescricao, inputCategoria, inputCentroCusto, inputIntervalo;
	@FXML private DatePicker inputDataInicio;
	@Autowired private CentroCustoReducedOverviewController centroCustoReducedOverviewController;
	@Autowired private CategoriaLancamentoFinanceiroReducedOverviewController categoriaLancamentoFinanceiroReducedOverviewController;
	private boolean gerouParcelas = false;

	@FXML
	public void initialize() {
		
		inputNumeroParcelas.setText(null);
		inputDiaVencimento.setText(""+DateUtil.asLocalDate(getObject().getDataVencimento()).getDayOfMonth());
		inputValorParcelas.setText(getObject().getValorTotal().toString());
		inputDescricao.setText(getObject().getDescricao());
		inputCategoria.setText(getObject().getCategoria() != null ? getObject().getCategoria().toString() : "");
		inputCentroCusto.setText(getObject().getCentroCusto() != null ? getObject().getCentroCusto().toString() : "");
		gerouParcelas = false;
		
		inputIntervalo.setText(String.valueOf(30));
		inputDataInicio.setValue(LocalDate.now());
		
		MaskFieldUtil.numeroInteiro(inputIntervalo);
		MaskFieldUtil.numeroInteiro(inputNumeroParcelas);
		MaskFieldUtil.numeroInteiro(inputDiaVencimento);
		MaskFieldUtil.decimalWithoutMask(inputValorParcelas);
		
	}

	@FXML
	private void handleGerarParcelas(){
		List<LancamentoFinanceiro> parcelas = new ArrayList<LancamentoFinanceiro>();
		
		int numeroParcelas = Integer.parseInt(inputNumeroParcelas.getText());
		
		String hashParcela = String.valueOf(Calendar.getInstance().getTimeInMillis());
		Calendar dataVencimento = Calendar.getInstance();
		
		if ( inputDataInicio.getValue() != null ){
			dataVencimento.setTime(DateUtil.asDate(inputDataInicio.getValue()));
		}else{
			dataVencimento.setTime(getObject().getDataVencimento() != null ? getObject().getDataVencimento() : new Date());	
		}
		
		if ( !inputDiaVencimento.getText().isEmpty() ){
			if ( dataVencimento.get(Calendar.DAY_OF_MONTH) > Integer.parseInt(inputDiaVencimento.getText()) ){
				dataVencimento.add(Calendar.MONTH, 1);
			}
			dataVencimento.set(Calendar.DAY_OF_MONTH, Integer.parseInt(inputDiaVencimento.getText()));
		}
		
		for ( int parcela = 1; parcela <= numeroParcelas; parcela++){
			LancamentoFinanceiro lancamento = new LancamentoFinanceiro();
			lancamento.setDescricao(inputDescricao.getText() + " (" + parcela + " de " + numeroParcelas + ")");
			lancamento.setValor(NumberFormatUtil.fromString(inputValorParcelas.getText()));
			
			lancamento.setDataVencimento(dataVencimento.getTime());
			lancamento.setCentroCusto(getObject().getCentroCusto());
			lancamento.setCategoria(getObject().getCategoria());
			lancamento.setServico(getObject().getServico());
			
			lancamento.setParcela(hashParcela);
			
			parcelas.add(lancamento);
			
			if ( inputIntervalo.getText().isEmpty() || inputIntervalo.getText().equals("30") ){
				dataVencimento.add(Calendar.MONTH, 1);
			}else{
				dataVencimento.add(Calendar.DAY_OF_MONTH, Integer.parseInt(inputIntervalo.getText()));
			}
			
		}
		
		((LancamentoFinanceiroService)service).salvarParcelas(parcelas);
		
		gerouParcelas = true;
		
		closeForm();
		
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
	
	public boolean gerouParcelas() {
		return gerouParcelas;
	}

	@Override
	public String getFormName() {
		return "view/lancamentoFinanceiro/ParcelasForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Parcelamento";
	}
	
	@Override
	@Resource(name = "lancamentoFinanceiroService")
	protected void setService(IService<Integer, LancamentoFinanceiro> service) {
		super.setService(service);
	}

}
