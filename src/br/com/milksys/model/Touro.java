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
import javax.persistence.Table;

import br.com.milksys.components.FieldRequired;


/**
 * The persistent class for the RACA database table.
 * 
 */
@Entity
@Table(name="touro")
@NamedQuery(name="Touro.findAll", query="SELECT r FROM Touro r")
public class Touro extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty nome   = new SimpleStringProperty();
	private StringProperty codigo = new SimpleStringProperty();
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="nome")
	public String getNome() {
		return this.nome.get();
	}

	public void setNome(String nome) {
		this.nome.set(nome);
	}
	
	public StringProperty nomeProperty(){
		return nome;
	}
	
	@Access(AccessType.PROPERTY)
	public String getCodigo() {
		return this.codigo.get();
	}

	public void setCodigo(String codigo) {
		this.codigo.set(codigo);
	}
	
	public StringProperty codigoProperty(){
		return codigo;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		
		if ( getCodigo() != null && !getCodigo().isEmpty() )
			return getNome() + " [" + getCodigo()+"]";
		return getNome();
	}
	
}