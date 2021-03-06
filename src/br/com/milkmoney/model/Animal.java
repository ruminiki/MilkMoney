package br.com.milkmoney.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

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
	private StringProperty            imagem                   = new SimpleStringProperty();
	private StringProperty            observacao               = new SimpleStringProperty();
	
	private ObjectProperty<Lote>      lote                     = new SimpleObjectProperty<Lote>();
	
	@Formula("(SELECT s.situacao FROM viewSituacaoAnimal s WHERE s.animal = id limit 1)")
	private String situacaoAnimal;
	
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
	@FieldRequired(message="n�mero")
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
	public String getImagem() {
		return this.imagem.get();
	}

	public void setImagem(String imagem) {
		this.imagem.set(imagem);
	}
	
	public StringProperty imagemProperty(){
		return imagem;
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
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=Raca.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="raca")
	@FieldRequired(message="ra�a")
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
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=Animal.class, cascade=CascadeType.REFRESH)
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
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=Animal.class, cascade=CascadeType.REFRESH)
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
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=Touro.class, cascade=CascadeType.REFRESH)
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
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=Lote.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="lote")
	public Lote getLote() {
		return this.lote.get();
	}

	public void setLote(Lote lote) {
		this.lote.set(lote);
	}
	
	public ObjectProperty<Lote> loteProperty(){
		return lote;
	}
	
	//==========================
	public String getNumeroNome(){
		return this.numero.get() + "-" + this.nome.get();
	}
	
	public String getSexoFormatado(){
		return this.getSexo().substring(0, 1);
	}
	
	public String getLoteFormatado(){
		if ( getLote() == null ){
			return "--";
		}else{
			return getLote().getDescricao();
		}
	}
	
	public StringProperty numeroNomeProperty(){
		return new SimpleStringProperty(numero.get() + "-" + this.nome.get());
	}
	
	public int getIdade() {
		
		if ( this.dataNascimento != null && this.dataNascimento.get() != null )
			return (int)ChronoUnit.MONTHS.between(this.dataNascimento.get(), LocalDate.now());
		return 0;
		
	}
	
	@Transient
	public Boolean isNascimentoCadastrado() {
		return nascimentoCadastrado != null ? nascimentoCadastrado : false;
	}

	public void setNascimentoCadastrado(boolean nascimentoCadastrado) {
		this.nascimentoCadastrado = nascimentoCadastrado;
	}
	
	@Transient
	public String getSituacaoUltimaCobertura() {
		return situacaoUltimaCobertura == null ? "--" : situacaoUltimaCobertura;
	}

	public void setSituacaoUltimaCobertura(String situacaoUltimaCobertura) {
		this.situacaoUltimaCobertura = situacaoUltimaCobertura;
	}
	
	public Image getImage(){
		try {
			if ( this.getImagem() == null || this.getImagem().isEmpty() ){
				return new Image(ClassLoader.getSystemResourceAsStream("img/cow-256.png"));
			}
			return new Image(new FileInputStream(getImagem()));
		} catch (FileNotFoundException e) {
			System.out.println("Erro ao carregar imagem do animal.");
		}
		return null;
	}
	
	@Transient
	public boolean situacaoPermiteParto(){
		if ( getSituacaoAnimal() != null && getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO) ){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getNumeroNome();
	}

}