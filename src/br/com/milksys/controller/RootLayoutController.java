package br.com.milksys.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import br.com.milksys.MainApp;

/**
 * O controlador para o root layout. O root layout prov� um layout b�sico
 * para a aplica��o contendo uma barra de menu e um espa�o onde outros elementos
 * JavaFX podem ser colocados.
 * 
 * @author Marco Jakob
 */
public class RootLayoutController {

    @FXML
    private void handleCadastroAnimal() throws IOException {
        
    	AnchorPane animalOverview = (AnchorPane) MainApp.load("view/animal/AnimalOverview.fxml");
    	MainApp.rootLayout.setCenter(animalOverview);
    	
    }

    @FXML
    private void handleCadastroRaca() {
        
    	AnchorPane form = (AnchorPane) MainApp.load("view/raca/RacaOverview.fxml");
    	MainApp.rootLayout.setCenter(form);
    	
    }

    @FXML
    private void handleCadastroCalendarioRecolha() {
        
    	AnchorPane form = (AnchorPane) MainApp.load("view/calendarioRecolha/CalendarioRecolhaOverview.fxml");
    	MainApp.rootLayout.setCenter(form);
    	
    }
    
    @FXML
    private void handleCadastroFinalidadeLote() {
        
    	AnchorPane form = (AnchorPane) MainApp.load("view/finalidadeLote/FinalidadeLoteOverview.fxml");
    	MainApp.rootLayout.setCenter(form);
    	
    }
       
    
    @FXML
    private void handleCadastroEntregaLeite() {
        
    	AnchorPane form = (AnchorPane) MainApp.load("view/entregaLeite/EntregaLeiteOverview.fxml");
    	MainApp.rootLayout.setCenter(form);
    	
    }

    @FXML
    private void handleSaveAs() {
    }

    /**
     * Abre uma janela Sobre.
     */
    @FXML
    private void handleAbout() {
    	
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Milk Money 1.0");
		alert.setHeaderText("Sobre");
		alert.setContentText("Autor: Ruminiki Schmoeller");
		alert.showAndWait();
		
    }

    /**
     * Fecha a aplica��o.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}