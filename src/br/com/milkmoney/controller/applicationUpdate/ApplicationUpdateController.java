package br.com.milkmoney.controller.applicationUpdate;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.service.ApplicationService;

@Controller
public class ApplicationUpdateController {

	@FXML private ProgressBar progressBar;
	@FXML private Button btnFinalizar, btnAtualizar;
	@FXML private TextArea taLog;
	
	private ApplicationService applicationService;
	private String novaVersao;
	
	@FXML
	private void handleAtualizar() {
		progressBar.setVisible(true);
		
		btnAtualizar.setDisable(true);
		btnFinalizar.setDisable(true);
		
		((Stage)progressBar.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				if ( btnFinalizar.isDisable() ){
					CustomAlert.mensagemInfo("Por favor, aguarde enquanto a atualização é concluída.");					
				}
			}
			
		});
		
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
	    	progressBar.setProgress(1);
	    	
		});
		
		progressBar.progressProperty().bind(task.progressProperty());
		task.messageProperty().addListener((observable, oldValue, newValue) -> {
			taLog.appendText(newValue);
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
		taLog.appendText("Nova versão encontrada: " + novaVersao + "\n");
	}
	
}
