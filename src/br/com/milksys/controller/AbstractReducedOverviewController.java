package br.com.milksys.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import org.springframework.context.ApplicationListener;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.events.ActionEvent;

public abstract class AbstractReducedOverviewController<K, E> extends AbstractOverviewController<K, E> implements ApplicationListener<ActionEvent>{

	
	@Override
	public void initialize(AbstractFormController<K, E> formController) {
		
		super.initialize(formController);
		configureDoubleClickTable();
		
	}
	
	@FXML
	public void selecionar(){
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			super.closeForm();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um registro na tabela.");
		}
	}
	
	@FXML
	private void fechar(){
		this.setObject(null);
		super.closeForm();
	}
	
	@Override
	protected void configureDoubleClickTable(){
		// captura o evento de double click da table
		table.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					selecionar();
				}
			}

		});
	}
	
	@Override@SuppressWarnings("unchecked")
	public void onApplicationEvent(ActionEvent event) {
		if ( event != null ){
			if ( ( event.getEventType().equals(ActionEvent.EVENT_INSERT) || event.getEventType().equals(ActionEvent.EVENT_UPDATE)) ){
				
				if ( event.getSource().getClass().isInstance(getObject()) ){
					this.setObject((E)event.getSource());
					this.closeForm();	
				}
				
			}
		}
	}
			
}
