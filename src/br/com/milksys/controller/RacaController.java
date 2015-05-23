package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.model.Raca;
import br.com.milksys.service.IService;

@Controller
public class RacaController extends AbstractController<Integer, Raca> {

	@FXML
	private TableColumn<Raca, String> idColumn;
	@FXML
	private TableColumn<Raca, String> descricaoColumn;
	@FXML
	private TextField idField;
	@FXML
	private TextField descricaoField;

	public RacaController() {
		super();
	}

	@FXML
	protected void initialize() {
		idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
		descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
		// Detecta mudanças de seleção e mostra os detalhes da pessoa quando houver mudança.
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showDetails(newValue));
		if ( data.size() <= 0 ){
			data.addAll(service.findAll());
			table.setItems(data);
		}
		
	}
	
	public void showFormDialog(Object object) {
		// Carrega o arquivo fxml e cria um novo stage para a janela popup.
		AnchorPane racaForm = (AnchorPane) MainApp.load("view/raca/RacaFormDialog.fxml");

		// Cria o palco dialogStage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Raça");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(racaForm);
		dialogStage.setScene(scene);

		// Define o objeto no controller.
		RacaController controller = (RacaController) MainApp.getController(RacaController.class);
		controller.setDialogStage(dialogStage);
		controller.setObject(object);
		
		descricaoField.setText(((Raca)object).getDescricao());

		// Mostra a janela e espera até o usuário fechar.
		dialogStage.showAndWait();
	}
	
	/**
	 * Chamado quando o usuário clica no botão novo. Abre uma janela para editar
	 * detalhes da nova pessoa.
	 */
	@FXML
	private void handleNew() {
		object = new Raca();
		showFormDialog(object);
	}


	/**
	 * Chamado quando o usuário clica no botão edit. Abre a janela para editar
	 * detalhes da pessoa selecionada.
	 */
	@FXML
	private void handleEdit() {
		object = table.getSelectionModel().getSelectedItem();
		if (object != null) {
			showFormDialog(object);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhuma Seleção");
			alert.setHeaderText("Nenhum animal selecionado");
			alert.setContentText("Selecione pelo menos um registro na tabela!");
			alert.showAndWait();
		}
	}

	/**
	 * Chamado quando o usuário clica no botão delete.
	 */
	@FXML
	private void handleDelete() {
		int selectedIndex = table.getSelectionModel().getSelectedIndex();
		if (selectedIndex > 0) {
			service.remove(table.getItems().get(selectedIndex));
			table.getItems().remove(selectedIndex);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nenhuma Seleção");
			alert.setHeaderText("Nenhum registro selecionado");
			alert.setContentText("Selecione pelo menos um registro na tabela!");
			alert.showAndWait();
		}

	}
	
	/**
	 * Chamado quando o usuário clica OK no pop up.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			//fazer via reflection ou eventListener
			((Raca) object).setDescricao(descricaoField.getText());
			dialogStage.close();

			if(((Raca) object).getId() > 0){
				data.remove(table.getSelectionModel().getSelectedIndex());
			}
			
			data.add((Raca)object);
			
			service.save((Raca) object);
		}
	}

	/**
	 * Chamado quando o usuário clica Cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@Override
	protected void showDetails(Object value) {

	}

	/**
	 * Valida a entrada do usuário nos campos de texto.
	 * 
	 * @return true se a entrada é válida
	 */
	private boolean isInputValid() {
		return true;
	}

	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}

}
