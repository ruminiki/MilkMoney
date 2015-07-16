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
@Table(name="parametro")
@NamedQuery(name="Parametro.findAll", query="SELECT r FROM Parametro r")
public class Parametro extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty sigla = new SimpleStringProperty();
	private StringProperty descricao = new SimpleStringProperty();
	private StringProperty valor = new SimpleStringProperty();
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="sigla")
	public String getSigla() {
		return this.sigla.get();
	}

	public void setSigla(String sigla) {
		this.sigla.set(sigla);
	}
	
	public StringProperty siglaProperty(){
		return sigla;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="descrição")
	public String getDescricao() {
		return this.descricao.get();
	}

	public void setDescricao(String descricao) {
		this.descricao.set(descricao);
	}
	
	public StringProperty descricaoProperty(){
		return descricao;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="valor")
	public String getValor() {
		return this.valor.get();
	}

	public void setValor(String valor) {
		this.valor.set(valor);
	}
	
	public StringProperty valorProperty(){
		return valor;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return getDescricao();
	}
	
}