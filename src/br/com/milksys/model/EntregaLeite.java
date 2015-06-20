package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
	private IntegerProperty anoReferencia = new SimpleIntegerProperty();
	private StringProperty dataInicio = new SimpleStringProperty();  
	private StringProperty dataFim = new SimpleStringProperty();  
	private StringProperty volume = new SimpleStringProperty();
	private StringProperty valorRecebido = new SimpleStringProperty();
	private StringProperty valorMaximoPraticado = new SimpleStringProperty();
	private StringProperty observacao = new SimpleStringProperty();
	

	public EntregaLeite() {
		
	}
	
	public EntregaLeite(String mesReferencia, int anoReferencia) {
		this.mesReferencia.set(mesReferencia);
		this.anoReferencia.set(anoReferencia);
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
		return DateUtil.asDate(DateUtil.parse(dataInicio.get()));
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio.set(DateUtil.format(dataInicio));
	}
	
	public StringProperty dataInicioProperty(){
		return dataInicio;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataFim() {
		return DateUtil.asDate(DateUtil.parse(dataFim.get()));
	}

	public void setDataFim(Date dataInicio) {
		this.dataFim.set(DateUtil.format(dataInicio));
	}
	
	public StringProperty dataFimProperty(){
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
		return this.anoReferencia.get();
	}

	public void setAnoReferencia(int value) {
		this.anoReferencia.set(value);
	}
	
	public IntegerProperty anoReferenciaProperty(){
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
	public BigDecimal getValorRecebido() {
		return NumberFormatUtil.fromString(this.valorRecebido.get());
	}

	public void setValorRecebido(BigDecimal valorRecebido) {
		this.valorRecebido.set(NumberFormatUtil.decimalFormat(valorRecebido));
	}
	
	public StringProperty valorRecebidoProperty(){
		return valorRecebido;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getValorMaximoPraticado() {
		return NumberFormatUtil.fromString(this.valorMaximoPraticado.get());
	}

	public void setValorMaximoPraticado(BigDecimal valorMaximoPraticado) {
		this.valorMaximoPraticado.set(NumberFormatUtil.decimalFormat(valorMaximoPraticado));
	}
	
	public StringProperty valorMaximoPraticadoProperty(){
		return valorMaximoPraticado;
	}
	
}