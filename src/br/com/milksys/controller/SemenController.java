package br.com.milksys.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.NumberTextField;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Fornecedor;
import br.com.milksys.model.Semen;
import br.com.milksys.model.State;
import br.com.milksys.service.FornecedorService;
import br.com.milksys.service.IService;

@Controller
public class SemenController extends AbstractController<Integer, Semen> {

	@FXML private TableColumn<Semen, String> descricaoColumn;
	@FXML private TableColumn<Semen, String> touroColumn;
	@FXML private TableColumn<Semen, LocalDate> dataCompraColumn;
	@FXML private TableColumn<Semen, String> valorUnitarioColumn;
	@FXML private TableColumn<Semen, String> valorTotalColumn;
	@FXML private TableColumn<Semen, String> quantidadeColumn;
	@FXML private TableColumn<Semen, String> loteColumn;
	
	@FXML private UCTextField inputDescricao;
	@FXML private UCTextField inputTouro;
	@FXML private DatePicker inputDataCompra;
	@FXML private NumberTextField inputValor;
	@FXML private NumberTextField inputQuantidade;
	@FXML private UCTextField inputLote;
	@FXML private ComboBox<Fornecedor> inputFornecedor;
	
	@Autowired private FornecedorService fornecedorService;
	@Autowired private FornecedorController fornecedorController;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("descricao"));
			touroColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("touro"));
			dataCompraColumn.setCellFactory(new TableCellDateFactory<Semen, LocalDate>("dataCompra"));
			valorUnitarioColumn.setCellValueFactory(new PropertyDecimalValueFactory<Semen,String>("valorUnitario"));
			valorTotalColumn.setCellValueFactory(new PropertyDecimalValueFactory<Semen,String>("valorTotal"));
			quantidadeColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("quantidade"));
			loteColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("lote"));
			
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
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
	protected Semen getObject() {
		return (Semen) super.object;
	}

	@Override
	@Resource(name = "semenService")
	protected void setService(IService<Integer, Semen> service) {
		super.setService(service);
	}
	
	@FXML
	protected void openFormFornecedorToInsertAndSelect() {
		fornecedorController.state = State.INSERT_TO_SELECT;
		fornecedorController.object = new Fornecedor();
		fornecedorController.showForm(null);
		if ( fornecedorController.getObject() != null ){
			inputFornecedor.getItems().add(fornecedorController.getObject());
			inputFornecedor.getSelectionModel().select(fornecedorController.getObject());
		}
	}


}
