package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.com.milksys.components.FieldRequired;


@Entity
@Table(name="indicador")
@NamedQuery(name="Indicador.findAll", query="SELECT r FROM Indicador r order by r.ordem")
public class Indicador extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String descricao;
	private String sigla;
	private String valorReferencia;
	@Type(type="text")
	private String definicao;
	private String classeCalculo;
	private String valorApurado;
	@Transient
	private BigDecimal resultado;
	private int ordem;
	
	public Indicador() {
	}
	
	public Indicador(String descricao, String sigla, String valor) {
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

	@FieldRequired(message="descrição")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@FieldRequired(message="sigla")
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@FieldRequired(message="valor de referência")
	public String getValorReferencia() {
		return valorReferencia;
	}

	public void setValorReferencia(String valorReferencia) {
		this.valorReferencia = valorReferencia;
	}

	@FieldRequired(message="classe de cálculo")
	public String getClasseCalculo() {
		return classeCalculo;
	}

	public void setClasseCalculo(String classeCalculo) {
		this.classeCalculo = classeCalculo;
	}

	public String getValorApurado() {
		return valorApurado;
	}

	public void setValorApurado(String valorApurado) {
		this.valorApurado = valorApurado;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	
	public String getDefinicao() {
		return definicao;
	}

	public void setDefinicao(String definicao) {
		this.definicao = definicao;
	}
	
	public BigDecimal getResultado() {
		
		BigDecimal valorApurado    = BigDecimal.valueOf(Double.parseDouble(getValorApurado()));
		BigDecimal valorReferencia = BigDecimal.valueOf(Double.parseDouble(getValorReferencia()));
		BigDecimal resultado = BigDecimal.ZERO;
		if ( valorApurado.compareTo(resultado) > 0 && valorReferencia.compareTo(resultado) > 0 ){
			resultado = valorApurado.multiply(BigDecimal.valueOf(100)).divide(valorReferencia, 2, RoundingMode.HALF_EVEN);
		}
		return resultado;
		
	}

	public void setResultado(BigDecimal resultado) {
		this.resultado = resultado;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
	
}