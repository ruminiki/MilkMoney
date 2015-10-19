package br.com.milkmoney.controller.morteAnimal;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.CausaMorteAnimal;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.service.IService;

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
		valorAnimalColumn.setCellValueFactory(new PropertyDecimalValueFactory<MorteAnimal,String>("valorAnimal", 2));
		
		super.initialize((MorteAnimalFormController)MainApp.getBean(MorteAnimalFormController.class));

	}

	@Override
	public String getFormTitle() {
		return "Morte Animal";
	}
	
	@Override
	public String getFormName() {
		return "view/morteAnimal/MorteAnimalOverview.fxml";
	}
	
	@Override
	@Resource(name = "morteAnimalService")
	protected void setService(IService<Integer, MorteAnimal> service) {
		super.setService(service);
	}
	
}
