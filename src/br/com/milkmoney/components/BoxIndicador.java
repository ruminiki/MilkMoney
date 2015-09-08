package br.com.milkmoney.components;

import java.util.function.Function;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.ObjetivoIndicador;
import br.com.milkmoney.util.NumberFormatUtil;

public class BoxIndicador extends VBox {

	public static final String MELHOR_INDICADO  = "MELHOR_INDICADO";
	public static final String DENTRO_INDICADO  = "DENTRO_INDICADO";
	public static final String PIOR_INDICADO    = "PIOR_INDICADO";
	
	private Function<Indicador, Boolean> functionEditIndicador;
	
	private Indicador indicador;
	
	public BoxIndicador(Indicador indicador, Function<Indicador, Boolean> functionEditIndicador) {
		
		this.indicador = indicador;
		this.functionEditIndicador = functionEditIndicador;
		
		this.setAlignment(Pos.CENTER);
		
		Label labelDescricao      = new Label(indicador.getDescricao());
		Label labelValor          = new Label(""+NumberFormatUtil.intFormat(indicador.getValorApurado()));
		Label labelIntervaloIdeal = new Label("Ideal " + indicador.getMenorValorIdeal() + " e " + indicador.getMaiorValorIdeal());
		HBox  hboxSituacao        = new HBox();

		if ( indicador.getObjetivo() != null && indicador.getObjetivo().equals(ObjetivoIndicador.DENTRO_OU_ACIMA_DO_INTERVALO_IDEAL) ){
			if ( indicador.getValorApurado().compareTo(indicador.getMenorValorIdeal()) >= 0 ){
				hboxSituacao.setStyle("-fx-background-color: GREEN");	
			}else{
				hboxSituacao.setStyle("-fx-background-color: RED");
			}
		}else{
			//nesse caso o objetivo é manter dentrou ou abaixo do intervalo ideal
			if ( indicador.getValorApurado().compareTo(indicador.getMaiorValorIdeal()) <= 0 ){
				hboxSituacao.setStyle("-fx-background-color: GREEN");	
			}else{
				hboxSituacao.setStyle("-fx-background-color: RED");
			}
		}
		
		hboxSituacao.setMinHeight(10);
		
		labelValor.setFont(Font.font("Verdana", 36));
		labelDescricao.setFont(Font.font("Verdana", 10));
		
		VBox.setMargin(hboxSituacao, new Insets(3,3,3,3));
		
		this.getChildren().addAll(labelValor, labelDescricao, labelIntervaloIdeal, hboxSituacao);
	
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
		this.setStyle("-fx-border-color: #CCC");
		this.setCursor(Cursor.HAND);
	}

	private void onMouseExit(){
		this.setStyle("");
		this.setCursor(Cursor.DEFAULT);
	}
	
	private void onMouseClicked(){
		functionEditIndicador.apply(indicador);
	}
}
