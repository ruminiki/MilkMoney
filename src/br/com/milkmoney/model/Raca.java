package br.com.milkmoney.model;

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

import br.com.milkmoney.components.FieldRequired;


/**
 * The persistent class for the RACA database table.
 * 
 */
@Entity
@Table(name="raca")
@NamedQuery(name="Raca.findAll", query="SELECT r FROM Raca r")
public class Raca extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty descricao       = new SimpleStringProperty();
	private StringProperty duracaoGestacao = new SimpleStringProperty();
	
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
	@FieldRequired(message="duração gestação")
	public Integer getDuracaoGestacao() {
		return this.duracaoGestacao.get() != null ? Integer.parseInt(this.duracaoGestacao.get()) : 0;
	}

	public void setDuracaoGestacao(Integer duracaoGestacao) {
		this.duracaoGestacao.set(String.valueOf(duracaoGestacao));
	}
	
	public StringProperty duracaoGestacaoProperty(){
		return duracaoGestacao;
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