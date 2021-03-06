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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;


/**
 * The persistent class for the ANIMAL database table.
 * 
 */
@Entity
@Table(name="entregaLeite")
@NamedQuery(name="EntregaLeite.findAll", query="SELECT e FROM EntregaLeite e")
public class EntregaLeite extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private StringProperty mesReferencia = new SimpleStringProperty();
	private StringProperty anoReferencia = new SimpleStringProperty();
	private ObjectProperty<LocalDate> dataInicio = new SimpleObjectProperty<LocalDate>();  
	private ObjectProperty<LocalDate> dataFim = new SimpleObjectProperty<LocalDate>();  
	private StringProperty volume = new SimpleStringProperty();
	private StringProperty observacao = new SimpleStringProperty();
	private StringProperty carregaMarcacoesMes = new SimpleStringProperty(SimNao.SIM);
	
	@Formula("(SELECT p.valorRecebido FROM precoLeite p WHERE p.mesReferencia = mesReferencia and p.anoReferencia = anoReferencia limit 1)")
	private BigDecimal valorRecebido;
	
	@Formula("(SELECT p.valorMaximoPraticado FROM precoLeite p WHERE p.mesReferencia = mesReferencia and p.anoReferencia = anoReferencia limit 1)")
	private BigDecimal valorMaximoPraticado;
	
	public EntregaLeite() {
	}
	
	public EntregaLeite(String mesReferencia, int anoReferencia, BigDecimal volume) {
		this.mesReferencia.set(mesReferencia);
		this.anoReferencia.set(String.valueOf(anoReferencia));
		this.volume.set(String.valueOf(volume));
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
		return NumberFormatUtil.intFromString(this.volume.get());
	}

	public void setVolume(BigDecimal volume) {
		this.volume.set(NumberFormatUtil.decimalFormat(volume, 0));
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

	@Access(AccessType.PROPERTY)
	@FieldRequired(message="carrega marca��es")
	public String getCarregaMarcacoesMes() {
		return this.carregaMarcacoesMes.get();
	}

	public void setCarregaMarcacoesMes(String carregaMarcacoesMes) {
		this.carregaMarcacoesMes.set(carregaMarcacoesMes);
	}
	
	public StringProperty carregaMarcacoesMesProperty(){
		return carregaMarcacoesMes;
	}
	
	@Transient
	public BigDecimal getValorTotal() {
		if ( valorRecebido != null ){
			return valorRecebido.multiply(getVolume());
		}
		return BigDecimal.ZERO;
	}
	
	@Transient	
	public BigDecimal getValorMaximoPraticado() {
		if ( valorMaximoPraticado != null ){
			return valorMaximoPraticado;
		}
		return BigDecimal.ZERO;
	}
	
	@Transient
	public BigDecimal getValorRecebido() {
		if ( valorRecebido != null ){
			return valorRecebido;
		}
		return BigDecimal.ZERO;
	}
	
}