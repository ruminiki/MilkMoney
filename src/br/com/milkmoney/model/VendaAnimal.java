package br.com.milkmoney.model;

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

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;


@Entity
@NamedQuery(name="VendaAnimal.findAll", query="SELECT f FROM VendaAnimal f")
public class VendaAnimal extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate>         dataVenda               = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<Comprador>         comprador               = new SimpleObjectProperty<Comprador>();
	private StringProperty                    observacao              = new SimpleStringProperty();
	private ObjectProperty<Animal>            animal                  = new SimpleObjectProperty<Animal>();
	private ObjectProperty<MotivoVendaAnimal> motivoVendaAnimal       = new SimpleObjectProperty<MotivoVendaAnimal>();
	private StringProperty                    destinacaoAnimal        = new SimpleStringProperty(DestinacaoAnimal.ABATE);
	private StringProperty                    valorAnimal             = new SimpleStringProperty();

	
	public VendaAnimal() {
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data da venda")
	public Date getDataVenda() {
		return DateUtil.asDate(this.dataVenda.get());
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda.set(DateUtil.asLocalDate(dataVenda));
	}
	
	public ObjectProperty<LocalDate> dataVendaProperty(){
		return dataVenda;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Comprador.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="comprador")
	@FieldRequired(message="comprador")
	public Comprador getComprador() {
		return comprador.get();
	}
	
	public void setComprador(Comprador comprador) {
		this.comprador.set(comprador);
	}
	
	public ObjectProperty<Comprador> compradorProperty(){
		return comprador;
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
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="animal")
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
	
}