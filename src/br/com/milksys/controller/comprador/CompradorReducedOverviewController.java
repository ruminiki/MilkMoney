package br.com.milksys.controller.comprador;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.Comprador;
import br.com.milksys.service.IService;

@Controller
public class CompradorReducedOverviewController extends AbstractReducedOverviewController<Integer, Comprador> {

	@FXML private TableColumn<Comprador, String> nomeColumn;
	@FXML private TableColumn<Comprador, String> telefonePrincipalColumn;
	@FXML private TableColumn<Comprador, String> telefoneSecundarioColumn;
	@FXML private TableColumn<Comprador, String> emailColumn;
	@FXML private TableColumn<Comprador, String> cpfCnpjColumn;

	@FXML
	public void initialize() {

		nomeColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("nome"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("email"));
		telefonePrincipalColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("telefonePrincipal"));
		telefoneSecundarioColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("telefoneSecundario"));
		cpfCnpjColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("cpfCnpf"));
		
		super.initialize((CompradorFormController) MainApp.getBean(CompradorFormController.class));
		
	}
	
	@Override
	public String getFormName() {
		return "view/comprador/CompradorReducedOverview.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Comprador";
	}
	
	@Override
	@Resource(name = "compradorService")
	protected void setService(IService<Integer, Comprador> service) {
		super.setService(service);
	}

}
