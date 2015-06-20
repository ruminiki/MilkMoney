package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@Table(name="producaoLeite")
@NamedQuery(name="ProducaoLeite.findAll", query="SELECT e FROM ProducaoLeite e")
public class ProducaoLeite implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private StringProperty data = new SimpleStringProperty();  
	private StringProperty numeroVacasOrdenhadas = new SimpleStringProperty();
	private StringProperty volumeProduzido = new SimpleStringProperty();
	private StringProperty volumeEntregue = new SimpleStringProperty();
	private StringProperty valorEstimado = new SimpleStringProperty();
	private StringProperty mediaProducao = new SimpleStringProperty();
	private StringProperty observacao = new SimpleStringProperty();

	public ProducaoLeite() {
		// TODO Auto-generated constructor stub
	}
	
	public ProducaoLeite(Date data, int numeroVacasOrdenhadas, BigDecimal volumeProduzido, 
			BigDecimal volumeEntregue, BigDecimal valorEstimado, BigDecimal mediaProducao) {
		this.data.set(DateUtil.format(data));
		this.numeroVacasOrdenhadas.set(String.valueOf(numeroVacasOrdenhadas));
		this.volumeProduzido.set(NumberFormatUtil.decimalFormat(volumeProduzido));
		this.volumeEntregue.set(NumberFormatUtil.decimalFormat(volumeEntregue));
		this.valorEstimado.set(NumberFormatUtil.decimalFormat(valorEstimado));
		this.mediaProducao.set(NumberFormatUtil.decimalFormat(mediaProducao));
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
	public BigDecimal getVolumeProduzido() {
		return NumberFormatUtil.fromString(this.volumeProduzido.get());
	}

	public void setVolumeProduzido(BigDecimal volumeProduzido) {
		this.volumeProduzido.set(NumberFormatUtil.decimalFormat(volumeProduzido));
	}

	public StringProperty volumeProduzidoProperty(){
		return volumeProduzido;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getVolumeEntregue() {
		return NumberFormatUtil.fromString(this.volumeEntregue.get());
	}

	public void setVolumeEntregue(BigDecimal volumeEntregue) {
		this.volumeEntregue.set(NumberFormatUtil.decimalFormat(volumeEntregue));
	}

	public StringProperty volumeEntregueProperty(){
		return volumeEntregue;
	}

	@Access(AccessType.PROPERTY)
	public BigDecimal getValorEstimado() {
		return NumberFormatUtil.fromString(this.valorEstimado.get());
	}

	public void setValorEstimado(BigDecimal valorEstimado) {
		this.valorEstimado.set(NumberFormatUtil.decimalFormat(valorEstimado));
	}

	public StringProperty ValorEstimadoProperty(){
		return valorEstimado;
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