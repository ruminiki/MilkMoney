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


/**
 * The persistent class for the funcionario database table.
 * 
 */
@Entity
@NamedQuery(name="VendaAnimal.findAll", query="SELECT f FROM VendaAnimal f")
public class VendaAnimal extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> dataVenda         = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<LocalDate> dataRecebimento   = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private ObjectProperty<Animal>    animal            = new SimpleObjectProperty<Animal>();
	private StringProperty            destinacaoAnimal  = new SimpleStringProperty();
	private StringProperty            valor             = new SimpleStringProperty();
	private ObjectProperty<Comprador> comprador         = new SimpleObjectProperty<Comprador>();
	private StringProperty            observacao        = new SimpleStringProperty();
	private StringProperty            motivoVendaAnimal = new SimpleStringProperty();

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
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data recebimento previsto")
	public Date getDataRecebimento() {
		return DateUtil.asDate(this.dataRecebimento.get());
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento.set(DateUtil.asLocalDate(dataRecebimento));
	}
	
	public ObjectProperty<LocalDate> dataRecebimentoProperty(){
		return dataRecebimento;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Animal.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="mae")
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
	public BigDecimal getValor() {
		return NumberFormatUtil.fromString(this.valor.get());
	}

	public void setValor(BigDecimal valor) {
		this.valor.set(NumberFormatUtil.decimalFormat(valor));
	}

	public StringProperty valorProperty(){
		return valor;
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
	public String getMotivoVendaAnimal() {
		return this.motivoVendaAnimal.get();
	}

	public void setMotivoVendaAnimal(String motivoVendaAnimal) {
		this.motivoVendaAnimal.set(motivoVendaAnimal);
	}
	
	public StringProperty motivoVendaAnimalProperty(){
		return motivoVendaAnimal;
	}
}