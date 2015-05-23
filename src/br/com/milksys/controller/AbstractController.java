package br.com.milksys.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import br.com.milksys.service.IService;

@Controller
public class AbstractController<K, E> {
	@FXML
	protected TableView<E> table;
	protected ObservableList<E> data = FXCollections.observableArrayList();
	protected Stage dialogStage;
	protected Object object;
	protected boolean okClicked = false;
	
	protected IService<K, E> service;

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setObject(Object object){
		this.object = object;
	}
	
	/**
	 * Retorna true se o usuário clicar OK,caso contrário false.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	protected void setService(IService<K, E> service){
		this.service = service;
	}
	
	protected void showDetails(Object value){
		
	}
	
	protected void showFormDialog(Object value){
		
	}
	
}
