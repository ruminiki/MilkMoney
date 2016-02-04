package br.com.milkmoney.controller.fornecedor;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.TableCellOptionSelectFactory;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.Fornecedor;
import br.com.milkmoney.service.IService;

@Controller
public class FornecedorReducedOverviewController extends AbstractReducedOverviewController<Integer, Fornecedor> {

	@FXML private TableColumn<Fornecedor, String> nomeColumn;
	@FXML private TableColumn<Fornecedor, String> telefonePrincipalColumn;
	@FXML private TableColumn<Fornecedor, String> telefoneSecundarioColumn;
	@FXML private TableColumn<Fornecedor, String> emailColumn;
	@FXML private TableColumn<Fornecedor, String> opcoesColumn;

	@FXML
	public void initialize() {

		nomeColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("nome"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("email"));
		telefonePrincipalColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("telefonePrincipal"));
		telefoneSecundarioColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("telefoneSecundario"));
		opcoesColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("id"));
		opcoesColumn.setCellFactory(new TableCellOptionSelectFactory<Fornecedor,String>(selecionar));

		super.initialize((FornecedorFormController) MainApp.getBean(FornecedorFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/fornecedor/FornecedorReducedOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Fornecedor";
	}
	
	@Override
	public Fornecedor getObject() {
		return (Fornecedor) super.object;
	}

	@Override
	@Resource(name = "fornecedorService")
	protected void setService(IService<Integer, Fornecedor> service) {
		super.setService(service);
	}
	
}
