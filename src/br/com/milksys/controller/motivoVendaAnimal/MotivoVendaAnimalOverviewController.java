package br.com.milksys.controller.motivoVendaAnimal;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.MotivoVendaAnimal;
import br.com.milksys.service.IService;

@Controller
public class MotivoVendaAnimalOverviewController extends AbstractOverviewController<Integer, MotivoVendaAnimal> {

	@FXML private TableColumn<MotivoVendaAnimal, String> idColumn;
	@FXML private TableColumn<MotivoVendaAnimal, String> descricaoColumn;

	@FXML
	public void initialize() {
		
		idColumn.setCellValueFactory(new PropertyValueFactory<MotivoVendaAnimal,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<MotivoVendaAnimal,String>("descricao"));
		super.initialize((MotivoVendaAnimalFormController) MainApp.getBean(MotivoVendaAnimalFormController.class));
		
	}

	@Override
	protected String getFormTitle() {
		return "Motivo Venda Animal";
	}
	
	@Override
	protected String getFormName() {
		return "view/motivoVendaAnimal/MotivoVendaAnimalOverview.fxml";
	}

	@Override
	@Resource(name = "motivoVendaAnimalService")
	protected void setService(IService<Integer, MotivoVendaAnimal> service) {
		super.setService(service);
	}

}
