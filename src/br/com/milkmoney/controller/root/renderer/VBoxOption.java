package br.com.milkmoney.controller.root.renderer;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class VBoxOption extends VBox {
	
	public VBoxOption(String img, String legend) {
		
		VBox vbIcon = new VBox();
		vbIcon.setAlignment(Pos.BOTTOM_CENTER);
		ImageView icon = null;
		try{
			icon = new ImageView(new Image(ClassLoader.getSystemResourceAsStream(img)));	
			HBox.setHgrow(vbIcon, Priority.ALWAYS);
			VBox.setVgrow(vbIcon, Priority.ALWAYS);
			vbIcon.getChildren().add(icon);
			VBox.setMargin(vbIcon,new Insets(3,0,0,0));
			this.getChildren().add(vbIcon);
		}catch(Exception e){
			System.out.println("N�o encontrado �cone: " + legend);
		}
		
		VBox vbLegend = new VBox();
		Label label = new Label(legend);
		label.setWrapText(true);
		label.setTextAlignment(TextAlignment.CENTER);
		vbLegend.setAlignment(Pos.CENTER);
		label.setPrefHeight(68);
		HBox.setHgrow(vbLegend, Priority.ALWAYS);
		VBox.setVgrow(vbLegend, Priority.ALWAYS);
		vbLegend.getChildren().add(label);
		
		HBox.setHgrow(this, Priority.ALWAYS);
		VBox.setVgrow(this, Priority.ALWAYS);
		
		this.getChildren().add(vbLegend);
		
		this.setAlignment(Pos.CENTER);
		this.setStyle((java.lang.String)styleDefault());
		this.setOnMouseMoved(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		setStyle((java.lang.String)styleOver());
        	}
        });
		this.setOnMouseExited(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		setStyle((java.lang.String)styleDefault());
        	}
        });
		
		Tooltip.install(this, new Tooltip(legend));
	}
	
	/*public void setWidth(double width){
		this.setMinWidth(width);
		this.setMaxWidth(width);
	}*/
	
	public java.lang.String styleOver(){
		return  (
                "-fx-cursor: HAND; " +
			    "-fx-effect: dropshadow(three-pass-box, #CCC, 10, 0, 0, 0);" +
                "-fx-background-color: cornsilk; " +
                "-fx-border-width:0; " +
                "-fx-border-color: " +
                    "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                    ");");
	}
	
	public java.lang.String styleDefault(){
		return  (
                "-fx-cursor: HAND; " +
                "-fx-background-color: #e0e0e0; " +
                "-fx-border-width:0; " +
                "-fx-border-color: " +
                    "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                    ");");
	}
	
}
