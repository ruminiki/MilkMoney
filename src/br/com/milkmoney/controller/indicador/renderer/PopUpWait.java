package br.com.milkmoney.controller.indicador.renderer;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import org.controlsfx.control.PopOver;

public class PopUpWait extends PopOver {
		
		private VBox              container         = new VBox();
		private ProgressBar       progressBar       = new ProgressBar();
		private StringProperty    progress          = new SimpleStringProperty();
		
		public PopUpWait(String message) {

			container.setSpacing(5);
			container.setAlignment(Pos.CENTER);
			container.setStyle("-fx-background-color: #FFF");
			container.getChildren().add(new Label(message));
			container.setMinWidth(400);
			container.setMaxHeight(80);
			container.setMinHeight(80);
			
			Label lblProgress = new Label("0%");
			lblProgress.textProperty().bind(progress);
			progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
				progress.set(BigDecimal.valueOf(progressBar.getProgress()*100).setScale(0, RoundingMode.HALF_EVEN)+"%");
			});
			
			progressBar.setMinWidth(380);
			progressBar.setMaxWidth(380);
			container.getChildren().addAll(progressBar, lblProgress);
			
			this.centerOnScreen();
			this.setArrowSize(0);
			this.setDetachable(false);
			this.setCornerRadius(0);
			this.setAutoHide(false);
			this.setHideOnEscape(false);
			this.setContentNode(container);
			
		}
		
		public ProgressBar getProgressBar(){
			return progressBar;
		}
			
	}