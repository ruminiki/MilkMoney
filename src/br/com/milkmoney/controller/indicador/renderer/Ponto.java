package br.com.milkmoney.controller.indicador.renderer;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Ponto extends Circle {

	public Ponto(double x, double y) {

		LinearGradient gradient = new LinearGradient(0f, 1f, 1f, 0f, true,
				CycleMethod.NO_CYCLE, new Stop(0, Color.web("#ffffff")),
				new Stop(0.5, Color.web("#7a7a52")), new Stop(1,
						Color.web("#ffffff")));

		this.setFill(gradient);
		this.setRadius(5);
		//this.centerXProperty().set(x);
		//this.centerYProperty().set(y);
		this.setLayoutX(x);
		this.setLayoutY(y);
		this.setStrokeType(StrokeType.OUTSIDE);
		this.setStroke(Color.web("#a3a375", 0.5));
		this.setStrokeWidth(5);

	}

}
