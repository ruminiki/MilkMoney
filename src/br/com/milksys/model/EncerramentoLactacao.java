package br.com.milksys.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
import javax.persistence.Transient;

import br.com.milksys.components.FieldRequired;
import br.com.milksys.util.DateUtil;


@Entity
@NamedQuery(name="EncerramentoLactacao.findAll", query="SELECT f FROM EncerramentoLactacao f")
public class EncerramentoLactacao extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> data                       = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<LocalDate> dataPrevisaoParto          = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private StringProperty            observacao                 = new SimpleStringProperty();
	private ObjectProperty<Animal>    animal                     = new SimpleObjectProperty<Animal>();
	private StringProperty            motivoEncerramentoLactacao = new SimpleStringProperty();

	public EncerramentoLactacao() {
	}
	
	public EncerramentoLactacao(Animal animal) {
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
	@FieldRequired(message="data encerramento lactação")
	public Date getData() {
		return DateUtil.asDate(this.data.get());
	}

	public void setData(Date data) {
		this.data.set(DateUtil.asLocalDate(data));
	}
	
	public ObjectProperty<LocalDate> dataProperty(){
		return data;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataPrevisaoParto() {
		return DateUtil.asDate(this.dataPrevisaoParto.get());
	}

	public void setDataPrevisaoParto(Date dataPrevisaoParto) {
		this.dataPrevisaoParto.set(DateUtil.asLocalDate(dataPrevisaoParto));
	}
	
	public ObjectProperty<LocalDate> dataPrevisaoPartoProperty(){
		return dataPrevisaoParto;
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
	@ManyToOne(targetEntity=Animal.class, cascade=CascadeType.REFRESH)
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
	@FieldRequired(message="motivo encerramento lactação")
	public String getMotivoEncerramentoLactacao() {
		return this.motivoEncerramentoLactacao.get();
	}

	public void setMotivoEncerramentoLactacao(String motivoEncerramentoLactacao) {
		this.motivoEncerramentoLactacao.set(motivoEncerramentoLactacao);
	}
	
	public StringProperty motivoEncerramentoLactacaoProperty(){
		return motivoEncerramentoLactacao;
	}
	
	@Transient
	public String getDiasParaParto() {
		long dias = 0;
		if ( this.dataPrevisaoParto != null && dataPrevisaoParto.get() != null ){
			dias = ChronoUnit.DAYS.between(LocalDate.now(), this.dataPrevisaoParto.get());
		}
		return dias <= 0 ? "--" : String.valueOf(dias);
	}
	
}