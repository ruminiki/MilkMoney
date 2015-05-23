package br.com.milksys.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import br.com.milksys.MainApp;

/**
 * O controlador para o root layout. O root layout provê um layout básico
 * para a aplicação contendo uma barra de menu e um espaço onde outros elementos
 * JavaFX podem ser colocados.
 * 
 * @author Marco Jakob
 */
public class RootLayoutController {

    /**
     * Cria uma agenda vazia.
     * @throws IOException 
     */
    @FXML
    private void handleCadastroAnimal() throws IOException {
        
        //loader.setLocation(MainApp.class.getResource("view/animal/AnimalOverview.fxml"));
    	AnchorPane animalOverview = (AnchorPane) MainApp.load("view/animal/AnimalOverview.fxml");
    	MainApp.rootLayout.setCenter(animalOverview);
    	
    }

    /**
     * Abre o FileChooser para permitir o usuário selecionar uma agenda
     * para carregar.
     */
    @FXML
    private void handleCadastroRaca() {
        
    	AnchorPane form = (AnchorPane) MainApp.load("view/raca/RacaForm.fxml");
    	MainApp.rootLayout.setCenter(form);
    	
    }

    /**
     * Salva o arquivo para o arquivo de pessoa aberto atualmente. Se não houver
     * arquivo aberto, a janela "salvar como" é mostrada.
     */
    @FXML
    private void handleSave() {
    }

    /**
     * Abre um FileChooser para permitir o usuário selecionar um arquivo
     * para salvar.
     */
    @FXML
    private void handleSaveAs() {
    }

    /**
     * Abre uma janela Sobre.
     */
    @FXML
    private void handleAbout() {
    	
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("MilkSys");
		alert.setHeaderText("Sobre");
		alert.setContentText("Autor: Ruminiki Schmoeller");
		alert.showAndWait();
		
    }

    /**
     * Fecha a aplicação.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}