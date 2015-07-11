package br.com.milksys.controller.animal;

import java.util.Date;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Raca;
import br.com.milksys.service.IService;
import br.com.milksys.service.searchers.SearchFemeasAtivas;
import br.com.milksys.service.searchers.SearchMachosAtivos;
import br.com.milksys.service.searchers.SearchReprodutoresAtivos;

@Controller
public class AnimalReducedOverviewController extends AbstractReducedOverviewController<Integer, Animal> {

	@FXML private TableColumn<Animal, String> situacaoAnimalColumn;
	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	@FXML private TableColumn<Animal, Date> dataNascimentoColumn;
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	
	@Autowired private AnimalFormController animalFormController;
	
	@FXML
	public void initialize() {
		
		situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("situacaoAnimal"));
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
		numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
		dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
		racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
		sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexo"));
		
		super.initialize(animalFormController);

	}
	
	@FXML
	private void selecionar(){
		
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			super.closeForm();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um animal na tabela.");
		}
		
	}

	@FXML
	private void fechar(){
		this.setObject(null);
		super.closeForm();
	}
	
	@Override
	protected void configureDoubleClickTable(){
		// captura o evento de double click da table
		table.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					selecionar();
				}
			}

		});
	}
	
	@FXML
	private void handleFindFemeas(){
		setSearch((SearchFemeasAtivas)MainApp.getBean(SearchFemeasAtivas.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindMachos(){
		setSearch((SearchMachosAtivos)MainApp.getBean(SearchMachosAtivos.class));
		refreshTableOverview();
	}
	
	@FXML
	private void handleFindReprodutores(){
		setSearch((SearchReprodutoresAtivos)MainApp.getBean(SearchReprodutoresAtivos.class));
		refreshTableOverview();
	}
	
	@Override
	public String getFormName() {
		return "view/animal/AnimalReducedOverview.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Animal";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}

}
