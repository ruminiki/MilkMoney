package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Fornecedor;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class FornecedorController extends AbstractController<Integer, Fornecedor> {

	@FXML private TableColumn<Fornecedor, String> nomeColumn;
	@FXML private TableColumn<Fornecedor, String> telefoneColumn;
	@FXML private TableColumn<Fornecedor, String> emailColumn;
	@FXML private TableColumn<Fornecedor, String> cpfCnpjColumn;
	@FXML private TableColumn<Fornecedor, String> enderecoColumn;
	@FXML private TableColumn<Fornecedor, String> siteColumn;
	
	@FXML private UCTextField inputNome;
	@FXML private UCTextField inputEmail;
	@FXML private UCTextField inputTelefone;
	@FXML private UCTextField inputCpfCnpj;
	@FXML private UCTextField inputEndereco;
	@FXML private UCTextField inputSite;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			nomeColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("nome"));
			emailColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("email"));
			telefoneColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("telefone"));
			cpfCnpjColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("cpfCnpf"));
			enderecoColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("endereco"));
			siteColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("site"));
			
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputNome.textProperty().bindBidirectional(getObject().nomeProperty());
			inputTelefone.textProperty().bindBidirectional(getObject().telefoneProperty());
			inputEmail.textProperty().bindBidirectional(getObject().emailProperty());
			inputCpfCnpj.textProperty().bindBidirectional(getObject().cpfCnpfProperty());
			inputEndereco.textProperty().bindBidirectional(getObject().enderecoProperty());
			inputSite.textProperty().bindBidirectional(getObject().siteProperty());
			
			MaskFieldUtil.cpfCnpj(inputCpfCnpj);
		}
		
	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/fornecedor/FornecedorForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Fornecedor";
	}
	
	@Override
	protected Fornecedor getObject() {
		return (Fornecedor)super.object;
	}

	@Override
	@Resource(name = "fornecedorService")
	protected void setService(IService<Integer, Fornecedor> service) {
		super.setService(service);
	}

}
