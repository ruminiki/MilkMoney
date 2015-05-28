package br.com.milksys.model;

import java.io.Serializable;
import java.util.List;

import javafx.beans.property.StringProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the situacaoprocedimentoagendado database table.
 * 
 */
@Entity
@Table(name="situacaoprocedimentoagendado")
@NamedQuery(name="SituacaoProcedimentoAgendado.findAll", query="SELECT s FROM SituacaoProcedimentoAgendado s")
public class SituacaoProcedimentoAgendado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
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