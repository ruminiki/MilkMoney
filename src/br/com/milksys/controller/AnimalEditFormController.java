package br.com.milksys.controller;

import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import br.com.milksys.model.Animal;

@Controller
public class AnimalEditFormController {
	@FXML
	private TextField numeroField;
	@FXML
	private TextField nomeField;
	@FXML
	private TextField racaField;
	@FXML
	private DatePicker dataNascimentoField;

	private Stage dialogStage;
	private Animal animal;
	private boolean okClicked = false;

	/**
	 * Inicializa a classe controlle. Este método é chamado automaticamente após
	 * o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {
	}

	/**
	 * Define o palco deste dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Define a pessoa a ser editada no dialog.
	 * 
	 * @param animal
	 */
	public void setAnimal(Animal animal) {
		this.animal = animal;

		numeroField.setText(animal.getNumero());
		nomeField.setText(animal.getNome());
		racaField.setText(animal.getRaca());
		dataNascimentoField.setValue(animal.getDataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		dataNascimentoField.setPromptText("dd.mm.yyyy");
	}

	/**
	 * Retorna true se o usuário clicar OK,caso contrário false.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Chamado quando o usuário clica OK.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			animal.setNumero(numeroField.getText());
			animal.setNome(nomeField.getText());
			animal.setRaca(racaField.getText());
			animal.setDataNascimento(Date.from(dataNascimentoField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Chamado quando o usuário clica Cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Valida a entrada do usuário nos campos de texto.
	 * 
	 * @return true se a entrada é válida
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (nomeField.getText() == null || nomeField.getText().length() == 0) {
			errorMessage += "Nome inválido!\n";
		}
		if (numeroField.getText() == null
				|| numeroField.getText().length() == 0) {
			errorMessage += "Sobrenome inválido!\n";
		}
		if (racaField.getText() == null || racaField.getText().length() == 0) {
			errorMessage += "Rua inválida!\n";
		}
		if (dataNascimentoField.getValue() == null) {
			errorMessage += "Aniversário inválido!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Mostra a mensagem de erro.
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Campos Inválidos");
			alert.setHeaderText("Por favor, corrija os campos inválidos");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
		}
	}
}
