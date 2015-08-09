package br.com.milkmoney.controller.semen;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.NumberTextField;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.fornecedor.FornecedorReducedOverviewController;
import br.com.milkmoney.controller.touro.TouroReducedOverviewController;
import br.com.milkmoney.model.Fornecedor;
import br.com.milkmoney.model.Semen;
import br.com.milkmoney.model.SimNao;
import br.com.milkmoney.model.Touro;
import br.com.milkmoney.service.IService;

@Controller
public class SemenFormController extends AbstractFormController<Integer, Semen> {

	@FXML private UCTextField inputCodigoPalheta;
	@FXML private UCTextField inputTouro;
	@FXML private DatePicker inputDataCompra;
	@FXML private NumberTextField inputValor;
	@FXML private NumberTextField inputQuantidade;
	@FXML private ComboBox<String> inputSexado;
	@FXML private UCTextField inputFornecedor;
	
	@Autowired private TouroReducedOverviewController touroReducedOverviewController;
	@Autowired private FornecedorReducedOverviewController fornecedorReducedOverviewController;

	@FXML
	public void initialize() {
		
		inputCodigoPalheta.textProperty().bindBidirectional(getObject().codigoPalhetaProperty());
		
		inputDataCompra.valueProperty().bindBidirectional(getObject().dataCompraProperty());
		inputValor.textProperty().bindBidirectional(getObject().valorUnitarioProperty());
		inputQuantidade.textProperty().bindBidirectional(getObject().quantidadeProperty());
		
		inputSexado.setItems(SimNao.getItems());
		inputSexado.valueProperty().bindBidirectional(getObject().sexadoProperty());
		
		if ( getObject() != null && getObject().getFornecedor() != null ){
			inputFornecedor.setText(getObject().getFornecedor().toString());
		}
		
		if ( getObject() != null && getObject().getTouro() != null ){
			inputTouro.setText(getObject().getTouro().toString());
		}
		
		MaskFieldUtil.numeroInteiro(inputQuantidade);
		MaskFieldUtil.decimal(inputValor);
		
	}

	@FXML
	protected void selecionarFornecedor() {
		
		fornecedorReducedOverviewController.setObject(new Fornecedor());
		fornecedorReducedOverviewController.showForm();
		
		if ( fornecedorReducedOverviewController.getObject() != null && fornecedorReducedOverviewController.getObject().getId() > 0 ){
			getObject().setFornecedor(fornecedorReducedOverviewController.getObject());
		}
		
		if ( getObject().getFornecedor() != null ){
			inputFornecedor.textProperty().set(getObject().getFornecedor().toString());	
		}else{
			inputFornecedor.textProperty().set("");
		}
		
	}
	
	@FXML
	protected void selecionarTouro() {
		
		touroReducedOverviewController.setObject(new Touro());
		touroReducedOverviewController.showForm();
		
		if ( touroReducedOverviewController.getObject() != null && touroReducedOverviewController.getObject().getId() > 0 ){
			getObject().setTouro(touroReducedOverviewController.getObject());
		}
		
		if ( getObject().getTouro() != null ){
			inputTouro.textProperty().set(getObject().getTouro().toString());	
		}else{
			inputTouro.textProperty().set("");
		}
		
	}
	
	@Override
	public String getFormName() {
		return "view/semen/SemenForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Semen";
	}

	@Override
	@Resource(name = "semenService")
	protected void setService(IService<Integer, Semen> service) {
		super.setService(service);
	}
	
}
