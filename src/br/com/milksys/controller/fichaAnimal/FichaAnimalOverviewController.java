package br.com.milksys.controller.fichaAnimal;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FichaAnimal;
import br.com.milksys.service.FichaAnimalService;

@Controller
public class FichaAnimalOverviewController {

	@FXML private TableView<FichaAnimal> table;
	@FXML private TableColumn<FichaAnimal, String> dataColumn;
	@FXML private TableColumn<FichaAnimal, String> eventoColumn;
	
	@Autowired FichaAnimalService fichaAnimalService;
	
	private Animal animal;
	
	@FXML
	public void initialize() {
		
		dataColumn.setCellFactory(new TableCellDateFactory<FichaAnimal,String>("data"));
		eventoColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("evento"));
		
		table.getItems().clear();
		table.setItems(fichaAnimalService.findAllByAnimal(animal));
		
	}
	
	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public String getFormTitle() {
		return "Eventos Animal";
	}
	
	public String getFormName() {
		return "view/fichaAnimal/FichaAnimalOverview.fxml";
	}
	
}
