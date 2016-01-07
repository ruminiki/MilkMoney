package br.com.milkmoney.controller.applicationUpdate;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.service.ApplicationService;

@Controller
public class ApplicationUpdateController {

	@FXML private ProgressBar progressBar;
	@FXML private Button btnFinalizar, btnAtualizar;
	@FXML private VBox vbMessages;
	
	private ApplicationService applicationService;
	private String novaVersao;
	
	@FXML
	private void handleAtualizar() {
		progressBar.setVisible(true);
		
		btnAtualizar.setDisable(true);
		btnFinalizar.setDisable(true);
		
		Task<Void> task = applicationService.update(novaVersao);
		
		task.setOnSucceeded(e -> {
			btnFinalizar.setDisable(false);
	    	
	    	//verificar se não ocorreu erro...
	    	btnFinalizar.setText("Fechar");
	    	
	    	btnFinalizar.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					System.exit(0);
				}
			});
	    	
	    	progressBar.progressProperty().unbind();
	    	progressBar.setProgress(100);
		});
		
		progressBar.progressProperty().bind(task.progressProperty());
		task.messageProperty().addListener((observable, oldValue, newValue) -> {
			vbMessages.getChildren().add(new Label(newValue));
			//vbMessages.layout();
		});
		
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
		
	}
	
	@FXML
	private void handleCancel() {
		((Stage)progressBar.getScene().getWindow()).close();	
	}
	
	public void setService(ApplicationService service){
		this.applicationService = service;
	}
	
	public void setVersao(String novaVersao){
		this.novaVersao = novaVersao;
		vbMessages.getChildren().add(new Label("Nova versão encontrada: " + novaVersao));
	}
	
}
