package br.com.milkmoney.controller.prestadorServico;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.PrestadorServico;
import br.com.milkmoney.service.IService;

@Controller
public class PrestadorServicoOverviewController extends AbstractOverviewController<Integer, PrestadorServico> {

	@FXML private TableColumn<PrestadorServico, String> nomeColumn;
	@FXML private TableColumn<PrestadorServico, String> telefonePrincipalColumn;
	@FXML private TableColumn<PrestadorServico, String> telefoneSecundarioColumn;
	@FXML private TableColumn<PrestadorServico, String> emailColumn;
	
	@FXML
	public void initialize() {
		
		nomeColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("nome"));
		telefonePrincipalColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("telefonePrincipal"));
		telefoneSecundarioColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("telefoneSecundario"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("email"));
		super.initialize((PrestadorServicoFormController)MainApp.getBean(PrestadorServicoFormController.class));
		
	}

	@Override
	public String getFormTitle() {
		return "Prestador de Serviço";
	}
	
	@Override
	public String getFormName() {
		return "view/prestadorServico/PrestadorServicoOverview.fxml";
	}
	
	@Override
	@Resource(name = "prestadorServicoService")
	protected void setService(IService<Integer, PrestadorServico> service) {
		super.setService(service);
	}

}
