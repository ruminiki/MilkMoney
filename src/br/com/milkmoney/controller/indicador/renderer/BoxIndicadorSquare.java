package br.com.milkmoney.controller.indicador.renderer;

import java.math.BigDecimal;
import java.util.function.Function;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.ObjetivoIndicador;
import br.com.milkmoney.service.indicadores.AbstractCalculadorIndicador;
import br.com.milkmoney.util.NumberFormatUtil;

public class BoxIndicadorSquare extends VBox {

	public static final String MELHOR_INDICADO  = "MELHOR_INDICADO";
	public static final String DENTRO_INDICADO  = "DENTRO_INDICADO";
	public static final String PIOR_INDICADO    = "PIOR_INDICADO";
	
	private Function<Indicador, Boolean> functionEditIndicador;
	private Indicador indicador;
	
	public BoxIndicadorSquare(Indicador indicador, Function<Indicador, Boolean> functionEditIndicador) {
		this.indicador = indicador;
		this.functionEditIndicador = functionEditIndicador;
		
		this.setMaxHeight(30);
		this.setMinHeight(30);
		this.setMaxWidth(80);
		this.setMinWidth(80);
		
		this.buildBox();
	}
	
	private void buildBox(){
		
		this.setAlignment(Pos.CENTER);
		
		Hyperlink labelValor = new Hyperlink("Objetivo " + indicador.getMenorValorIdeal() + " e " + indicador.getMaiorValorIdeal());
		labelValor.setStyle("-fx-text-fill: white;");
		labelValor.setFocusTraversable(false);
		labelValor.setWrapText(true);
		labelValor.setOnAction(e -> onMouseClicked());
		
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
		
		setStyle();
		
		labelValor.setFont(Font.font("System", 13));
		
		this.getChildren().clear();
		this.getChildren().addAll(labelValor);
	
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
					this.setStyle(styleAlerta());	
				}else{
					this.setStyle(styleIdeal());					
				}
			}else{
				this.setStyle(styleAbaixo());
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
					this.setStyle(styleAlerta());	
				}else{
					this.setStyle(styleIdeal());					
				}
			}else{
				this.setStyle(styleAbaixo());
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
					this.setStyle(styleAlerta());	
				}else{
					//se o indicador estiver até apenas 5% da margem superior - liga o alerta
					if ( indicador.getMaiorValorIdeal().compareTo(BigDecimal.ZERO) > 0 && 
							indicador.getValorApurado().compareTo(BigDecimal.ZERO) > 0 &&
							indicador.getMaiorValorIdeal().subtract(
										indicador.getMaiorValorIdeal()
										.multiply(BigDecimal.valueOf(0.05)))
										.compareTo(indicador.getValorApurado()) <= 0 ){
						this.setStyle(styleAlerta());	
					}else{
						this.setStyle(styleIdeal());		
					}
				}
				
			}else{
				this.setStyle(styleAbaixo());
			}
		}
		
	}
	
	private String styleAbaixo(){
		return "-fx-background-color: #FF0000;";
	}
	
	private String styleIdeal(){
		return "-fx-background-color: #33CC33;";
	}
	
	private String styleAlerta(){
		return "-fx-background-color: #FFFF00;";
	}
	
	private void onMouseClicked(){
		functionEditIndicador.apply(indicador);
		buildBox();
	}
}
