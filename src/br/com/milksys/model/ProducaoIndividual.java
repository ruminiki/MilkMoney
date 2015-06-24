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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;


@Entity
@Table(name="producaoIndividual")
@NamedQuery(name="ProducaoIndividual.findAll", query="SELECT e FROM ProducaoIndividual e")
public class ProducaoIndividual implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> data = new SimpleObjectProperty<LocalDate>(DateUtil.asLocalDate(new Date()));  
	private StringProperty primeiraOrdenha = new SimpleStringProperty();
	private StringProperty segundaOrdenha = new SimpleStringProperty();
	private StringProperty terceiraOrdenha = new SimpleStringProperty();
	private StringProperty observacao = new SimpleStringProperty();
	private ObjectProperty<Animal> animal = new SimpleObjectProperty<Animal>();
	private ObjectProperty<PrecoLeite> precoLeite = new SimpleObjectProperty<PrecoLeite>();
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getData() {
		return DateUtil.asDate(data.get());
	}

	public void setData(Date data) {
		this.data.set(DateUtil.asLocalDate(data));
	}
	
	public ObjectProperty<LocalDate> dataProperty(){
		return data;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getPrimeiraOrdenha() {
		return NumberFormatUtil.fromString(this.primeiraOrdenha.get());
	}

	public void setPrimeiraOrdenha(BigDecimal primeiraOrdenha) {
		this.primeiraOrdenha.set(NumberFormatUtil.decimalFormat(primeiraOrdenha));
	}

	public StringProperty primeiraOrdenhaProperty(){
		return primeiraOrdenha;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getSegundaOrdenha() {
		return NumberFormatUtil.fromString(this.segundaOrdenha.get());
	}

	public void setSegundaOrdenha(BigDecimal segundaOrdenha) {
		this.segundaOrdenha.set(NumberFormatUtil.decimalFormat(segundaOrdenha));
	}

	public StringProperty segundaOrdenhaProperty(){
		return segundaOrdenha;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getTerceiraOrdenha() {
		return NumberFormatUtil.fromString(this.terceiraOrdenha.get());
	}

	public void setTerceiraOrdenha(BigDecimal terceiraOrdenha) {
		this.terceiraOrdenha.set(NumberFormatUtil.decimalFormat(terceiraOrdenha));
	}

	public StringProperty terceiraOrdenhaProperty(){
		return terceiraOrdenha;
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
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="precoLeite")
	public PrecoLeite getPrecoLeite() {
		return precoLeite.get();
	}
	
	public void setPrecoLeite(PrecoLeite precoLeite) {
		this.precoLeite.set(precoLeite);
	}
	
	public ObjectProperty<PrecoLeite> precoLeiteProperty(){
		return precoLeite;
	}
	
	@Transient
	public BigDecimal getValor() {
		
		if ( getPrecoLeite() != null ){
			if ( getPrecoLeite().getValorRecebido().compareTo(BigDecimal.ZERO) > 0 ){
				return 	getPrecoLeite().getValorRecebido().multiply(getPrimeiraOrdenha().add(getSegundaOrdenha()).add(getTerceiraOrdenha()));
			}else{
				return 	getPrecoLeite().getValorMaximoPraticado().multiply(getPrimeiraOrdenha().add(getSegundaOrdenha()).add(getTerceiraOrdenha()));
			}
			 
		}
		
		return BigDecimal.ZERO;
		
	}

}