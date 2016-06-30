package br.com.milkmoney.components;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/** a node which displays a value on hover, but is otherwise empty */
public class HoveredThresholdNode extends StackPane {
	public HoveredThresholdNode(String value) {
		setPrefSize(10, 10);

		final Label label = createDataThresholdLabel(value);

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().setAll(label);
				setCursor(Cursor.NONE);
				toFront();
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().clear();
				setCursor(Cursor.CROSSHAIR);
			}
		});
	}

	private Label createDataThresholdLabel(String value) {
		final Label label = new Label(value);
		label.getStyleClass().addAll("default-color0", "chart-line-symbol",	"chart-series-line");
		label.setStyle("-fx-font-size: 12;");
		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		return label;
	}
}