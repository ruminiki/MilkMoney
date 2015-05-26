package br.com.milksys.model;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the RACA database table.
 * 
 */
@Entity
@NamedQuery(name="Raca.findAll", query="SELECT r FROM Raca r")
public class Raca implements Serializable {
	private static final long serialVersionUID = 1L;

	private StringProperty descricao = new SimpleStringProperty();;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	public Raca() {
	}
	@Access(AccessType.PROPERTY)
	public String getDescricao() {
		return this.descricao.get();
	}

	public void setDescricao(String descricao) {
		this.descricao.set(descricao);
	}
	
	public StringProperty descricaoProperty(){
		return descricao;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}