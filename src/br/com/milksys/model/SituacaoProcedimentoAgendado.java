package br.com.milksys.model;

import java.io.Serializable;

import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the situacaoprocedimentoagendado database table.
 * 
 */
@Entity
@Table(name="situacaoProcedimentoAgendado")
@NamedQuery(name="SituacaoProcedimentoAgendado.findAll", query="SELECT s FROM SituacaoProcedimentoAgendado s")
public class SituacaoProcedimentoAgendado extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private StringProperty descricao;

	public SituacaoProcedimentoAgendado() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Access(AccessType.PROPERTY)
	public String getDescricao() {
		return this.descricao.get();
	}

	public void setDescricao(String descricao) {
		this.descricao.set(descricao);
	}
	
	public StringProperty descricaoProperty(){
		return this.descricao;
	}

}