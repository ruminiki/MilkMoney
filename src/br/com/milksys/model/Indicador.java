package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


@Entity
@Table(name="indicador")
@NamedQuery(name="Indicador.findAll", query="SELECT r FROM Indicador r order by r.ordem")
public class Indicador extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String descricao;
	private String sigla;
	private BigDecimal valorReferencia;
	
	@Type(type="text")
	private String query;
	private BigDecimal valorApurado;
	private int ordem;
	
	public Indicador() {
	}
	
	public Indicador(String descricao, String sigla, BigDecimal valor) {
		this.descricao = descricao;
		this.sigla = sigla;
		this.valorApurado = valor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public BigDecimal getValorReferencia() {
		return valorReferencia;
	}

	public void setValorReferencia(BigDecimal valorReferencia) {
		this.valorReferencia = valorReferencia;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public BigDecimal getValorApurado() {
		return valorApurado;
	}

	public void setValorApurado(BigDecimal valorApurado) {
		this.valorApurado = valorApurado;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
	
}