package br.com.milkmoney.controller;

import java.util.function.Function;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import br.com.milkmoney.components.CustomAlert;

public abstract class AbstractReducedOverviewController<K, E> extends AbstractOverviewController<K, E>{

	
	@Override
	public void initialize(AbstractFormController<K, E> formController) {
		
		super.initialize(formController);
		
	}
	
	protected Function<Object, Boolean> selecionar = obj -> {
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			super.closeForm();
			return true;
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um registro na tabela.");
			return false;
		}
	};
	
	@FXML
	private void handleLimparSelecao(){
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
					//selecionar();
					if ( !getFormConfig().get(EDIT_DISABLED) ){
						handleEdit();
					}
					
				}
			}

		});
	}
	
}
