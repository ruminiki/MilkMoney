package br.com.milkmoney.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;


@Entity
@Table(name="procedimento")
@NamedQuery(name="Procedimento.findAll", query="SELECT a FROM Procedimento a")
public class Procedimento extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty                   descricao        = new SimpleStringProperty();
	private ObjectProperty<LocalDate>        dataAgendada     = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<LocalDate>        dataRealizacao   = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private ObjectProperty<TipoProcedimento> tipoProcedimento = new SimpleObjectProperty<TipoProcedimento>();
	private ObjectProperty<Servico>          servico          = new SimpleObjectProperty<Servico>();
	private StringProperty                   responsavel      = new SimpleStringProperty();
	private StringProperty                   observacao       = new SimpleStringProperty();
	private List<Animal>                     animais          = new ArrayList<Animal>();
	
	public Procedimento() {
	}
	
	public Procedimento(TipoProcedimento tipoProcedimento) {
		this.tipoProcedimento.set(tipoProcedimento);
	}

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
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data agendada")
	public Date getDataAgendada() {
		return DateUtil.asDate(this.dataAgendada.get());
	}
	
	public void setDataAgendada(Date dataAgendada) {
		this.dataAgendada.set(DateUtil.asLocalDate(dataAgendada));
	}
	
	public ObjectProperty<LocalDate> dataAgendadaProperty(){
		return dataAgendada;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data realização")
	public Date getDataRealizacao() {
		return DateUtil.asDate(this.dataRealizacao.get());
	}
	
	public void setDataRealizacao(Date dataRealizacao) {
		this.dataRealizacao.set(DateUtil.asLocalDate(dataRealizacao));
	}
	
	public ObjectProperty<LocalDate> dataRealizacaoProperty(){
		return dataRealizacao;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=TipoProcedimento.class)
	@JoinColumn(name="tipoProcedimento")
	@FieldRequired(message="finalidade")
	public TipoProcedimento getTipoProcedimento() {
		return tipoProcedimento.get();
	}
	
	public void setTipoProcedimento(TipoProcedimento tipoProcedimento) {
		this.tipoProcedimento.set(tipoProcedimento);
	}
	
	public ObjectProperty<TipoProcedimento> tipoProcedimentoProperty(){
		return tipoProcedimento;
	}
	
	
	@Access(AccessType.PROPERTY)
	public String getResponsavel() {
		return this.responsavel.get();
	}

	public void setResponsavel(String responsavel) {
		this.responsavel.set(responsavel);
	}

	public StringProperty responsavelProperty(){
		return responsavel;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToOne(orphanRemoval=true, targetEntity=Servico.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="servico")
	public Servico getServico() {
		return servico.get();
	}
	
	public void setServico(Servico servico) {
		this.servico.set(servico);
	}
	
	public ObjectProperty<Servico> servicoProperty(){
		return servico;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToMany
	@JoinTable(name="procedimentoAnimal", joinColumns={@JoinColumn(name="procedimento", referencedColumnName="id")},
	      inverseJoinColumns={@JoinColumn(name="animal", referencedColumnName="id")})
	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}
	
	@Access(AccessType.PROPERTY)
	public String getObservacao() {
		return this.observacao.get();
	}

	public void setObservacao(String descricao) {
		this.observacao.set(descricao);
	}

	public StringProperty observacaoProperty(){
		return observacao;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
	
}