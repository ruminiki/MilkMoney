package br.com.milkmoney.controller.root.renderer;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class VBoxOption extends VBox {
	
	public VBoxOption(String img, String tooltip) {
		
		this.setAlignment(Pos.CENTER);
		this.setStyle((java.lang.String)styleDefault(img));
		this.setOnMouseMoved(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		setStyle((java.lang.String)styleOver(img));
        	}
        });
		this.setOnMouseExited(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		setStyle((java.lang.String)styleDefault(img));
        	}
        });
		Tooltip.install(
			    this,
			    new Tooltip(tooltip)
		);
		
	}
	
	public java.lang.String styleOver(java.lang.String img){
		return  (
				"-fx-min-height: 128; -fx-min-width: 128;" +
				"-fx-max-height: 128; -fx-max-width: 128;" +
                "-fx-cursor: HAND; " +
			    "-fx-effect: dropshadow(three-pass-box, #CCC, 10, 0, 0, 0);" +
                "-fx-background-image: url('" + img + "'); " +		
                "-fx-background-color: cornsilk; " +
                "-fx-border-width:0; " +
                "-fx-border-color: " +
                    "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                    ");");
	}
	
	public java.lang.String styleDefault(java.lang.String img){
		return  ("-fx-padding: 1; " +
				"-fx-min-height: 128; -fx-min-width: 128;" +
				"-fx-max-height: 128; -fx-max-width: 128;" +
                "-fx-cursor: HAND; " +
                "-fx-background-image: url('" + img + "'); " +		
                "-fx-border-width:0; " +
                "-fx-border-color: " +
                    "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                    ");");
	}
	
}
