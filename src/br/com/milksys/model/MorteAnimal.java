package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.milksys.components.FieldRequired;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;


@Entity
@NamedQuery(name="MorteAnimal.findAll", query="SELECT f FROM MorteAnimal f")
public class MorteAnimal extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate>        dataMorte        = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<Animal>           animal           = new SimpleObjectProperty<Animal>();
	private StringProperty                   valorAnimal      = new SimpleStringProperty();
	private StringProperty                   observacao       = new SimpleStringProperty();
	private ObjectProperty<CausaMorteAnimal> causaMorteAnimal = new SimpleObjectProperty<CausaMorteAnimal>();

	public MorteAnimal() {
		// TODO Auto-generated constructor stub
	}
	
	public MorteAnimal(Animal animal) {
		this.animal.set(animal);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data da morte")
	public Date getDataMorte() {
		return DateUtil.asDate(this.dataMorte.get());
	}

	public void setDataMorte(Date dataMorte) {
		this.dataMorte.set(DateUtil.asLocalDate(dataMorte));
	}
	
	public ObjectProperty<LocalDate> dataMorteProperty(){
		return dataMorte;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Animal.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="animal")
	@FieldRequired(message="animal morto")
	public Animal getAnimal() {
		return animal.get();
	}
	
	public void setAnimal(Animal animal) {
		this.animal.set(animal);
	}
	
	public ObjectProperty<Animal> animalProperty(){
		return animal;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="valor do animal")
	public BigDecimal getValorAnimal() {
		return NumberFormatUtil.fromString(this.valorAnimal.get());
	}

	public void setValorAnimal(BigDecimal valorAnimal) {
		this.valorAnimal.set(NumberFormatUtil.decimalFormat(valorAnimal));
	}

	public StringProperty valorAnimalProperty(){
		return valorAnimal;
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
	@ManyToOne(targetEntity=CausaMorteAnimal.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="causaMorteAnimal")
	@FieldRequired(message="motivo venda animal")
	public CausaMorteAnimal getCausaMorteAnimal() {
		return this.causaMorteAnimal.get();
	}

	public void setCausaMorteAnimal(CausaMorteAnimal causaMorteAnimal) {
		this.causaMorteAnimal.set(causaMorteAnimal);
	}
	
	public ObjectProperty<CausaMorteAnimal> causaMorteAnimalProperty(){
		return causaMorteAnimal;
	}
}