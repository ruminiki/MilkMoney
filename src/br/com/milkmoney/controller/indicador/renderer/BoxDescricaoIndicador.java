package br.com.milkmoney.controller.indicador.renderer;

import java.util.function.Function;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import br.com.milkmoney.model.Indicador;

public class BoxDescricaoIndicador extends VBox {

	private Indicador indicador;
	
	public BoxDescricaoIndicador(Indicador indicador, Function<Indicador, Boolean> functionEditIndicador) {
		this.indicador = indicador;
		
		this.setMaxHeight(30);
		this.setMinHeight(30);
		
		this.buildBox();
	}
	
	private void buildBox(){
		
		this.setAlignment(Pos.CENTER_RIGHT);
		
		Label labelDescricao = new Label(indicador.getDescricao());
		labelDescricao.setWrapText(true);
		labelDescricao.setFont(Font.font("System", 10));

		this.setStyle("-fx-background-color: #CCC;");
		
		this.getChildren().clear();
		this.getChildren().addAll(labelDescricao);
	
	}
	

}
