package br.com.milksys.controller;

import java.util.Date;

import javafx.collections.ListChangeListener;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
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
import br.com.milksys.model.Animal;
import br.com.milksys.model.Raca;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.searchers.SearchFemeasAtivas;
import br.com.milksys.service.searchers.SearchMachosAtivos;
import br.com.milksys.service.searchers.SearchReprodutoresAtivos;

@Controller
public class AnimalReducedController extends AbstractController<Integer, Animal> implements EventTarget {

	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> numeroColumn;
	@FXML private TableColumn<Animal, Date> dataNascimentoColumn;
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<String, String> sexoColumn;
	
	@Autowired private AnimalController animalController;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
			numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
			dataNascimentoColumn.setCellFactory(new TableCellDateFactory<Animal,Date>("dataNascimento"));
			racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
			sexoColumn.setCellValueFactory(new PropertyValueFactory<String,String>("sexo"));
			
			animalController.data.addListener(new ListChangeListener<Animal>(){
				@Override
				public void onChanged(ListChangeListener.Change<? extends Animal> c) {
						data.clear();
						data.addAll(animalController.data);
				}
			});

			super.initialize();
			
			animalController.setControllerOrigin(AnimalReducedController.class);

		}
		
	}
	
	@FXML
	private void selecionar(){
		
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			super.dialogStage.close();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um animal na tabela.");
		}
		
	}

	@FXML
	private void fechar(){
		this.setObject(null);
		super.dialogStage.close();
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
	
	@Override
	protected void handleNew() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		animalController.handleNew();
	}
	
	@Override
	protected void handleEdit() {
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			animalController.setObject(table.getSelectionModel().getSelectedItem());
			animalController.handleEdit();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um animal na tabela.");
		}
	}
	
	@Override
	protected void handleDelete() {
		int index = table.getSelectionModel().getSelectedIndex();
		super.handleDelete();
		if ( animalController.data.size() >= index )
			animalController.data.remove(index);
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
	protected String getFormName() {
		return "view/animal/AnimalReducedOverview.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Animal";
	}
	
	@Override
	protected Animal getObject() {
		return (Animal) super.object;
	}

	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}

	@Override
	public EventDispatchChain buildEventDispatchChain(EventDispatchChain arg0) {
		//Optional<ButtonType> result = CustomAlert.confirmar("Confirmar seleção", "Deseja selecionar o objeto inserido?");
		//if (result.get() == ButtonType.OK) {
		this.setObject(animalController.getObject());
		super.dialogStage.close();
		//}
		return arg0;
	}

}
