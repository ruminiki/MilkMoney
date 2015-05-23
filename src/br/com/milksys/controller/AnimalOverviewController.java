package br.com.milksys.controller;

import java.time.ZoneId;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.model.Animal;
import br.com.milksys.service.AnimalService;
import br.com.milksys.util.DateUtil;

@Controller
public class AnimalOverviewController {
	@FXML
	private TableView<Animal> animalTable;
	@FXML
	private TableColumn<Animal, String> numeroColumn;
	@FXML
	private TableColumn<Animal, String> nomeColumn;
	@FXML
	private Label numeroLabel;
	@FXML
	private Label nomeLabel;
	@FXML
	private Label racaLabel;
	@FXML
	private Label dataNascimentoLabel;
	
	/**
	 * Os dados como uma observable list de Animals.
	 */
	private ObservableList<Animal> animalData = FXCollections.observableArrayList();
	
	//@Resource(name="animalService")
	@Autowired
	public AnimalService animalService;

	/**
	 * O construtor. O construtor é chamado antes do método inicialize().
	 */
	public AnimalOverviewController() {
	}

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente
	 * após o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {
		numeroColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));
		nomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));

		showAnimalDetails(null);

		// Detecta mudanças de seleção e mostra os detalhes da pessoa quando
		// houver mudança.
		animalTable.getSelectionModel().selectedItemProperty().addListener(
						(observable, oldValue, newValue) -> showAnimalDetails(newValue));
		
		animalData.addAll(animalService.findAll());
		
		animalTable.setItems(animalData);
	}

	/**
	 * Abre uma janela para editar detalhes para a pessoa especificada. Se o
	 * usuário clicar OK, as mudanças são salvasno objeto pessoa fornecido e
	 * retorna true.
	 * 
	 * @param Animal
	 *            O objeto pessoa a ser editado
	 * @return true Se o usuário clicou OK, caso contrário false.
	 */
	public boolean showAnimalEditDialog(Animal animal) {
		try {
			// Carrega o arquivo fxml e cria um novo stage para a janela popup.
			//FXMLLoader loader = new FXMLLoader();
	       // loader.setLocation(MainApp.class.getResource("view/animal/AnimalEditForm.fxml"));
	    	AnchorPane animalEdit = (AnchorPane) MainApp.load("view/animal/AnimalEditForm.fxml");

	    	//MainApp.rootLayout.setCenter(animalEdit);
			
			// Cria o palco dialogStage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Animal");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(MainApp.primaryStage);
			Scene scene = new Scene(animalEdit);
			dialogStage.setScene(scene);

			// Define a pessoa no controller.
			AnimalEditFormController controller = (AnimalEditFormController) MainApp.getController(AnimalEditFormController.class);
			controller.setDialogStage(dialogStage);
			controller.setAnimal(animal);
			
			// Mostra a janela e espera até o usuário fechar.
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	private void showAnimalDetails(Animal animal) {
		if (animal != null) {
			// Preenche as labels com informações do objeto animal.
			numeroLabel.setText(animal.getNumero());
			nomeLabel.setText(animal.getNome());
			racaLabel.setText(animal.getRaca());
			dataNascimentoLabel.setText(DateUtil.format(animal.getDataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));

			// TODO: Nós precisamos de uma maneira de converter o aniversário em
			// um String!
			// birthdayLabel.setText(...);
		} else {
			// animal é null, remove todo o texto.
			numeroLabel.setText("");
			nomeLabel.setText("");
			racaLabel.setText("");
			dataNascimentoLabel.setText("");
		}
	}

	/**
	 * Chamado quando o usuário clica no botão delete.
	 */
	@FXML
	private void handleDeleteAnimal() {
		int selectedIndex = animalTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > 0) {
			animalService.remove(animalTable.getItems().get(selectedIndex));
			animalTable.getItems().remove(selectedIndex);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhuma Seleção");
			alert.setHeaderText("Nenhum animal selecionado");
			alert.setContentText("Selecione pelo menos um registro na tabela!");

			alert.showAndWait();
		}

	}

	/**
	 * Chamado quando o usuário clica no botão novo. Abre uma janela para editar
	 * detalhes da nova pessoa.
	 */
	@FXML
	private void handleNewAnimal() {
		Animal tempAnimal = new Animal("", "");
		boolean okClicked = showAnimalEditDialog(tempAnimal);
		if (okClicked) {
			animalService.save(tempAnimal);
			animalData.add(tempAnimal);
		}
	}

	/**
	 * Chamado quando o usuário clica no botão edit. Abre a janela para editar
	 * detalhes da pessoa selecionada.
	 */
	@FXML
	private void handleEditAnimal() {
		int selectedIndex = animalTable.getSelectionModel().getSelectedIndex();
		Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();
		if (selectedAnimal != null) {
			boolean okClicked = showAnimalEditDialog(selectedAnimal);
			if (okClicked) {
				animalService.save(selectedAnimal);
				animalData.remove(selectedIndex);
				animalData.add(selectedAnimal);
			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhuma Seleção");
			alert.setHeaderText("Nenhum animal selecionado");
			alert.setContentText("Selecione pelo menos um registro na tabela!");

			alert.showAndWait();
		}
	}
	
}
