package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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

	@FXML
	public void initialize() {
		idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
		descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
		// Detecta mudan�as de sele��o e mostra os detalhes da pessoa quando houver mudan�a.
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectRowTableHandler(newValue));
		
        super.initialize();
        
	}
	
	protected void showFormDialog(Object object) {
		// Carrega o arquivo fxml e cria um novo stage para a janela popup.
		AnchorPane racaForm = (AnchorPane) MainApp.load("view/raca/RacaFormDialog.fxml");

		// Cria o palco dialogStage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Ra�a");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(racaForm);
		dialogStage.setScene(scene);

		// Define o objeto no controller.
		RacaController controller = (RacaController) MainApp.getController(RacaController.class);
		controller.setDialogStage(dialogStage);
		controller.setObject(object);
		
		descricaoField.setText(((Raca)object).getDescricao());

		// Mostra a janela e espera at� o usu�rio fechar.
		dialogStage.showAndWait();
	}
	
	/**
	 * Chamado quando o usu�rio clica OK no pop up.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			//fazer via reflection ou eventListener
			object().setDescricao(descricaoField.getText());
			dialogStage.close();

			if(object().getId() > 0){
				data.remove(table.getSelectionModel().getSelectedIndex());
			}
			
			data.add(object());
			
			service.save(object());
		}
	}
	
	public Raca object(){
		return (Raca) super.object;
	}
	
	/**
	 * Valida a entrada do usu�rio nos campos de texto.
	 * 
	 * @return true se a entrada � v�lida
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
