package br.com.milkmoney.model;

import java.io.Serializable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.milkmoney.components.FieldRequired;


@Entity
@Table(name="insumo")
@NamedQuery(name="Insumo.findAll", query="SELECT l FROM Insumo l")
public class Insumo extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private StringProperty                descricao     = new SimpleStringProperty();
	private ObjectProperty<TipoInsumo>    tipoInsumo    = new SimpleObjectProperty<TipoInsumo>();
	private ObjectProperty<UnidadeMedida> unidadeMedida = new SimpleObjectProperty<UnidadeMedida>();
	private StringProperty                observacao    = new SimpleStringProperty();

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="descrição")
	public String getDescricao() {
		return this.descricao.get();
	}

	public void setDescricao(String descricao) {
		this.descricao.set(descricao);
	}
	
	public StringProperty descricaoProperty(){
		return descricao;
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
	@ManyToOne(targetEntity=TipoInsumo.class, cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="tipoInsumo")
	public TipoInsumo getTipoInsumo() {
		return tipoInsumo.get();
	}
	
	public void setTipoInsumo(TipoInsumo tipoInsumo) {
		this.tipoInsumo.set(tipoInsumo);
	}
	
	public ObjectProperty<TipoInsumo> tipoInsumoProperty(){
		return tipoInsumo;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=UnidadeMedida.class, cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="unidadeMedida")
	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida.get();
	}
	
	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida.set(unidadeMedida);
	}
	
	public ObjectProperty<UnidadeMedida> unidadeMedidaProperty(){
		return unidadeMedida;
	}
	
	@Override
	public String toString() {
		return getDescricao();
	}
	
}