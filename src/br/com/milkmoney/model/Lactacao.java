package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;


@Entity
@NamedQuery(name="Lactacao.findAll", query="SELECT l FROM Lactacao l")
public class Lactacao extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate>                  dataInicio                 = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<LocalDate>                  dataFim                    = new SimpleObjectProperty<LocalDate>();
	private StringProperty                             observacao                 = new SimpleStringProperty();
	private ObjectProperty<Animal>                     animal                     = new SimpleObjectProperty<Animal>();
	private ObjectProperty<Parto>                      parto                      = new SimpleObjectProperty<Parto>();
	private ObjectProperty<MotivoEncerramentoLactacao> motivoEncerramentoLactacao = new SimpleObjectProperty<MotivoEncerramentoLactacao>();
	@Transient
	private Float                                      mediaProducao;
	
	public Lactacao() {
	}
	
	public Lactacao(Animal animal) {
		this.animal.set(animal);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data inicio da lactação")
	public Date getDataInicio() {
		return DateUtil.asDate(this.dataInicio.get());
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
		return DateUtil.asDate(this.dataFim.get());
	}

	public void setDataFim(Date dataFim) {
		this.dataFim.set(DateUtil.asLocalDate(dataFim));
	}
	
	public ObjectProperty<LocalDate> dataFimProperty(){
		return dataFim;
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
	@ManyToOne(targetEntity=Animal.class, cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="animal")
	@FieldRequired(message="animal")
	public Animal getAnimal() {
		return animal.get();
	}
	
	public void setAnimal(Animal animal) {
		this.animal.set(animal);
	}
	
	public ObjectProperty<Animal> animalProperty(){
		return animal;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToOne(targetEntity=Parto.class, cascade={CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval=true)
	@JoinColumn(name="parto")
	@FieldRequired(message="parto")
	public Parto getParto() {
		return parto.get();
	}
	
	public void setParto(Parto parto) {
		this.parto.set(parto);
	}
	
	public ObjectProperty<Parto> partoProperty(){
		return parto;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=MotivoEncerramentoLactacao.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="motivoEncerramentoLactacao")
	public MotivoEncerramentoLactacao getMotivoEncerramentoLactacao() {
		return motivoEncerramentoLactacao.get();
	}
	
	public void setMotivoEncerramentoLactacao(MotivoEncerramentoLactacao motivoEncerramentoLactacao) {
		this.motivoEncerramentoLactacao.set(motivoEncerramentoLactacao);
	}
	
	public ObjectProperty<MotivoEncerramentoLactacao> motivoEncerramentoLactacaoProperty(){
		return motivoEncerramentoLactacao;
	}
	
	public int getDiasLactacao() {
		return (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(getDataInicio()), 
				DateUtil.asLocalDate(getDataFim() != null ? getDataFim() : new Date()));
	}
	
	public int getMesesLactacao() {
		return (int) ChronoUnit.MONTHS.between(DateUtil.asLocalDate(getDataInicio()), 
				DateUtil.asLocalDate(getDataFim() != null ? getDataFim() : new Date()));
	}
	
	public Date getDataPrevistaEncerramento(){
		if ( getDataFim() == null ){
			return DateUtil.asDate(ChronoUnit.DAYS.addTo(DateUtil.asLocalDate(getDataInicio()), 305));
		}
		
		return null;
	}
	
	@Transient
	public Float getMediaProducao() {
		return mediaProducao != null ? BigDecimal.valueOf(mediaProducao.floatValue()).setScale(2, RoundingMode.HALF_EVEN).floatValue() : BigDecimal.ZERO.floatValue();
	}

	public void setMediaProducao(Float mediaProducao) {
		this.mediaProducao = mediaProducao;
	}

	@Override
	public String toString() {
		return DateUtil.format(getDataInicio()) + " - " + (getDataFim() == null ? "Aberto" : DateUtil.format(getDataFim()));
	}
	
}