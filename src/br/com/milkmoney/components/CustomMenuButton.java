package br.com.milkmoney.components;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class CustomMenuButton extends Popup {
	
	public CustomMenuButton(int columns, int rows) {
		
		HBox container = new HBox();
		
		container.setSpacing(3);
		container.setAlignment(Pos.TOP_LEFT);
		container.setStyle("-fx-background-color: #FFF");
		
		for ( int i = 0; i <= rows; i++ ){
			VBox column = new VBox();
			column.setSpacing(3);
			column.setStyle("-fx-background-color: #FFF");
			for ( int j = 0; j <= columns; j++ ){
				
				HBox itemRow = new HBox();
				itemRow.setMinWidth(200);
				itemRow.getChildren().add(new Label("AA"));
				itemRow.setAlignment(Pos.CENTER_LEFT);
				
				//criar listener ao passar mouse
				itemRow.addEventHandler(MouseEvent.MOUSE_ENTERED,
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							itemRow.setStyle("-fx-background-color: #CCC;");
						}
				});
				
				itemRow.addEventHandler(MouseEvent.MOUSE_EXITED,
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							itemRow.setStyle("-fx-background-color: #FFF");
						}
				});
				
				column.getChildren().add(itemRow);
				
			}
			
			container.getChildren().add(column);
			
			if ( i < rows ){
				container.getChildren().add(new Separator(Orientation.VERTICAL));
			}
			
			getContent().add(container);
			
		}
		
	}
	
}
