package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.com.milksys.components.FieldRequired;
import br.com.milksys.util.NumberFormatUtil;


/**
 * The persistent class for the funcionario database table.
 * 
 */
@Entity
@NamedQuery(name="AnimalVendido.findAll", query="SELECT f FROM AnimalVendido f")
public class AnimalVendido extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<Animal>            animal                  = new SimpleObjectProperty<Animal>();
	private ObjectProperty<VendaAnimal>       vendaAnimal             = new SimpleObjectProperty<VendaAnimal>();
	private StringProperty                    destinacaoAnimal        = new SimpleStringProperty(DestinacaoAnimal.ABATE);
	private StringProperty                    valorAnimal             = new SimpleStringProperty();
	private ObjectProperty<MotivoVendaAnimal> motivoVendaAnimal       = new SimpleObjectProperty<MotivoVendaAnimal>();

	public AnimalVendido() {
		// TODO Auto-generated constructor stub
	}
	
	public AnimalVendido(VendaAnimal vendaAnimal) {
		this.vendaAnimal.set(vendaAnimal);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Animal.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="animal")
	@FieldRequired(message="animal vendido")
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
	@ManyToOne(targetEntity=VendaAnimal.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="vendaAnimal")
	@FieldRequired(message="venda animal")
	public VendaAnimal getVendaAnimal() {
		return vendaAnimal.get();
	}
	
	public void setVendaAnimal(VendaAnimal vendaAnimal) {
		this.vendaAnimal.set(vendaAnimal);
	}
	
	public ObjectProperty<VendaAnimal> vendaAnimalProperty(){
		return vendaAnimal;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="destinação do animal")
	public String getDestinacaoAnimal() {
		return this.destinacaoAnimal.get();
	}

	public void setDestinacaoAnimal(String destinacaoAnimal) {
		this.destinacaoAnimal.set(destinacaoAnimal);
	}
	
	public StringProperty destinacaoAnimalProperty(){
		return destinacaoAnimal;
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
	@ManyToOne(targetEntity=MotivoVendaAnimal.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="motivoVendaAnimal")
	@FieldRequired(message="motivo venda animal")
	public MotivoVendaAnimal getMotivoVendaAnimal() {
		return this.motivoVendaAnimal.get();
	}

	public void setMotivoVendaAnimal(MotivoVendaAnimal motivoVendaAnimal) {
		this.motivoVendaAnimal.set(motivoVendaAnimal);
	}
	
	public ObjectProperty<MotivoVendaAnimal> motivoVendaAnimalProperty(){
		return motivoVendaAnimal;
	}
}