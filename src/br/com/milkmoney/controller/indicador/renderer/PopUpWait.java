package br.com.milkmoney.controller.indicador.renderer;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUpWait{
		
		private HBox container = new HBox();
		
		public PopUpWait(String message) {

			container.setSpacing(5);
			container.setAlignment(Pos.CENTER);
			container.setStyle("-fx-background-color: #FFF");
			container.getChildren().add(new Label(message));
			container.setMinWidth(300);
			container.setMinHeight(80);
			
			final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            Scene dialogScene = new Scene(container, 300, 80);
            dialog.setScene(dialogScene);
            dialog.show();
			
		}
			
	}