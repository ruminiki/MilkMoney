package br.com.milksys.model;

import java.io.Serializable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
 * The persistent class for the finalidadelote database table.
 * 
 */
@Entity
@Table(name="situacaoAnimal")
@NamedQuery(name="SituacaoAnimal.findAll", query="SELECT f FROM SituacaoAnimal f")
public class SituacaoAnimal extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty descricao = new SimpleStringProperty();
	private BooleanProperty animalAtivo = new SimpleBooleanProperty();

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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
		return this.descricao;
	}
	
	@Access(AccessType.PROPERTY)
	public Boolean getAnimalAtivo() {
		return this.animalAtivo.get();
	}

	public void setAnimalAtivo(boolean animalAtivo) {
		this.animalAtivo.set(animalAtivo);
	}
	
	public BooleanProperty animalAtivoProperty(){
		return this.animalAtivo;
	}

	public String getAnimalAtivoText(){
		return getAnimalAtivo() ? "ATIVO" : "INATIVO";
	}
	
	@Override
	public String toString() {
		return getDescricao();
	}
}