package br.com.milkmoney.controller.databaseBackup;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.service.DatabaseBackupService;

@Controller
public class DatabaseBackupController {

	@FXML private Button btnFinalizar, btnIniciar;
	@FXML private TextArea taLog;
	
	@Autowired private DatabaseBackupService databaseBackupService;
	
	@FXML
	private void handleIniciar() {
		
		btnIniciar.setDisable(true);
		btnFinalizar.setDisable(true);
		
		((Stage)taLog.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				if ( btnFinalizar.isDisable() ){
					CustomAlert.mensagemInfo("Por favor, aguarde enquanto o backup é concluído.");					
				}
			}
			
		});
		
		Task<Void> task = databaseBackupService.backup();
		
		task.setOnSucceeded(e -> {
			btnFinalizar.setDisable(false);
	    	
	    	//verificar se não ocorreu erro...
	    	btnFinalizar.setText("Fechar");
	    	
	    	btnFinalizar.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					handleCancel();
				}
			});
	    	
		});
		
		task.messageProperty().addListener((observable, oldValue, newValue) -> {
			taLog.appendText(newValue);
		});
		
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
		
	}
	
	@FXML
	private void handleCancel() {
		Stage window = ((Stage)taLog.getScene().getWindow());
		// se for popup
		if ( window.getModality().equals(Modality.APPLICATION_MODAL) ){
			window.close();	
		}else{
			MainApp.resetLayout();
		}
	}
	
}
