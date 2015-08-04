package br.com.milkmoney.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;

@Entity
@Table(name="loteAnimal")
@NamedQuery(name="LoteAnimal.findAll", query="SELECT e FROM LoteAnimal e")
public class LoteAnimal extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> dataInclusao = new SimpleObjectProperty<LocalDate>(DateUtil.asLocalDate(new Date()));  
	private ObjectProperty<LocalDate> dataRemocao  = new SimpleObjectProperty<LocalDate>(DateUtil.asLocalDate(new Date())); 
	private ObjectProperty<Animal>    animal       = new SimpleObjectProperty<Animal>();
	private StringProperty            observacao   = new SimpleStringProperty();
	
	public LoteAnimal() {
	}
	
	public LoteAnimal(Animal animal) {
		setAnimal(animal);
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data inclusão")
	public Date getDataInclusao() {
		return DateUtil.asDate(dataInclusao.get());
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao.set(DateUtil.asLocalDate(dataInclusao));
	}
	
	public ObjectProperty<LocalDate> dataInclusaoProperty(){
		return dataInclusao;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data remoção")
	public Date getDataRemocao() {
		return DateUtil.asDate(dataRemocao.get());
	}

	public void setDataRemocao(Date dataRemocao) {
		this.dataRemocao.set(DateUtil.asLocalDate(dataRemocao));
	}
	
	public ObjectProperty<LocalDate> dataRemocaoProperty(){
		return dataRemocao;
	}

	@Access(AccessType.PROPERTY)
	public String getObservacao() {
		return this.observacao.get();
	}

	public void setObservacao(String observacao) {
		this.observacao.set(observacao);
	}

	public StringProperty observacaoProperty(){
		return observacao;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne@JoinColumn(name="animal")
	@FieldRequired(message="animal")
	public Animal getAnimal() {
		return animal.get();
	}
	
	public void setAnimal(Animal animal) {
		this.animal.set(animal);
	}
	
	public ObjectProperty<Animal> animalProperty(){
		return animal;
	}
	
	public StringProperty numeroNomeAnimalProperty(){
		if ( getAnimal() != null ){
			return new SimpleStringProperty(getAnimal().getNumeroNome());
		}
		return new SimpleStringProperty();
	}
	
}