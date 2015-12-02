package br.com.milkmoney.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import br.com.milkmoney.components.FieldRequired;

@Entity
@NamedQuery(name="MotivoEncerramentoLactacao.findAll", query="SELECT l FROM MotivoEncerramentoLactacao l")
public class MotivoEncerramentoLactacao extends AbstractEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty descricao       = new SimpleStringProperty();
	
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