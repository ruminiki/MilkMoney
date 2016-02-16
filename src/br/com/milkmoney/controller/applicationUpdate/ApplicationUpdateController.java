package br.com.milkmoney.controller.applicationUpdate;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.service.ApplicationUpdateService;

@Controller
public class ApplicationUpdateController {

	@FXML private Button btnFinalizar, btnAtualizar;
	@FXML private TextArea taLog;
	@FXML private ProgressIndicator progressIndicator;
	
	private ApplicationUpdateService applicationService;
	private String novaVersao;
	
	@FXML
	private void handleAtualizar() {
		
		btnAtualizar.setDisable(true);
		btnFinalizar.setDisable(true);
		
		((Stage)taLog.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				if ( btnFinalizar.isDisable() ){
					CustomAlert.mensagemInfo("Por favor, aguarde enquanto a atualização é concluída.");					
				}
			}
			
		});
		
		Task<Void> task = applicationService.update(novaVersao);
		
		progressIndicator.progressProperty().bind(task.progressProperty());
		progressIndicator.setVisible(true);
		
		task.setOnSucceeded(e -> {
			btnFinalizar.setDisable(false);
			progressIndicator.setVisible(false);
	    	
	    	//verificar se não ocorreu erro...
	    	btnFinalizar.setText("Fechar");
	    	
	    	btnFinalizar.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					System.exit(0);
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
		((Stage)taLog.getScene().getWindow()).close();	
	}
	
	public void setService(ApplicationUpdateService service){
		this.applicationService = service;
	}
	
	public void setVersao(String novaVersao){
		this.novaVersao = novaVersao;
		taLog.appendText("Nova versão encontrada: " + novaVersao + "\n");
	}
	
}
