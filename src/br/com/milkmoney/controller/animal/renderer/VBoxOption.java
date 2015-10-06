package br.com.milkmoney.controller.animal.renderer;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class VBoxOption extends VBox {
	
	private String img;
	private boolean disabled = false;
	
	public VBoxOption(String img, String tooltip) {
		
		this.img = img;
		this.setAlignment(Pos.CENTER);
		this.setStyle((java.lang.String)styleDefault(img));
		this.setOnMouseMoved(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		if ( disabled ) return;
        		setStyle((java.lang.String)styleOver(img));
        	}
        });
		this.setOnMouseExited(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		if ( disabled ) return;
        		setStyle((java.lang.String)styleDefault(img));
        	}
        });
		Tooltip.install(
			    this,
			    new Tooltip(tooltip)
		);
	}
	
	public void _setDisabled(boolean disabled){
		super.setDisabled(disabled);
		this.disabled = disabled;
		if ( disabled )
			this.setStyle((java.lang.String)styleDisabled(img));
	}
	
	public boolean _isDisabled(){
		return disabled;
	}
	
	public java.lang.String styleOver(java.lang.String img){
		return  ("-fx-padding: 0; " +
			    "-fx-min-height: 24; -fx-min-width: 24;" +
			    "-fx-background-insets: 0;" +
			    "-fx-effect: dropshadow(three-pass-box, white, 10, 0, 0, 0);" +
                "-fx-background-image: url('" + img + "'); " +	
			    "-fx-background-repeat: no-repeat; " + 
			    "-fx-background-position: center; " +
                "-fx-cursor: HAND; " +
                "-fx-border-width:0; ");
	}
	
	public java.lang.String styleDefault(java.lang.String img){
		return  ("-fx-padding: 0; " +
				"-fx-min-height: 24; -fx-min-width: 24;" +
				"-fx-background-insets: 0;" +
			    "-fx-background-image: url('" + img + "'); " +	
			    "-fx-background-repeat: no-repeat; " + 
			    "-fx-background-position: center; " +
			    "-fx-cursor: HAND; " +
			    "-fx-border-width:0; ");
	}
	
	public java.lang.String styleDisabled(java.lang.String img){
		return  ("-fx-padding: 0; " +
				"-fx-background-insets: 0;" +
				"-fx-min-height: 24; -fx-min-width: 24;" +
			    "-fx-background-image: url('" + img + "'); " +	
			    "-fx-background-repeat: no-repeat; " + 
			    "-fx-background-position: center; " +
			    "-fx-border-width:0; ");
	}
	
	
}
