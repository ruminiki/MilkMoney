package br.com.milkmoney.components;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import org.controlsfx.control.PopOver;

public class WaitReport {
		
		public static void wait(Task<Void> task, Window window){
			
			window.getScene().getRoot().setDisable(true);
			
			VBox container = new VBox();
			
			container.setSpacing(5);
			container.setAlignment(Pos.CENTER);
			container.setStyle("-fx-background-color: #FFF");
			container.getChildren().add(new Label("Aguarde..."));
			container.setMinWidth(300);
			container.setMaxHeight(100);
			container.setMinHeight(100);
			
			ProgressIndicator progressIndicator = new ProgressIndicator();
			progressIndicator.setMinSize(60, 60);
			container.getChildren().add(progressIndicator);
			
			PopOver popUp = new PopOver();
			
			popUp.centerOnScreen();
			popUp.setArrowSize(0);
			popUp.setDetachable(false);
			popUp.setCornerRadius(0);
			popUp.setAutoHide(false);
			popUp.setHideOnEscape(false);
			popUp.setContentNode(container);
			
			progressIndicator.progressProperty().bind(task.progressProperty());
			popUp.show(window);
			
			task.setOnSucceeded(e -> {
				popUp.hide();
				window.getScene().getRoot().setDisable(false);
			});
			
			Thread thread = new Thread(task);
			thread.setDaemon(true);
			thread.start();
			
		}
	}