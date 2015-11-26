package br.com.milkmoney.model;

import java.io.Serializable;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.Entity;

@Entity
public class EvolucaoRebanho implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String variavel;
	private String mes;
	private String valor;
	
	public static final String EM_LACTACAO            = "Em Lacta��o";
	public static final String SECAS                  = "Secas";
	public static final String RELACAO_LACTACAO_SECOS = "Rela��o Secos x Lacta��o";
	public static final String ZERO_A_UM_ANO          = "F�meas de 0 a 12 meses (0 a 1 ano)";
	public static final String UM_A_DOIS_ANOS         = "F�meas de 12 a 24 meses (1 a 2 anos)";
	public static final String DOIS_A_TRES_ANOS       = "F�meas de 24 a 36 meses (2 a 3 anos)";
	public static final String TRES_A_QUATRO_ANOS     = "F�meas de 36 a 48 meses (3 a 4 anos)";
	public static final String MAIS_QUATRO_ANOS       = "F�meas acima 48 meses (mais de 4 anos)";
	
	public EvolucaoRebanho() {
	}

	public EvolucaoRebanho(String variavel, String mes, String valor) {
		setVariavel(variavel);
		setMes(mes);
		setValor(valor);
	}
	
	public static ObservableList<EvolucaoRebanho> getItems(){
		ObservableList<EvolucaoRebanho> e = FXCollections.observableArrayList();
		List<String> variaveis = FXCollections.observableArrayList(EM_LACTACAO, SECAS, RELACAO_LACTACAO_SECOS, ZERO_A_UM_ANO, UM_A_DOIS_ANOS, DOIS_A_TRES_ANOS, TRES_A_QUATRO_ANOS, MAIS_QUATRO_ANOS);
		for ( String s : variaveis ){
			e.add(new EvolucaoRebanho(s, null, null));
		}
		return e;
	}
	
	public String getVariavel() {
		return variavel;
	}

	public void setVariavel(String variavel) {
		this.variavel = variavel;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
