package br.com.milksys.model;

import java.io.Serializable;
import java.util.List;

import javafx.beans.property.StringProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the finalidadelote database table.
 * 
 */
@Entity
@Table(name="finalidadelote")
@NamedQuery(name="FinalidadeLote.findAll", query="SELECT f FROM FinalidadeLote f")
public class FinalidadeLote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private StringProperty descricao;

	public FinalidadeLote() {
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