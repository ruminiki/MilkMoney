package br.com.milksys.controller.fornecedor;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.CustomAlert;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.Fornecedor;
import br.com.milksys.service.IService;

@Controller
public class FornecedorReducedOverviewController extends AbstractReducedOverviewController<Integer, Fornecedor> {

	@FXML private TableColumn<Fornecedor, String> nomeColumn;
	@FXML private TableColumn<Fornecedor, String> telefonePrincipalColumn;
	@FXML private TableColumn<Fornecedor, String> telefoneSecundarioColumn;
	@FXML private TableColumn<Fornecedor, String> emailColumn;

	@FXML
	public void initialize() {

		nomeColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("nome"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("email"));
		telefonePrincipalColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("telefonePrincipal"));
		telefoneSecundarioColumn.setCellValueFactory(new PropertyValueFactory<Fornecedor,String>("telefoneSecundario"));

		super.initialize((FornecedorFormController) MainApp.getBean(FornecedorFormController.class));
		
	}

	@FXML
	private void selecionar(){
		
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			super.closeForm();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um sêmen na tabela.");
		}
		
	}

	@FXML
	private void fechar(){
		this.setObject(null);
		super.closeForm();
	}
	
	@Override
	protected void configureDoubleClickTable(){
		// captura o evento de double click da table
		table.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					selecionar();
				}
			}

		});
	}
	
	@Override
	public String getFormName() {
		return "view/fornecedor/FornecedorReducedOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
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
