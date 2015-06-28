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
 * The persistent class for the motivoocorrenciafuncionario database table.
 * 
 */
@Entity
@NamedQuery(name="MotivoOcorrenciaFuncionario.findAll", query="SELECT m FROM MotivoOcorrenciaFuncionario m")
public class MotivoOcorrenciaFuncionario extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private StringProperty descricao = new SimpleStringProperty();

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