package br.com.milksys.controller.morteAnimal;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.CausaMorteAnimal;
import br.com.milksys.model.MorteAnimal;
import br.com.milksys.service.IService;

@Controller
public class MorteAnimalOverviewController extends AbstractOverviewController<Integer, MorteAnimal> {

	@FXML private TableColumn<MorteAnimal, Date> dataMorteColumn;
	@FXML private TableColumn<Animal, String> animalColumn;
	@FXML private TableColumn<CausaMorteAnimal, String> causaMorteAnimalColumn;
	@FXML private TableColumn<MorteAnimal, String> valorAnimalColumn;
	
	@FXML
	public void initialize() {
		
		dataMorteColumn.setCellFactory(new TableCellDateFactory<MorteAnimal,Date>("dataMorte"));
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("animal"));
		causaMorteAnimalColumn.setCellValueFactory(new PropertyValueFactory<CausaMorteAnimal,String>("causaMorteAnimal"));
		valorAnimalColumn.setCellValueFactory(new PropertyDecimalValueFactory<MorteAnimal,String>("valorAnimal"));
		
		super.initialize((MorteAnimalFormController)MainApp.getBean(MorteAnimalFormController.class));
		
	}

	@Override
	protected String getFormTitle() {
		return "Morte Animal";
	}
	
	@Override
	protected String getFormName() {
		return "view/morteAnimal/MorteAnimalOverview.fxml";
	}
	
	@Override
	@Resource(name = "morteAnimalService")
	protected void setService(IService<Integer, MorteAnimal> service) {
		super.setService(service);
	}
	
}
