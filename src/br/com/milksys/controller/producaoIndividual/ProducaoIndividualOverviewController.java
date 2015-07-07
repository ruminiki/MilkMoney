package br.com.milksys.controller.producaoIndividual;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.State;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.IService;
import br.com.milksys.service.ProducaoIndividualService;
import br.com.milksys.service.searchers.SearchFemeasAtivas;

@Controller
public class ProducaoIndividualOverviewController extends AbstractOverviewController<Integer, ProducaoIndividual> {

	@FXML private TableView<Animal> tableAnimal; 
	@FXML private TableColumn<Animal, String> animalListColumn;
	
	@FXML private TableColumn<Animal, String> animalColumn;
	@FXML private TableColumn<ProducaoIndividual, LocalDate> dataColumn;
	@FXML private TableColumn<ProducaoIndividual, String> primeiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> segundaOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> terceiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> observacaoColumn;
	@FXML private TableColumn<ProducaoIndividual, String> valorColumn;
	
	@Autowired private AnimalService animalService;
	@Autowired private SearchFemeasAtivas searchFemeasAtivas;
	@Autowired private ProducaoIndividualFormController producaoIndividualFormController;
	
	private Animal selectedAnimal;
	
	@FXML
	public void initialize() {
		
		tableAnimal.setItems(searchFemeasAtivas.doSearch());
		tableAnimal.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> findByAnimal(newValue));
		animalListColumn.setCellValueFactory(new PropertyValueFactory<Animal, String>("numeroNome"));
	
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal, String>("animal"));
		dataColumn.setCellFactory(new TableCellDateFactory<ProducaoIndividual, LocalDate>("data"));
		primeiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("primeiraOrdenha"));
		segundaOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("segundaOrdenha"));
		terceiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("terceiraOrdenha"));
		valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("valor"));
		observacaoColumn.setCellValueFactory(new PropertyValueFactory<ProducaoIndividual, String>("observacao"));
		
		super.initialize(producaoIndividualFormController);
		
	}
	
	@Override
	public void handleNew() {
		setObject(new ProducaoIndividual(selectedAnimal));
		producaoIndividualFormController.setOverviewController(this);
		producaoIndividualFormController.setState(State.INSERT);
		producaoIndividualFormController.setObject(getObject());
		producaoIndividualFormController.showForm();
	}
	
	@Override
	public void handleEdit() {
		if ( getObject() != null ){
			producaoIndividualFormController.setOverviewController(this);
			producaoIndividualFormController.setState(State.INSERT);
			producaoIndividualFormController.setObject(getObject());
			producaoIndividualFormController.showForm();
		}
	}
	
	@Override
	protected void refreshTableOverview() {
		data.clear();
		
		if ( selectedAnimal == null || selectedAnimal.getId() <= 0 ){
			if ( tableAnimal.getItems() != null && tableAnimal.getItems().size() > 0 ){
				table.getSelectionModel().clearAndSelect(0);
				selectedAnimal = tableAnimal.getItems().get(0);
				refreshTableOverview();
			}	
		}else{
			data.addAll(((ProducaoIndividualService)service).findByAnimal(selectedAnimal));
		}
		
		updateLabelNumRegistros();
			
		((ProducaoIndividualService)service).atualizaValorProducao(data);
		
	}
	
	private void findByAnimal(Animal animal) {
		selectedAnimal = animal;
		refreshTableOverview();
	}

	@Override
	protected String getFormName() {
		return "view/producaoIndividual/ProducaoIndividualForm.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Produção Individual";
	}
	
	@Override
	@Resource(name = "producaoIndividualService")
	protected void setService(IService<Integer, ProducaoIndividual> service) {
		super.setService(service);
	}

}
