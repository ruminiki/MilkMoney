package br.com.milksys.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

import br.com.milksys.util.DateUtil;


/**
 * The persistent class for the ANIMAL database table.
 * 
 */
@Entity
@Table(name="entregaLeite")
@NamedQuery(name="EntregaLeite.findAll", query="SELECT e FROM EntregaLeite e")
public class EntregaLeite implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> data = new SimpleObjectProperty<LocalDate>(DateUtil.asLocalDate(new Date()));  
	private Property<Number> numeroVacasOrdenhadas = new SimpleIntegerProperty();
	private FloatProperty volume = new SimpleFloatProperty();
	private ObjectProperty<CalendarioRecolha> calendarioRecolha = new SimpleObjectProperty<CalendarioRecolha>();
	private StringProperty observacao = new SimpleStringProperty();

	public EntregaLeite() {
	}
	
	public EntregaLeite(LocalDate data, int numeroVacasOrdenhadas, float volume, CalendarioRecolha cr) {
		this.calendarioRecolha.set(cr);
		this.volume.set(volume);
		this.numeroVacasOrdenhadas.setValue(numeroVacasOrdenhadas);
		this.data.set(data);
	}

	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getData() {
		return DateUtil.asDate(this.data.get());
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
	public Float getVolume() {
		return this.volume.get();
	}

	public void setVolume(Float volume) {
		this.volume.set(volume);
	}

	public FloatProperty volumeProperty(){
		return volume;
	}
	@Access(AccessType.PROPERTY)
	public int getNumeroVacasOrdenhadas() {
		return (int) this.numeroVacasOrdenhadas.getValue();
	}

	public void setNumeroVacasOrdenhadas(int numeroVacasOrdenhadas) {
		this.numeroVacasOrdenhadas.setValue(numeroVacasOrdenhadas);
	}
	
	public Property<Number> numeroVacasOrdenhadasProperty(){
		return numeroVacasOrdenhadas;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="calendarioRecolha")
	public CalendarioRecolha getCalendarioRecolha() {
		return calendarioRecolha.get();
	}
	
	public void setCalendarioRecolha(CalendarioRecolha calendarioRecolha) {
		this.calendarioRecolha.set(calendarioRecolha);
	}
	
	public ObjectProperty<CalendarioRecolha> calendarioRecolhaProperty(){
		return calendarioRecolha;
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

}