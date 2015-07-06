package br.com.milksys.controller.fornecedor;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Fornecedor;
import br.com.milksys.service.IService;

@Controller
public class FornecedorOverviewController extends AbstractOverviewController<Integer, Fornecedor> {

	@FXML private TableColumn<Fornecedor, String> nomeColumn;
	@FXML private TableColumn<Fornecedor, String> telefonePrincipalColumn;
	@FXML private TableColumn<Fornecedor, String> telefoneSecundarioColumn;
	@FXML private TableColumn<Fornecedor, String> emailColumn;
	@FXML private TableColumn<Fornecedor, String> cpfCnpjColumn;
	@FXML private TableColumn<Fornecedor, String> enderecoColumn;
	@FXML private TableColumn<Fornecedor, String> siteColumn;

	@FXML
	public void initialize() {

		nomeColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("nome"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("email"));
		telefonePrincipalColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("telefonePrincipal"));
		telefoneSecundarioColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("telefoneSecundario"));
		cpfCnpjColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("cpfCnpf"));
		enderecoColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("endereco"));
		siteColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("site"));
		
		super.initialize((FornecedorFormController) MainApp.getBean(FornecedorFormController.class));
		
	}

	@Override
	protected String getFormTitle() {
		return "Fornecedor";
	}
	
	@Override
	protected String getFormName() {
		return "view/fornecedor/FornecedorOverview.fxml";
	}

	@Override
	@Resource(name = "fornecedorService")
	protected void setService(IService<Integer, Fornecedor> service) {
		super.setService(service);
	}

}
