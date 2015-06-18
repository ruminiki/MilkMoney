package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.com.milksys.util.NumberFormatUtil;


/**
 * The persistent class for the precoleite database table.
 * 
 */
@Entity
@Table(name="precoLeite")
@NamedQuery(name="PrecoLeite.findAll", query="SELECT p FROM PrecoLeite p")
public class PrecoLeite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty valor = new SimpleStringProperty();
	private StringProperty mesReferencia = new SimpleStringProperty();
	private IntegerProperty anoReferencia = new SimpleIntegerProperty();

	public PrecoLeite() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getValor() {
		return NumberFormatUtil.fromString(this.valor.get());
	}

	public void setValor(BigDecimal valor) {
		this.valor.set(NumberFormatUtil.decimalFormat(valor));
	}
	
	public StringProperty valorProperty(){
		return valor;
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
	
}