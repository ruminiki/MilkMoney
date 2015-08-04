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
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import br.com.milkmoney.components.FieldRequired;


@Entity
@NamedQuery(name="Lote.findAll", query="SELECT l FROM Lote l")
public class Lote extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty                 descricao      = new SimpleStringProperty();
	private ObjectProperty<FinalidadeLote> finalidadeLote = new SimpleObjectProperty<FinalidadeLote>();
	private ObjectProperty<Boolean>        ativo          = new SimpleObjectProperty<Boolean>();
	private List<LoteAnimal>               loteAnimal     = new ArrayList<LoteAnimal>();
	
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
	@ManyToOne(targetEntity=FinalidadeLote.class)
	@JoinColumn(name="finalidade")
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
	public Boolean isAtivo() {
		return this.ativo.get();
	}

	public void setAtivo(Boolean ativo) {
		this.ativo.set(ativo);
	}
	
	public ObjectProperty<Boolean> ativoProperty(){
		return ativo;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToMany(orphanRemoval=true, targetEntity=LoteAnimal.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="lote")
	public List<LoteAnimal> getLoteAnimal() {
		return loteAnimal;
	}

	public void setLoteAnimal(List<LoteAnimal> loteAnimal) {
		this.loteAnimal = loteAnimal;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
	
}