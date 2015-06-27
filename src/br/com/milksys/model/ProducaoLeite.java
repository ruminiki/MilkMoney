package br.com.milksys.model;

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
@Table(name="producaoLeite")
@NamedQuery(name="ProducaoLeite.findAll", query="SELECT e FROM ProducaoLeite e")
public class ProducaoLeite implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> data = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private StringProperty numeroVacasOrdenhadas = new SimpleStringProperty();
	private StringProperty volumeProduzido = new SimpleStringProperty();
	private StringProperty volumeEntregue = new SimpleStringProperty();
	private StringProperty mediaProducao = new SimpleStringProperty();
	private StringProperty observacao = new SimpleStringProperty();
	@Transient
	private BigDecimal valor;

	public ProducaoLeite() {
		// TODO Auto-generated constructor stub
	}
	
	public ProducaoLeite(LocalDate data, int numeroVacasOrdenhadas, BigDecimal volumeProduzido, 
			BigDecimal volumeEntregue, BigDecimal mediaProducao) {
		this.data.set(data);
		this.numeroVacasOrdenhadas.set(String.valueOf(numeroVacasOrdenhadas));
		this.volumeProduzido.set(NumberFormatUtil.decimalFormat(volumeProduzido));
		this.volumeEntregue.set(NumberFormatUtil.decimalFormat(volumeEntregue));
		this.mediaProducao.set(NumberFormatUtil.decimalFormat(mediaProducao));
	}
	
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
	
	@Transient
	public BigDecimal getValor() {
		return this.valor == null ? BigDecimal.ZERO : this.valor;
	}
	
	public void setValor(BigDecimal valor){
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
	
	
}