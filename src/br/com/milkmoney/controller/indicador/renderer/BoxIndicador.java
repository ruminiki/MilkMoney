package br.com.milkmoney.controller.indicador.renderer;

import java.math.BigDecimal;
import java.util.function.Function;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.ObjetivoIndicador;
import br.com.milkmoney.service.indicadores.AbstractCalculadorIndicador;
import br.com.milkmoney.util.NumberFormatUtil;

public class BoxIndicador extends VBox {

	public static final String MELHOR_INDICADO  = "MELHOR_INDICADO";
	public static final String DENTRO_INDICADO  = "DENTRO_INDICADO";
	public static final String PIOR_INDICADO    = "PIOR_INDICADO";
	
	private Function<Indicador, Boolean> functionEditIndicador;
	private Indicador indicador;
	
	private VBox vbValor = new VBox();
	
	public BoxIndicador(Indicador indicador, Function<Indicador, Boolean> functionEditIndicador) {
		this.indicador = indicador;
		this.functionEditIndicador = functionEditIndicador;
		this.buildBox();
	}
	
	private void buildBox(){
		
		this.setAlignment(Pos.CENTER);
		
		Label labelDescricao      = new Label(indicador.getDescricao());
		Label labelValor          = new Label();
		Label labelIntervaloIdeal = new Label("Ideal " + indicador.getMenorValorIdeal() + " e " + indicador.getMaiorValorIdeal());
		
		String sufixo =  indicador.getSufixo() != null ?  indicador.getSufixo() : "";
		
		if ( indicador.getFormato().equals(AbstractCalculadorIndicador.DECIMAL_FORMAT_DUAS_CASAS) ){
			labelValor.setText(NumberFormatUtil.decimalFormat(indicador.getValorApurado(), 2) + sufixo);
		}
		
		if ( indicador.getFormato().equals(AbstractCalculadorIndicador.DECIMAL_FORMAT_UMA_CASA) ){
			labelValor.setText(NumberFormatUtil.decimalFormat(indicador.getValorApurado(), 1) + sufixo);
		}
		
		if ( indicador.getFormato().equals(AbstractCalculadorIndicador.INTEIRO_FORMAT) ){
			labelValor.setText(NumberFormatUtil.intFormat(indicador.getValorApurado()) + sufixo);
		}
		
		vbValor.setAlignment(Pos.CENTER);
		
		vbValor.setMaxHeight(60);
		vbValor.setMinHeight(60);
		vbValor.setMaxWidth(60);
		vbValor.setMinWidth(60);
		
		vbValor.getChildren().clear();
		vbValor.getChildren().add(labelValor);
		
		setStyle();
		
		labelValor.setFont(Font.font("System", 15));
		labelDescricao.setFont(Font.font("System", 8));
		
		this.getChildren().clear();
		this.getChildren().addAll(vbValor, labelDescricao, labelIntervaloIdeal);
	
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
	
	private void setStyle(){
		if ( indicador.getObjetivo() != null && 
				indicador.getObjetivo().equals(ObjetivoIndicador.DENTRO_OU_ACIMA_DO_INTERVALO_IDEAL) ){
			
			if ( indicador.getValorApurado().compareTo(indicador.getMenorValorIdeal()) >= 0 ){
				//se o indicador estiver até apenas 5% acima do valor mínimo - liga o alerta
				if ( indicador.getMenorValorIdeal().compareTo(BigDecimal.ZERO) > 0 && 
						indicador.getValorApurado().compareTo(BigDecimal.ZERO) > 0 &&
						indicador.getMenorValorIdeal().add(
									indicador.getMenorValorIdeal()
									.multiply(BigDecimal.valueOf(0.05)))
									.compareTo(indicador.getValorApurado()) >= 0 ){
					vbValor.setStyle(styleAlerta());	
				}else{
					vbValor.setStyle(styleIdeal());					
				}
			}else{
				vbValor.setStyle(styleAbaixo());
			}
			
		}
		
		if ( indicador.getObjetivo() != null && 
				indicador.getObjetivo().equals(ObjetivoIndicador.DENTRO_OU_ABAIXO_DO_INTERVALO_IDEAL) ){
			//nesse caso o objetivo é manter dentrou ou abaixo do intervalo ideal
			if ( indicador.getValorApurado().compareTo(indicador.getMaiorValorIdeal()) <= 0 ){
				//se o indicador estiver até apenas 5% abaixo do valor máximo - liga o alerta
				if ( indicador.getMaiorValorIdeal().compareTo(BigDecimal.ZERO) > 0 &&
						indicador.getValorApurado().compareTo(BigDecimal.ZERO) > 0 &&
						indicador.getMaiorValorIdeal().subtract(
								indicador.getMaiorValorIdeal()
								.multiply(BigDecimal.valueOf(0.05)))
								.compareTo(indicador.getValorApurado()) <= 0 ){
					vbValor.setStyle(styleAlerta());	
				}else{
					vbValor.setStyle(styleIdeal());					
				}
			}else{
				vbValor.setStyle(styleAbaixo());
			}
		}
		
		if ( indicador.getObjetivo() != null && 
				indicador.getObjetivo().equals(ObjetivoIndicador.DENTRO_DO_INTERVALO_IDEAL) ){
			//nesse caso o objetivo é manter dentrou do intervalo ideal
			if ( indicador.getValorApurado().compareTo(indicador.getMenorValorIdeal()) >= 0 &&
					indicador.getValorApurado().compareTo(indicador.getMaiorValorIdeal()) <= 0 ){
				
				//se o indicador estiver até apenas 5% da margem inferior - liga o alerta
				if ( indicador.getMenorValorIdeal().compareTo(BigDecimal.ZERO) > 0 &&
						indicador.getValorApurado().compareTo(BigDecimal.ZERO) > 0 &&
						indicador.getMenorValorIdeal().add(
								indicador.getMenorValorIdeal()
								.multiply(BigDecimal.valueOf(0.05)))
								.compareTo(indicador.getValorApurado()) >= 0 ){
					vbValor.setStyle(styleAlerta());	
				}else{
					//se o indicador estiver até apenas 5% da margem superior - liga o alerta
					if ( indicador.getMaiorValorIdeal().compareTo(BigDecimal.ZERO) > 0 && 
							indicador.getValorApurado().compareTo(BigDecimal.ZERO) > 0 &&
							indicador.getMaiorValorIdeal().subtract(
										indicador.getMaiorValorIdeal()
										.multiply(BigDecimal.valueOf(0.05)))
										.compareTo(indicador.getValorApurado()) <= 0 ){
						vbValor.setStyle(styleAlerta());	
					}else{
						vbValor.setStyle(styleIdeal());		
					}
				}
				
			}else{
				vbValor.setStyle(styleAbaixo());
			}
		}
		
	}
	
	private String styleAbaixo(){
		return "-fx-border-color: #FF0000; -fx-border-radius: 50; -fx-border-width: 3";
	}
	
	private String styleIdeal(){
		return "-fx-border-color: #33CC33; -fx-border-radius: 50; -fx-border-width: 3";
	}
	
	private String styleAlerta(){
		return "-fx-border-color: #FFFF00; -fx-border-radius: 50; -fx-border-width: 3";
	}
	
	private void onMouseHover(){
		vbValor.setStyle("-fx-border-color: #999; -fx-border-radius: 50");
		vbValor.setCursor(Cursor.HAND);
	}

	private void onMouseExit(){
		this.setStyle();
		vbValor.setCursor(Cursor.DEFAULT);
	}
	
	private void onMouseClicked(){
		functionEditIndicador.apply(indicador);
		buildBox();
	}
}
