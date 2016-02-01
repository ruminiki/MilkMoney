package br.com.milkmoney.components;

import java.util.function.Function;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

public class CustomTreeItem extends TreeItem<String> {

	@SuppressWarnings("rawtypes")
	public CustomTreeItem(String label, boolean colapse, Function function) {
		
		super(label);
		
		addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

			}
			
		});
		
		if ( !colapse ){
			setExpanded(true);
			addEventHandler(TreeItem.branchCollapsedEvent(),
					new EventHandler<TreeModificationEvent<String>>() {

						@Override
						public void handle(TreeModificationEvent<String> event) {
							event.getTreeItem().setExpanded(true);
						}
						
			});
	
		}
		
		addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(MouseEvent arg0) {
				function.apply(null);
			}
			
		});
		
	}
	
	
	
}
