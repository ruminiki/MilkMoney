package br.com.milksys.model;

import java.io.Serializable;

import javafx.beans.property.FloatProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the precoleite database table.
 * 
 */
@Entity
@Table(name="precoLeite")
@NamedQuery(name="PrecoLeite.findAll", query="SELECT p FROM PrecoLeite p")
public class PrecoLeite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private FloatProperty valor;

	public PrecoLeite() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Access(AccessType.PROPERTY)
	public float getValor() {
		return this.valor.get();
	}

	public void setValor(float valor) {
		this.valor.set(valor);
	}
	
	public FloatProperty valorProperty(){
		return this.valor;
	}
	
}