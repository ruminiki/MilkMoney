package br.com.milkmoney.components;

import java.util.function.Function;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

public class CustomTreeItem extends TreeItem<String> {

	private Function<Integer, Boolean> function;
	
	public CustomTreeItem(String label, boolean colapse, Function<Integer, Boolean> function) {
		super(label);
		this.function = function;
		
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
		
		//get//.setStyle("-fx-text-fill: #00cc00; -fx-font-weight: bold");
		
		addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				function.apply(null);
			}
			
		});
		
	}

	public Function<Integer, Boolean> getFunction() {
		return function;
	}

	public void setFunction(Function<Integer, Boolean> function) {
		this.function = function;
	}
	
}
