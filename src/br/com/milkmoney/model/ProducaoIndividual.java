package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
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
import javax.persistence.Transient;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;

@Entity
@Table(name="producaoIndividual")
@NamedQuery(name="ProducaoIndividual.findAll", query="SELECT e FROM ProducaoIndividual e")
public class ProducaoIndividual extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> data = new SimpleObjectProperty<LocalDate>(DateUtil.asLocalDate(new Date()));  
	private StringProperty primeiraOrdenha = new SimpleStringProperty();
	private StringProperty segundaOrdenha = new SimpleStringProperty();
	private StringProperty terceiraOrdenha = new SimpleStringProperty();
	private StringProperty observacao = new SimpleStringProperty();
	private ObjectProperty<Animal> animal = new SimpleObjectProperty<Animal>();
	private BigDecimal valor;
	
	public ProducaoIndividual() {
	}
	
	public ProducaoIndividual(Date data) {
		setData(data);
	}

	public ProducaoIndividual(Animal animal) {
		this.animal.set(animal);
	}

	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data")
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
	
	@Transient
	public BigDecimal getValor() {
		return this.valor == null ? BigDecimal.ZERO : this.valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	/**
	 * Retorna o codigo do mes sendo 1 - janeiro e 12 - dezembro
	 * @return
	 */
	@Transient
	public int getMes(){
		Calendar data = Calendar.getInstance();
		data.setTime(getData());
		return data != null ? data.get(Calendar.MONTH) + 1 : 0;
	}

	@Transient
	public int getAno(){
		Calendar data = Calendar.getInstance();
		data.setTime(getData());
		return data != null ? data.get(Calendar.YEAR) : 0;
	}
	
	@Transient
	public BigDecimal getTotalProducaoDia(){
		return getPrimeiraOrdenha().add(getSegundaOrdenha()).add(getTerceiraOrdenha());
	}
	
}