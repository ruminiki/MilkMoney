package br.com.milksys.controller;

import javafx.beans.property.SimpleStringProperty;
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
import br.com.milksys.controller.annotations.ColumnBind;
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
	@FXML @ColumnBind(name="descricaoProperty")
	private TextField descricaoField;

	@FXML
	public void initialize() {
		idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
		descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
		
        super.initialize();
        
	}
	
	protected void showFormDialog(Object object) {
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
		
		// Mostra a janela e espera até o usuário fechar.
		dialogStage.showAndWait();
	}
	
	protected boolean isInputValid() {
		return true;
	}

	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}

}
