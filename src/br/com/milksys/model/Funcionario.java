package br.com.milksys.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the funcionario database table.
 * 
 */
@Entity
@NamedQuery(name="Funcionario.findAll", query="SELECT f FROM Funcionario f")
public class Funcionario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Temporal(TemporalType.DATE)
	private Date dataContratacao;

	private int diaPagamento;

	private String jornadaTrabalho;

	private String nome;

	private BigDecimal salario;

	//bi-directional many-to-one association to Ocorrenciafuncionario
	@OneToMany(mappedBy="funcionario")
	private List<OcorrenciaFuncionario> ocorrenciaFuncionarios;

	public Funcionario() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataContratacao() {
		return this.dataContratacao;
	}

	public void setDataContratacao(Date dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	public int getDiaPagamento() {
		return this.diaPagamento;
	}

	public void setDiaPagamento(int diaPagamento) {
		this.diaPagamento = diaPagamento;
	}

	public String getJornadaTrabalho() {
		return this.jornadaTrabalho;
	}

	public void setJornadaTrabalho(String jornadaTrabalho) {
		this.jornadaTrabalho = jornadaTrabalho;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getSalario() {
		return this.salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public List<OcorrenciaFuncionario> getOcorrenciaFuncionarios() {
		return this.ocorrenciaFuncionarios;
	}

	public void setOcorrenciafuncionarios(List<OcorrenciaFuncionario> ocorrenciaFuncionarios) {
		this.ocorrenciaFuncionarios = ocorrenciaFuncionarios;
	}

	public OcorrenciaFuncionario addOcorrenciafuncionario(OcorrenciaFuncionario ocorrenciaFuncionario) {
		getOcorrenciaFuncionarios().add(ocorrenciaFuncionario);
		ocorrenciaFuncionario.setFuncionario(this);
		return ocorrenciaFuncionario;
	}

	public OcorrenciaFuncionario removeOcorrenciafuncionario(OcorrenciaFuncionario ocorrenciaFuncionario) {
		getOcorrenciaFuncionarios().remove(ocorrenciaFuncionario);
		ocorrenciaFuncionario.setFuncionario(null);
		return ocorrenciaFuncionario;
	}

}