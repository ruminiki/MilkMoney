package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;


/**
 * The persistent class for the ANIMAL database table.
 * 
 */
@Entity
@Table(name="animal")
@NamedQuery(name="Animal.findAll", query="SELECT a FROM Animal a")
//,hints={ @javax.persistence.QueryHint(name = "org.hibernate.cacheable", value = "false") })
public class Animal extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> dataNascimento           = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private StringProperty            nome                     = new SimpleStringProperty();
	private StringProperty            numero                   = new SimpleStringProperty();
	private StringProperty            sexo                     = new SimpleStringProperty(Sexo.FEMEA);
	private StringProperty            finalidadeAnimal         = new SimpleStringProperty(FinalidadeAnimal.PRODUCAO_LEITE);
	
	private ObjectProperty<Raca>      raca                     = new SimpleObjectProperty<Raca>();
	
	private ObjectProperty<Animal>    mae                      = new SimpleObjectProperty<Animal>();
	private ObjectProperty<Animal>    paiMontaNatural          = new SimpleObjectProperty<Animal>();
	private ObjectProperty<Touro>     paiEnseminacaoArtificial = new SimpleObjectProperty<Touro>();
	
	private StringProperty            peso                     = new SimpleStringProperty();
	private StringProperty            valor                    = new SimpleStringProperty();
	
	@Formula("(SELECT s.situacao FROM viewSituacaoAnimal s WHERE s.animal = id limit 1)")
	private String situacaoAnimal;
	
	@Formula("(SELECT MAX(p.data) FROM parto p inner join cobertura c ON (c.parto = p.id) "
			+ "INNER JOIN animal a on (a.id = c.femea) WHERE a.id = id)")
	private Date dataUltimoParto;

	@Formula("(SELECT MAX(c.data) FROM cobertura c WHERE c.femea = id)")
	private Date dataUltimaCobertura;
	
	@Formula("(SELECT MAX(c.previsaoParto) FROM cobertura c WHERE c.femea = id)")
	private Date dataPrevisaoProximoParto;
	
	@Formula("(SELECT c.situacaoCobertura FROM cobertura c WHERE c.femea = id order by c.data desc limit 1)")
	private String situacaoUltimaCobertura;
	
	@Formula("(SELECT (c.id > 0) FROM cria c WHERE c.animal = id LIMIT 1)")
	private Boolean nascimentoCadastrado = false;

	public Animal() {}

	public Animal(String sexo) {
		this.sexo.set(sexo);
	}

	public Animal(String sexo, String finalidadeAnimal) {
		this.sexo.set(sexo);
		this.finalidadeAnimal.set(finalidadeAnimal);
	}

	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data de nascimento")
	public Date getDataNascimento() {
		return DateUtil.asDate(this.dataNascimento.get());
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento.set(DateUtil.asLocalDate(dataNascimento));
	}
	
	public ObjectProperty<LocalDate> dataNascimentoProperty(){
		return dataNascimento;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="nome")
	public String getNome() {
		return this.nome.get();
	}

	public void setNome(String nome) {
		this.nome.set(nome);
	}

	public StringProperty nomeProperty(){
		return nome;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="número")
	public String getNumero() {
		return this.numero.get();
	}

	public void setNumero(String numero) {
		this.numero.set(numero);
	}
	
	public StringProperty numeroProperty(){
		return numero;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="finalidade do animal")
	public String getFinalidadeAnimal() {
		return finalidadeAnimal.get();
	}
	
	public void setFinalidadeAnimal(String finalidadeAnimal) {
		this.finalidadeAnimal.set(finalidadeAnimal);
	}
	
	public StringProperty finalidadeAnimalProperty(){
		return finalidadeAnimal;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="sexo")
	public String getSexo() {
		return this.sexo.get();
	}

	public void setSexo(String sexo) {
		this.sexo.set(sexo);
	}
	
	public StringProperty sexoProperty(){
		return sexo;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Raca.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="raca")
	@FieldRequired(message="raça")
	public Raca getRaca() {
		return raca.get();
	}
	
	public void setRaca(Raca raca) {
		this.raca.set(raca);
	}
	
	public ObjectProperty<Raca> racaProperty(){
		return raca;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Animal.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="mae")
	public Animal getMae() {
		return mae.get();
	}
	
	public void setMae(Animal mae) {
		this.mae.set(mae);
	}
	
	public ObjectProperty<Animal> maeProperty(){
		return mae;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Animal.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="paiMontaNatural")
	public Animal getPaiMontaNatural() {
		return paiMontaNatural.get();
	}
	
	public void setPaiMontaNatural(Animal paiMontaNatural) {
		this.paiMontaNatural.set(paiMontaNatural);
	}
	
	public ObjectProperty<Animal> paiMontaNaturalProperty(){
		return paiMontaNatural;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Touro.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="paiEnseminacaoArtificial")
	public Touro getPaiEnseminacaoArtificial() {
		return this.paiEnseminacaoArtificial.get();
	}

	public void setPaiEnseminacaoArtificial(Touro paiEnseminacaoArtificial) {
		this.paiEnseminacaoArtificial.set(paiEnseminacaoArtificial);
	}
	
	public ObjectProperty<Touro> paiEnseminacaoArtificialProperty(){
		return paiEnseminacaoArtificial;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getPeso() {
		return NumberFormatUtil.fromString(this.peso.get());
	}

	public void setPeso(BigDecimal peso) {
		this.peso.set(NumberFormatUtil.decimalFormat(peso));
	}

	public StringProperty pesoProperty(){
		return peso;
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
	
	@Transient
	public String getSituacaoAnimal() {
		
		if ( situacaoAnimal == null ){
			return SituacaoAnimal.NAO_DEFINIDA;
		}
		return this.situacaoAnimal;
	}

	public void setSituacaoAnimal(String situacaoAnimal) {
		this.situacaoAnimal = situacaoAnimal;
	}
	
	//==========================
	public String getNumeroNome(){
		return this.numero.get() + "-" + this.nome.get();
	}
	
	public StringProperty numeroNomeProperty(){
		return new SimpleStringProperty(numero.get() + "-" + this.nome.get());
	}
	
	public long getIdade() {
		
		if ( this.dataNascimento != null && this.dataNascimento.get() != null )
			return ChronoUnit.MONTHS.between(this.dataNascimento.get(), LocalDate.now());
		return 0;
		
	}
	
	@Transient
	public String getDiasUltimoParto() {
		if ( getDataUltimoParto() != null )
			return String.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(getDataUltimoParto()), LocalDate.now()));
		return "--";
	}

	@Transient
	public Date getDataUltimoParto() {
		return dataUltimoParto;
	}

	public void setDataUltimoParto(Date dataUltimoParto) {
		this.dataUltimoParto = dataUltimoParto;
	}

	@Transient
	public Date getDataUltimaCobertura() {
		return dataUltimaCobertura;
	}

	public void setDataUltimaCobertura(Date dataUltimaCobertura) {
		this.dataUltimaCobertura = dataUltimaCobertura;
	}

	@Transient
	public Date getDataPrevisaoProximoParto() {
		return dataPrevisaoProximoParto;
	}

	public void setDataPrevisaoProximoParto(Date dataPrevisaoProximoParto) {
		this.dataPrevisaoProximoParto = dataPrevisaoProximoParto;
	}
	
	@Transient
	public Date getDataPrevisaoEncerramentoLactacao() {
		
		if ( dataUltimoParto != null ){
			return DateUtil.asDate(DateUtil.asLocalDate(dataUltimoParto).plusMonths(7));
		}
		return null;
	}

	@Transient
	public String getSituacaoUltimaCobertura() {
		if ( situacaoUltimaCobertura == null || situacaoUltimaCobertura.isEmpty() )
			return "--";
		return situacaoUltimaCobertura;
	}

	public void setSituacaoUltimaCobertura(String situacaoUltimaCobertura) {
		this.situacaoUltimaCobertura = situacaoUltimaCobertura;
	}

	@Transient
	public String getDiasUltimaCobertura() {
		if ( getDataUltimaCobertura() != null )
			return String.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(getDataUltimaCobertura()), LocalDate.now()));
		return "--";
	}
	
	@Transient
	public Boolean isNascimentoCadastrado() {
		return nascimentoCadastrado != null ? nascimentoCadastrado : false;
	}

	public void setNascimentoCadastrado(boolean nascimentoCadastrado) {
		this.nascimentoCadastrado = nascimentoCadastrado;
	}
	
	@Override
	public String toString() {
		return getNumeroNome();
	}

}