package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.persistence.Transient;

import br.com.milksys.util.NumberFormatUtil;


/**
 * The persistent class for the precoleite database table.
 * 
 */
@Entity
@Table(name="precoLeite")
@NamedQuery(name="PrecoLeite.findAll", query="SELECT p FROM PrecoLeite p")
public class PrecoLeite extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty valorMaximoPraticado = new SimpleStringProperty();
	private StringProperty valorRecebido = new SimpleStringProperty();
	private StringProperty mesReferencia = new SimpleStringProperty();
	private StringProperty anoReferencia = new SimpleStringProperty();
	private int codigoMes;

	public PrecoLeite() {
	}

	public PrecoLeite(String mesReferencia, int codigoMes, int anoReferencia, BigDecimal valorMaximoPraticado, BigDecimal valorRecebido) {
		this.mesReferencia.set(mesReferencia);
		this.codigoMes = codigoMes;
		this.anoReferencia.set(String.valueOf(anoReferencia));
		this.valorMaximoPraticado.set(NumberFormatUtil.decimalFormat(valorMaximoPraticado));
		this.valorRecebido.set(NumberFormatUtil.decimalFormat(valorRecebido));
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getCodigoMes() {
		return codigoMes;
	}

	public void setCodigoMes(int codigoMes) {
		this.codigoMes = codigoMes;
	}

	/**
	 * Centraliza em um metodo a verificação dos valores do preco de leite.
	 * a preferencia em considerar primeiro o valor recebido.
	 * @return
	 */
	@Transient
	public BigDecimal getValor(){
		if ( getValorRecebido().compareTo(BigDecimal.ZERO) > 0 )
			return getValorRecebido();
		return getValorMaximoPraticado();
	}
}