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
	
	private StringProperty mesReferencia = new SimpleStringProperty();
	private StringProperty anoReferencia = new SimpleStringProperty();
	private ObjectProperty<LocalDate> dataInicio = new SimpleObjectProperty<LocalDate>();  
	private ObjectProperty<LocalDate> dataFim = new SimpleObjectProperty<LocalDate>();  
	private StringProperty volume = new SimpleStringProperty();
	private StringProperty observacao = new SimpleStringProperty();
	private ObjectProperty<PrecoLeite> precoLeite = new SimpleObjectProperty<PrecoLeite>();
	

	public EntregaLeite() {
		
	}
	
	public EntregaLeite(String mesReferencia, int anoReferencia, 
			BigDecimal volume, PrecoLeite precoLeite) {
		this.mesReferencia.set(mesReferencia);
		this.anoReferencia.set(String.valueOf(anoReferencia));
		this.volume.set(String.valueOf(volume));
		this.precoLeite.set(precoLeite);
		
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataInicio() {
		if ( dataInicio != null && dataInicio.get() != null )
			return DateUtil.asDate(dataInicio.get());
		return null;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio.set(DateUtil.asLocalDate(dataInicio));
	}
	
	public ObjectProperty<LocalDate> dataInicioProperty(){
		return dataInicio;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataFim() {
		if ( dataFim != null && dataFim.get() != null )
			return DateUtil.asDate(dataFim.get());	
		return null;
	}

	public void setDataFim(Date dataInicio) {
		this.dataFim.set(DateUtil.asLocalDate(dataInicio));
	}
	
	public ObjectProperty<LocalDate> dataFimProperty(){
		return dataFim;
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
	public String getMesReferencia() {
		return this.mesReferencia.get();
	}

	public void setMesReferencia(String value) {
		this.mesReferencia.set(value);
	}
	
	public StringProperty mesReferenciaProperty(){
		return mesReferencia;
	}

	@Access(AccessType.PROPERTY)
	public int getAnoReferencia() {
		return Integer.parseInt(this.anoReferencia.get());
	}

	public void setAnoReferencia(int value) {
		this.anoReferencia.set(String.valueOf(value));
	}
	
	public StringProperty anoReferenciaProperty(){
		return anoReferencia;
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

	@Transient
	public BigDecimal getValorTotal() {
		if ( getPrecoLeite() != null ){
			return getPrecoLeite().getValorRecebido().multiply(getVolume());
		}
		return BigDecimal.ZERO;
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

	public BigDecimal getValorMaximoPraticado() {
		if ( getPrecoLeite() != null ){
			return getPrecoLeite().getValorMaximoPraticado();
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getValorRecebido() {
		if ( getPrecoLeite() != null ){
			return getPrecoLeite().getValorRecebido();
		}
		return BigDecimal.ZERO;
	}
	
	
}