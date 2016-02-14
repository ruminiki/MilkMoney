package br.com.milkmoney.controller.indicador.renderer;

import java.math.BigDecimal;
import java.util.function.Function;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import br.com.milkmoney.model.ConfiguracaoIndicador;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.ObjetivoIndicador;
import br.com.milkmoney.model.ValorIndicador;

public class BoxIndicadorSquare extends VBox {

	public static final String MELHOR_INDICADO  = "MELHOR_INDICADO";
	public static final String DENTRO_INDICADO  = "DENTRO_INDICADO";
	public static final String PIOR_INDICADO    = "PIOR_INDICADO";
	
	private Indicador indicador;
	private ValorIndicador valorIndicador;
	private ConfiguracaoIndicador configuracaoIndicador;
	
	public BoxIndicadorSquare(Indicador indicador, int ano, int mes, Function<Indicador, Boolean> functionEditIndicador) {
		
		this.indicador = indicador;
		this.valorIndicador = indicador.getValorIndicador(ano, mes);
		this.configuracaoIndicador = indicador.getConfiguracaoIndicador(ano);
		
		this.setMaxHeight(30);
		this.setMinHeight(30);
		this.setMaxWidth(80);
		this.setMinWidth(80);
		
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
		
		this.buildBox();
	}
	
	private void buildBox(){
		
		this.setAlignment(Pos.CENTER);
		
		Label labelValor = new Label();
		String sufixo =  indicador.getSufixo() != null ?  indicador.getSufixo() : "";
		labelValor.setText(valorIndicador.getValor() + sufixo);
		
		setStyle();
		
		labelValor.setFont(Font.font("System", 13));
		labelValor.setStyle("-fx-text-fill: white;");
		labelValor.setWrapText(true);
		
		this.getChildren().clear();
		this.getChildren().addAll(labelValor);
		
	}
	
	private void setStyle(){
		if ( configuracaoIndicador.getObjetivo() != null && 
				configuracaoIndicador.getObjetivo().equals(ObjetivoIndicador.DENTRO_OU_ACIMA_DO_INTERVALO_IDEAL) ){
			
			if ( valorIndicador.getValor().compareTo(configuracaoIndicador.getMenorValorIdeal()) >= 0 ){
				//se o indicador estiver até apenas 5% acima do valor mínimo - liga o alerta
				if ( configuracaoIndicador.getMenorValorIdeal().compareTo(BigDecimal.ZERO) > 0 && 
						valorIndicador.getValor().compareTo(BigDecimal.ZERO) > 0 &&
						configuracaoIndicador.getMenorValorIdeal().add(
								configuracaoIndicador.getMenorValorIdeal()
									.multiply(BigDecimal.valueOf(0.05)))
									.compareTo(valorIndicador.getValor()) >= 0 ){
					this.setStyle(styleAlerta());	
				}else{
					this.setStyle(styleIdeal());					
				}
			}else{
				this.setStyle(styleAbaixo());
			}
			
		}
		
		if ( configuracaoIndicador.getObjetivo() != null && 
				configuracaoIndicador.getObjetivo().equals(ObjetivoIndicador.DENTRO_OU_ABAIXO_DO_INTERVALO_IDEAL) ){
			//nesse caso o objetivo é manter dentrou ou abaixo do intervalo ideal
			if ( valorIndicador.getValor().compareTo(configuracaoIndicador.getMaiorValorIdeal()) <= 0 ){
				//se o indicador estiver até apenas 5% abaixo do valor máximo - liga o alerta
				if ( configuracaoIndicador.getMaiorValorIdeal().compareTo(BigDecimal.ZERO) > 0 &&
						valorIndicador.getValor().compareTo(BigDecimal.ZERO) > 0 &&
						configuracaoIndicador.getMaiorValorIdeal().subtract(
								configuracaoIndicador.getMaiorValorIdeal()
								.multiply(BigDecimal.valueOf(0.05)))
								.compareTo(valorIndicador.getValor()) <= 0 ){
					this.setStyle(styleAlerta());	
				}else{
					this.setStyle(styleIdeal());					
				}
			}else{
				this.setStyle(styleAbaixo());
			}
		}
		
		if ( configuracaoIndicador.getObjetivo() != null && 
				configuracaoIndicador.getObjetivo().equals(ObjetivoIndicador.DENTRO_DO_INTERVALO_IDEAL) ){
			//nesse caso o objetivo é manter dentrou do intervalo ideal
			if ( valorIndicador.getValor().compareTo(configuracaoIndicador.getMenorValorIdeal()) >= 0 &&
					valorIndicador.getValor().compareTo(configuracaoIndicador.getMaiorValorIdeal()) <= 0 ){
				
				//se o indicador estiver até apenas 5% da margem inferior - liga o alerta
				if ( configuracaoIndicador.getMenorValorIdeal().compareTo(BigDecimal.ZERO) > 0 &&
						valorIndicador.getValor().compareTo(BigDecimal.ZERO) > 0 &&
						configuracaoIndicador.getMenorValorIdeal().add(
								configuracaoIndicador.getMenorValorIdeal()
								.multiply(BigDecimal.valueOf(0.05)))
								.compareTo(valorIndicador.getValor()) >= 0 ){
					this.setStyle(styleAlerta());	
				}else{
					//se o indicador estiver até apenas 5% da margem superior - liga o alerta
					if ( configuracaoIndicador.getMaiorValorIdeal().compareTo(BigDecimal.ZERO) > 0 && 
							valorIndicador.getValor().compareTo(BigDecimal.ZERO) > 0 &&
							configuracaoIndicador.getMaiorValorIdeal().subtract(
									configuracaoIndicador.getMaiorValorIdeal()
										.multiply(BigDecimal.valueOf(0.05)))
										.compareTo(valorIndicador.getValor()) <= 0 ){
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
}
