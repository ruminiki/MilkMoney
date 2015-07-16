package br.com.milksys.model;

import java.util.Date;

public class FichaAnimal {

	private Date   data;
	private String evento;
	private Class<?> classeReferencia;
	private int idEvento;

	public FichaAnimal(Date data, String evento, int idEvento, Class<?> classeReferencia) {
		this.data = data;
		this.evento = evento;
		this.idEvento = idEvento;
		this.classeReferencia = classeReferencia;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public String getEvento() {
		return evento;
	}
	
	public void setEvento(String evento) {
		this.evento = evento;
	}

	public Class<?> getClasseReferencia() {
		return classeReferencia;
	}

	public void setClasseReferencia(Class<?> classeReferencia) {
		this.classeReferencia = classeReferencia;
	}

	public int getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento;
	}

}
