package br.com.milkmoney.controller.arvoreGenealogica.renderer;

import java.util.function.Function;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import br.com.milkmoney.model.Animal;

public class BoxArvoreGenealogica extends HBox {
	
	private Function<Animal, Boolean> functionSelecionaAnimal;
	private VBox vBox = new VBox();
	private String parentesco;
	private Object parente;
	
	public BoxArvoreGenealogica(String parentesco, Object parente, Function<Animal, Boolean> functionSelecionaAnimal) {
		this.parentesco = parentesco;
		this.parente = parente;
		this.functionSelecionaAnimal = functionSelecionaAnimal;
		
		this.setPrefWidth(160);
		this.buildBox();
	}
	
	private void buildBox(){
		
		this.setAlignment(Pos.CENTER);
		
		this.setStyle("-fx-border-color: #CCC; -fx-border-width: 1");
		
		Label labelPaiMae = new Label(parentesco);
		Label labelAnimal = new Label(parente != null ? parente.toString() : "--");
		
		vBox.setAlignment(Pos.CENTER);
		
		vBox.getChildren().clear();
		vBox.getChildren().addAll(labelPaiMae, labelAnimal);
		
		this.getChildren().clear();
		this.getChildren().add(vBox);
	
		//criar listener ao passar mouse
		this.addEventHandler(MouseEvent.MOUSE_ENTERED,
			new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					onMouseHover();
				}
		});
		
		this.addEventHandler(MouseEvent.MOUSE_EXITED,
			new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					onMouseExit();
				}
		});
		
		this.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 1) {
					onMouseClicked();
				}
				
			}

		});
	}
	
	private void onMouseHover(){
		this.setStyle("-fx-border-color: #ccc; -fx-border-width: 2");
		this.setCursor(Cursor.HAND);
	}

	private void onMouseExit(){
		this.setStyle("-fx-border-color: #CCC; -fx-border-width: 1");
		this.setCursor(Cursor.DEFAULT);
	}
	
	private void onMouseClicked(){
		if ( parente instanceof Animal ){
			functionSelecionaAnimal.apply((Animal)parente);
			buildBox();
		}
	}
}
