package br.com.milkmoney.controller.indicador.renderer;

import java.util.function.Function;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import br.com.milkmoney.model.Indicador;

public class BoxDescricaoIndicador extends VBox {

	private Indicador indicador;
	private Function<Indicador, Boolean> functionEditIndicador;
	
	public BoxDescricaoIndicador(Indicador indicador, Function<Indicador, Boolean> functionEditIndicador) {
		this.indicador = indicador;
		this.functionEditIndicador = functionEditIndicador;
		
		this.setMaxHeight(30);
		this.setMinHeight(30);
		
		this.buildBox();
	}
	
	private void buildBox(){
		
		this.setAlignment(Pos.CENTER_RIGHT);
		
		Hyperlink labelDescricao = new Hyperlink(indicador.getDescricao());
		labelDescricao.setWrapText(true);
		labelDescricao.setFocusTraversable(false);
		labelDescricao.setFont(Font.font("System", 10));
		labelDescricao.setStyle("-fx-text-fill: black;");
		
		labelDescricao.setOnAction(e -> onMouseClicked());

		this.setStyle("-fx-background-color: #CCC;");
		
		this.getChildren().clear();
		this.getChildren().addAll(labelDescricao);
	
	}
	
	private void onMouseClicked(){
		functionEditIndicador.apply(indicador);
	}
	

}
