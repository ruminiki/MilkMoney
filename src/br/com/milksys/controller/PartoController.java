package br.com.milksys.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Parto;
import br.com.milksys.model.Raca;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class PartoController extends AbstractController<Integer, Parto> {

	@FXML private TableView<Animal> table;
	@FXML private TableColumn<Animal, String> numeroColumn;
	@FXML private TableColumn<Animal, String> nomeColumn;
	@FXML private TableColumn<Animal, String> sexoColumn;
	@FXML private TableColumn<Raca, String> racaColumn;
	@FXML private TableColumn<Animal, String> removerColumn;
	
	@FXML private UCTextField inputCobertura;
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputObservacao;
	
	@FXML private Button btnSalvar;
	@FXML Hyperlink btnAdicionarCria;
	
	@Autowired private AnimalController animalController;

	@FXML
	public void initialize() {
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || 
				state.equals(State.INSERT_TO_SELECT) || state.equals(State.CREATE_TO_SELECT) ){
			
			inputCobertura.setText(getObject().getCobertura().toString());
			inputData.valueProperty().bindBidirectional(getObject().dataProperty());
			inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());

			nomeColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
			numeroColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("numero"));
			racaColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("raca"));
			sexoColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("sexo"));
			removerColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("nome"));
			removerColumn.setCellFactory(new Callback<TableColumn<Animal,String>, TableCell<Animal,String>>(){
				@Override
				public TableCell<Animal, String> call(TableColumn<Animal, String> param) {
					TableCell<Animal, String> cell = new TableCell<Animal, String>(){
						@Override
						public void updateItem(String item, boolean empty) {
							if(item!=null){
								Hyperlink link = new Hyperlink();
								
								link.setDisable(getObject().getId() > 0 );
								
								if ( getTableRow().getItem() != null )
									link.setText("Remover");
								link.setFocusTraversable(false);
								link.setOnAction(new EventHandler<ActionEvent>() {
								    @Override
								    public void handle(ActionEvent e) {
								    	getObject().getCrias().remove(getTableRow().getIndex());
								    	table.getItems().remove(getTableRow().getIndex());								
								    }
								});
								setGraphic(link);
							} 
						}
					};                           
					return cell;
				}
			});
			
			btnAdicionarCria.setDisable(getObject().getId() > 0 );
			btnSalvar.setDisable(getObject().getId() > 0 );
			inputData.setDisable(getObject().getId() > 0 );
			inputObservacao.setDisable(getObject().getId() > 0);
			
			//super.initialize();
			table.setItems(FXCollections.observableArrayList(getObject().getCrias()));
			
		}
		
	}
	
	@FXML
	protected void handleSelecionarCadastrarAnimal() {
		
		animalController.setState(State.CREATE_TO_SELECT);
		animalController.object = new Animal();
		animalController.showForm(animalController.getFormName());
		
		if ( animalController.getObject() != null ){
			getObject().getCrias().add( animalController.getObject() );
			table.getItems().add(animalController.getObject());
		}
		
	}

	@Override
	protected String getFormName() {
		return "view/parto/PartoForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Parto";
	}
	
	@Override
	protected Parto getObject() {
		return (Parto)super.object;
	}

	@Override
	@Resource(name = "partoService")
	protected void setService(IService<Integer, Parto> service) {
		super.setService(service);
	}

}
