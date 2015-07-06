package br.com.milksys.controller.semen;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.NumberTextField;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.fornecedor.FornecedorFormController;
import br.com.milksys.model.Fornecedor;
import br.com.milksys.model.Semen;
import br.com.milksys.model.State;
import br.com.milksys.service.FornecedorService;
import br.com.milksys.service.IService;

@Controller
public class SemenFormController extends AbstractFormController<Integer, Semen> {

	@FXML private UCTextField inputDescricao;
	@FXML private UCTextField inputTouro;
	@FXML private DatePicker inputDataCompra;
	@FXML private NumberTextField inputValor;
	@FXML private NumberTextField inputQuantidade;
	@FXML private UCTextField inputLote;
	@FXML private ComboBox<Fornecedor> inputFornecedor;
	
	@Autowired private FornecedorService fornecedorService;
	@Autowired private FornecedorFormController fornecedorFormController;

	@FXML
	public void initialize() {
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputTouro.textProperty().bindBidirectional(getObject().touroProperty());
		inputDataCompra.valueProperty().bindBidirectional(getObject().dataCompraProperty());
		inputValor.textProperty().bindBidirectional(getObject().valorUnitarioProperty());
		inputQuantidade.textProperty().bindBidirectional(getObject().quantidadeProperty());
		inputLote.textProperty().bindBidirectional(getObject().loteProperty());
		
		inputFornecedor.setItems(fornecedorService.findAllAsObservableList());
		inputFornecedor.valueProperty().bindBidirectional(getObject().fornecedorProperty());
		
		MaskFieldUtil.numeroInteiro(inputQuantidade);
		MaskFieldUtil.moeda(inputValor);
		
	}

	@FXML
	protected void openFormFornecedorToInsertAndSelect() {
		fornecedorFormController.setState(State.INSERT_TO_SELECT);
		fornecedorFormController.setObject(new Fornecedor());
		fornecedorFormController.showForm();
		if ( fornecedorFormController.getObject() != null ){
			inputFornecedor.getItems().add(fornecedorFormController.getObject());
			inputFornecedor.getSelectionModel().select(fornecedorFormController.getObject());
		}
	}
	
	@Override
	protected String getFormName() {
		return "view/semen/SemenForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Semen";
	}

	@Override
	@Resource(name = "semenService")
	protected void setService(IService<Integer, Semen> service) {
		super.setService(service);
	}
	
}
