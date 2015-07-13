package br.com.milksys.controller.prestadorServico;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.PrestadorServico;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class PrestadorServicoReducedOverviewController extends AbstractReducedOverviewController<Integer, PrestadorServico> {

	@FXML private TableColumn<PrestadorServico, String> nomeColumn;
	@FXML private TableColumn<PrestadorServico, String> telefonePrincipalColumn;
	@FXML private TableColumn<PrestadorServico, String> telefoneSecundarioColumn;
	@FXML private TableColumn<PrestadorServico, String> emailColumn;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			nomeColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("nome"));
			telefonePrincipalColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("telefonePrincipal"));
			telefoneSecundarioColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("telefoneSecundario"));
			emailColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("email"));
			super.initialize((PrestadorServicoFormController)MainApp.getBean(PrestadorServicoFormController.class));
		}
		
	}

	@Override
	public String getFormName() {
		return "view/prestadorServico/PrestadorServicoReducedOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Prestador de Serviço";
	}
	
	@Override
	public PrestadorServico getObject() {
		return (PrestadorServico) super.object;
	}

	@Override
	@Resource(name = "prestadorServicoService")
	protected void setService(IService<Integer, PrestadorServico> service) {
		super.setService(service);
	}
	
}
