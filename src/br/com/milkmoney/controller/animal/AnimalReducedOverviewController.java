package br.com.milkmoney.controller.animal;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.TableCellOptionSelectFactory;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.service.IService;

@Controller
public class AnimalReducedOverviewController extends AbstractReducedOverviewController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> situacaoAnimalColumn;
	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	@FXML private TableColumn<Animal, Date>   dataNascimentoColumn;
	@FXML private TableColumn<Raca,   String> racaColumn;
	@FXML private TableColumn<Animal, String> opcoesColumn;
	
	@Autowired private AnimalFormController animalFormController;
	
	@FXML
	public void initialize() {
		
		situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
		numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
		dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
		racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
		opcoesColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
		opcoesColumn.setCellFactory(new TableCellOptionSelectFactory<Animal,String>(selecionar));
		
		super.initialize(animalFormController);

	}
	
	@Override
	public String getFormName() {
		return "view/animal/AnimalReducedOverview.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Animal";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}

}
