package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="fichaAnimal")
@NamedQuery(name="FichaAnimal.findAll", query="SELECT a FROM FichaAnimal a")
public class FichaAnimal extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne(targetEntity=Animal.class, orphanRemoval=true, cascade={CascadeType.MERGE})
	@JoinColumn(name="animal")
	private Animal     animal;
	private Date       dataUltimaCobertura;
	private int        numeroServicosAtePrenhez;
	private Date       proximoServico;
	private Date       dataProximoParto;
	private Date       dataUltimoParto;
	private int        numeroPartos;
	private int        numeroLactacoes;
	private String     lote;
	private String     ultimoTratamento;
	private BigDecimal mediaProducao;
	private int        numeroCriasMacho;
	private int        numeroCriasFemea;
	private int        diasEmAberto;
	private int        diasEmLactacao;
	private int        intervaloEntrePartos;
	private int        idadePrimeiroParto;
	private int        idadePrimeiraCobertura;
	private String     situacaoUltimaCobertura;
	private Date       dataPrevisaoEncerramentoLactacao;
	private BigDecimal eficienciaReprodutiva;
	private int        mesesProduzindo;
	private int        diasGestacaoPeriodo;
	private int        diasVacaExcessoPeriodo;
	private BigDecimal mesesProducaoIdeal;
	private BigDecimal eficienciaTempoProducao;
	@Transient
	private Date       data;
	@Transient
	private String     evento;

	public FichaAnimal() {
	}
	
	public FichaAnimal(Date data, String evento) {
		this.data = data;
		this.evento = evento;
	}
	
	public FichaAnimal(Animal animal) {
		this.animal = animal;
	}
	
	@Override
	public int getId() {
		return animal != null ? animal.getId() : 0;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public Date getDataUltimaCobertura() {
		return dataUltimaCobertura;
	}

	public void setDataUltimaCobertura(Date dataUltimaCobertura) {
		this.dataUltimaCobertura = dataUltimaCobertura;
	}

	public int getNumeroServicosAtePrenhez() {
		return numeroServicosAtePrenhez;
	}

	public void setNumeroServicosAtePrenhez(int numeroServicosAtePrenhez) {
		this.numeroServicosAtePrenhez = numeroServicosAtePrenhez;
	}

	public Date getProximoServico() {
		return proximoServico;
	}

	public void setProximoServico(Date proximoServico) {
		this.proximoServico = proximoServico;
	}

	public int getNumeroPartos() {
		return numeroPartos;
	}

	public void setNumeroPartos(int numeroPartos) {
		this.numeroPartos = numeroPartos;
	}

	public int getNumeroCriasMacho() {
		return numeroCriasMacho;
	}

	public void setNumeroCriasMacho(int numeroCriasMacho) {
		this.numeroCriasMacho = numeroCriasMacho;
	}
	
	@OneToOne
	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public int getNumeroCriasFemea() {
		return numeroCriasFemea;
	}

	public void setNumeroCriasFemea(int numeroCriasFemea) {
		this.numeroCriasFemea = numeroCriasFemea;
	}

	public String getDiasEmAberto() {
		return String.valueOf(diasEmAberto + "d - " + BigDecimal.valueOf(diasEmAberto).divide(BigDecimal.valueOf(30), 1, RoundingMode.HALF_EVEN) + "m");
	}

	public void setDiasEmAberto(int diasEmAberto) {
		this.diasEmAberto = diasEmAberto;
	}

	public String getDiasEmLactacao() {
		return String.valueOf(diasEmLactacao + "d - " + BigDecimal.valueOf(diasEmLactacao).divide(BigDecimal.valueOf(30), 1, RoundingMode.HALF_EVEN) + "m");
	}

	public void setDiasEmLactacao(int diasEmLactacao) {
		this.diasEmLactacao = diasEmLactacao;
	}

	public String getIntervaloEntrePartos() {
		return intervaloEntrePartos + "m";
	}

	public void setIntervaloEntrePartos(int intervaloEntrePartos) {
		this.intervaloEntrePartos = intervaloEntrePartos;
	}

	public String getIdadePrimeiroParto() {
		return idadePrimeiroParto + "m";
	}

	public void setIdadePrimeiroParto(int idadePrimeiroParto) {
		this.idadePrimeiroParto = idadePrimeiroParto;
	}

	public String getIdadePrimeiraCobertura() {
		return idadePrimeiraCobertura + "m";
	}

	public void setIdadePrimeiraCobertura(int idadePrimeiraCobertura) {
		this.idadePrimeiraCobertura = idadePrimeiraCobertura;
	}

	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public String getEvento() {
		return evento;
	}
	
	public void setEvento(String evento) {
		this.evento = evento;
	}

	public Date getDataProximoParto() {
		return dataProximoParto;
	}

	public void setDataProximoParto(Date dataProximoParto) {
		this.dataProximoParto = dataProximoParto;
	}

	public Date getDataUltimoParto() {
		return dataUltimoParto;
	}

	public void setDataUltimoParto(Date dataUltimoParto) {
		this.dataUltimoParto = dataUltimoParto;
	}

	public String getSituacaoUltimaCobertura() {
		return situacaoUltimaCobertura;
	}

	public void setSituacaoUltimaCobertura(String situacaoUltimaCobertura) {
		this.situacaoUltimaCobertura = situacaoUltimaCobertura;
	}

	public Date getDataPrevisaoEncerramentoLactacao() {
		return dataPrevisaoEncerramentoLactacao;
	}

	public void setDataPrevisaoEncerramentoLactacao(
			Date dataPrevisaoEncerramentoLactacao) {
		this.dataPrevisaoEncerramentoLactacao = dataPrevisaoEncerramentoLactacao;
	}

	public int getNumeroLactacoes() {
		return numeroLactacoes;
	}

	public void setNumeroLactacoes(int numeroLactacoes) {
		this.numeroLactacoes = numeroLactacoes;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getUltimoTratamento() {
		return ultimoTratamento;
	}

	public void setUltimoTratamento(String ultimoTratamento) {
		this.ultimoTratamento = ultimoTratamento;
	}

	public BigDecimal getMediaProducao() {
		return mediaProducao;
	}

	public void setMediaProducao(BigDecimal mediaProducao) {
		this.mediaProducao = mediaProducao;
	}

	public BigDecimal getEficienciaReprodutiva() {
		return eficienciaReprodutiva;
	}

	public void setEficienciaReprodutiva(BigDecimal eficienciaReprodutiva) {
		this.eficienciaReprodutiva = eficienciaReprodutiva;
	}
	
	public String getSituacaoAnimal(){
		return animal.getSituacaoAnimal();
	}

	public int getMesesProduzindo() {
		return mesesProduzindo;
	}

	public void setMesesProduzindo(int mesesProduzindo) {
		this.mesesProduzindo = mesesProduzindo;
	}

	public BigDecimal getMesesProducaoIdeal() {
		return mesesProducaoIdeal;
	}

	public void setMesesProducaoIdeal(BigDecimal mesesProducaoIdeal) {
		this.mesesProducaoIdeal = mesesProducaoIdeal;
	}

	public BigDecimal getEficienciaTempoProducao() {
		return eficienciaTempoProducao;
	}

	public void setEficienciaTempoProducao(BigDecimal eficienciaTempoProducao) {
		this.eficienciaTempoProducao = eficienciaTempoProducao;
	}

	public int getDiasGestacaoPeriodo() {
		return diasGestacaoPeriodo;
	}

	public void setDiasGestacaoPeriodo(int diasGestacaoPeriodo) {
		this.diasGestacaoPeriodo = diasGestacaoPeriodo;
	}

	public int getDiasVacaExcessoPeriodo() {
		return diasVacaExcessoPeriodo;
	}

	public void setDiasVacaExcessoPeriodo(int diasVacaExcessoPeriodo) {
		this.diasVacaExcessoPeriodo = diasVacaExcessoPeriodo;
	}
	
}
