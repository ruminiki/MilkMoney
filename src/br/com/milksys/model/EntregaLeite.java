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

import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;


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
	
	private StringProperty data = new SimpleStringProperty();  
	private StringProperty numeroVacasOrdenhadas = new SimpleStringProperty();
	private StringProperty volume = new SimpleStringProperty();
	private ObjectProperty<CalendarioRecolha> calendarioRecolha = new SimpleObjectProperty<CalendarioRecolha>();
	private StringProperty observacao = new SimpleStringProperty();
	private StringProperty mediaProducao = new SimpleStringProperty();

	public EntregaLeite() {
	}
	
	public EntregaLeite(LocalDate data, int numeroVacasOrdenhadas, float volume, float media, CalendarioRecolha cr) {
		this.calendarioRecolha.set(cr);
		this.volume.set(String.valueOf(volume));
		this.mediaProducao.set(String.valueOf(media));
		this.numeroVacasOrdenhadas.setValue(String.valueOf(numeroVacasOrdenhadas));
		this.data.set(DateUtil.format(data));
	}

	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getData() {
		return DateUtil.asDate(DateUtil.parse(data.get()));
	}

	public void setData(Date data) {
		this.data.set(DateUtil.format(data));
	}
	
	public StringProperty dataProperty(){
		return data;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Access(AccessType.PROPERTY)
	public BigDecimal getVolume() {
		return NumberFormatUtil.fromString(this.volume.get());
	}

	public void setVolume(BigDecimal volume) {
		this.volume.set(NumberFormatUtil.decimalFormat(volume));
	}

	public StringProperty volumeProperty(){
		return volume;
	}
	@Access(AccessType.PROPERTY)
	public int getNumeroVacasOrdenhadas() {
		return Integer.parseInt(numeroVacasOrdenhadas.getValue());
	}

	public void setNumeroVacasOrdenhadas(int numeroVacasOrdenhadas) {
		this.numeroVacasOrdenhadas.setValue(String.valueOf(numeroVacasOrdenhadas));
	}
	
	public StringProperty numeroVacasOrdenhadasProperty(){
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
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getMediaProducao() {
		return NumberFormatUtil.fromString(this.mediaProducao.get());
	}

	public void setMediaProducao(BigDecimal mediaProducao) {
		this.mediaProducao.set(NumberFormatUtil.decimalFormat(mediaProducao));
	}
	
	public StringProperty mediaProducaoProperty(){
		return mediaProducao;
	}
	
}