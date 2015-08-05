package br.com.milkmoney.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.milkmoney.components.FieldRequired;


@Entity
@Table(name="lote")
@NamedQuery(name="Lote.findAll", query="SELECT a FROM Lote a")
public class Lote extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty                 descricao      = new SimpleStringProperty();
	private ObjectProperty<FinalidadeLote> finalidadeLote = new SimpleObjectProperty<FinalidadeLote>();
	private StringProperty                 ativo          = new SimpleStringProperty(SimNao.SIM);
	private List<Animal>                   animais        = new ArrayList<Animal>();
	
	public Lote() {
	}
	
	public Lote(FinalidadeLote finalidade) {
		this.finalidadeLote.set(finalidade);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="descri��o")
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
	@ManyToOne(targetEntity=FinalidadeLote.class)
	@JoinColumn(name="finalidadeLote")
	@FieldRequired(message="finalidade")
	public FinalidadeLote getFinalidadeLote() {
		return finalidadeLote.get();
	}
	
	public void setFinalidadeLote(FinalidadeLote finalidadeLote) {
		this.finalidadeLote.set(finalidadeLote);
	}
	
	public ObjectProperty<FinalidadeLote> finalidadeLoteProperty(){
		return finalidadeLote;
	}
	
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="ativo")
	public String isAtivo() {
		return this.ativo.get();
	}

	public void setAtivo(String ativo) {
		this.ativo.set(ativo);
	}
	
	public StringProperty ativoProperty(){
		return ativo;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToMany
	@JoinTable(name="loteAnimal", joinColumns={@JoinColumn(name="lote", referencedColumnName="id")},
	      inverseJoinColumns={@JoinColumn(name="animal", referencedColumnName="id")})
	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
	
}