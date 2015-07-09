package br.com.milksys.controller.causaMorteAnimal;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.CausaMorteAnimal;
import br.com.milksys.service.IService;

@Controller
public class CausaMorteAnimalOverviewController extends AbstractOverviewController<Integer, CausaMorteAnimal> {

	@FXML private TableColumn<CausaMorteAnimal, String> idColumn;
	@FXML private TableColumn<CausaMorteAnimal, String> descricaoColumn;

	@Autowired private CausaMorteAnimalFormController formController;
	
	@FXML
	public void initialize() {
		
		idColumn.setCellValueFactory(new PropertyValueFactory<CausaMorteAnimal,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<CausaMorteAnimal,String>("descricao"));
		super.initialize(formController);
		
	}

	@Override
	protected String getFormTitle() {
		return "Causa Morte Animal";
	}
	
	@Override
	public String getFormName() {
		return "view/causaMorteAnimal/CausaMorteAnimalOverview.fxml";
	}
	
	@Override
	@Resource(name = "causaMorteAnimalService")
	protected void setService(IService<Integer, CausaMorteAnimal> service) {
		super.setService(service);
	}

}
