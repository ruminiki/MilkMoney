package br.com.milksys.model;

import java.io.Serializable;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the precoleite database table.
 * 
 */
@Entity
@Table(name="precoleite")
@NamedQuery(name="PrecoLeite.findAll", query="SELECT p FROM PrecoLeite p")
public class PrecoLeite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private FloatProperty valor;

	private ObjectProperty<CalendarioRecolha> calendariorecolha;

	public PrecoLeite() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getValor() {
		return this.valor.get();
	}

	public void setValor(float valor) {
		this.valor.set(valor);
	}
	
	public FloatProperty valorProperty(){
		return this.valor;
	}
	
	@ManyToOne
	@JoinColumn(name="periodoReferencia")
	public CalendarioRecolha getCalendariorecolha() {
		return this.calendariorecolha.get();
	}

	public void setCalendariorecolha(CalendarioRecolha calendariorecolha) {
		this.calendariorecolha.set(calendariorecolha);
	}
	
	public ObjectProperty<CalendarioRecolha> calendarioRecolhaProperty(){
		return this.calendariorecolha;
	}

}