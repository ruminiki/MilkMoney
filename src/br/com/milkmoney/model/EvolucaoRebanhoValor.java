package br.com.milkmoney.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class EvolucaoRebanhoValor implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String mes;
	private String valor;
	
	public EvolucaoRebanhoValor() {
	}

	public EvolucaoRebanhoValor(String mes, String valor) {
		setMes(mes);
		setValor(valor);
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
	
	@Override
	public String toString() {
		return getMes() + " - " + getValor();
	}

}
