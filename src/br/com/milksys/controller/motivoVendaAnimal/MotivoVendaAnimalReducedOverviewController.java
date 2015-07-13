package br.com.milksys.controller.motivoVendaAnimal;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.MotivoVendaAnimal;
import br.com.milksys.service.IService;

@Controller
public class MotivoVendaAnimalReducedOverviewController extends AbstractReducedOverviewController<Integer, MotivoVendaAnimal> {

	@FXML private TableColumn<MotivoVendaAnimal, String> idColumn;
	@FXML private TableColumn<MotivoVendaAnimal, String> descricaoColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<MotivoVendaAnimal,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<MotivoVendaAnimal,String>("descricao"));
		super.initialize((MotivoVendaAnimalFormController) MainApp.getBean(MotivoVendaAnimalFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/motivoVendaAnimal/MotivoVendaAnimalReducedOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Motivo Venda Animal";
	}
	
	@Override
	public MotivoVendaAnimal getObject() {
		return (MotivoVendaAnimal) super.object;
	}

	@Override
	@Resource(name = "motivoVendaAnimalService")
	protected void setService(IService<Integer, MotivoVendaAnimal> service) {
		super.setService(service);
	}
	
}
