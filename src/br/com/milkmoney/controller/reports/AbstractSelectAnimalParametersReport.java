package br.com.milkmoney.controller.reports;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.RelatorioService;

public class AbstractSelectAnimalParametersReport {
	
	@FXML protected UCTextField inputPesquisa;
	@FXML protected ListView<Animal> listAnimais, listSelecionados;
	@FXML protected Button btnAdicionar, btnAdicionarTodos, btnRemover, btnRemoverTodos;
	
	@Autowired protected AnimalService animalService;
	@Autowired protected RelatorioService relatorioService;

	@FXML
	public void initialize() {
		
		if ( inputPesquisa != null ){
			inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
				listAnimais.setItems(animalService.defaultSearch(newValue));
			});
		}
		
		listAnimais.setItems(animalService.findAllFemeasAtivasAsObservableList());
		listAnimais.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		// captura o evento de double click da table
		listAnimais.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					if ( !listSelecionados.getItems().contains(listAnimais.getSelectionModel().getSelectedItem()) ){
						listSelecionados.getItems().add(listAnimais.getSelectionModel().getSelectedItem());
					}
					listAnimais.getSelectionModel().clearSelection();
				}
			}
		});
		
		btnAdicionar.setOnAction(action -> {
			
			if ( listAnimais.getSelectionModel().getSelectedItems() != null ){
				
				for ( Animal animal : listAnimais.getSelectionModel().getSelectedItems() ){
					
					if ( !listSelecionados.getItems().contains(animal) ){
						listSelecionados.getItems().add(animal);
					}
					
				}
				
				listAnimais.getSelectionModel().clearSelection();
				
			}
			
		});
		
		btnAdicionarTodos.setOnAction(action -> {
			
			for ( Animal animal : listAnimais.getItems() ){
				
				if ( !listSelecionados.getItems().contains(animal) ){
					listSelecionados.getItems().add(animal);
				}
				
			}
			
		});
		
		btnRemover.setOnAction(action -> {
			if ( listSelecionados.getSelectionModel().getSelectedItem() != null ){
				listSelecionados.getItems().remove(listSelecionados.getSelectionModel().getSelectedItem());	
			}
		});
		
		btnRemoverTodos.setOnAction(action -> {
			listSelecionados.getItems().clear();
		});
		
	}
	
	@FXML
	protected void handleClose(){
		if ( btnAdicionar != null ){
			Stage stage = (Stage)btnAdicionar.getScene().getWindow();
			// se for popup
			if ( stage.getModality().equals(Modality.APPLICATION_MODAL) ){
				((Stage)btnAdicionar.getScene().getWindow()).close();	
			}else{
				MainApp.resetLayout();
			}
		}
	}
	
}
