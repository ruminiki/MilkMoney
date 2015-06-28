package br.com.milksys.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ocorrenciafuncionario database table.
 * 
 */
@Entity
@NamedQuery(name="OcorrenciaFuncionario.findAll", query="SELECT o FROM OcorrenciaFuncionario o")
public class OcorrenciaFuncionario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Temporal(TemporalType.DATE)
	private Date data;

	private String descricao;

	private String justificativa;

	@ManyToOne
	@JoinColumn(name="funcionario")
	private Funcionario funcionario;

	@ManyToOne
	@JoinColumn(name="motivo")
	private MotivoOcorrenciaFuncionario motivoOcorrenciaFuncionario;

	public OcorrenciaFuncionario() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getJustificativa() {
		return this.justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Funcionario getFuncionario() {
		return this.funcionario;
	}

	public void setFuncionario(Funcionario funcionarioBean) {
		this.funcionario = funcionarioBean;
	}

	public MotivoOcorrenciaFuncionario getMotivoOcorrenciaFuncionario() {
		return this.motivoOcorrenciaFuncionario;
	}

	public void setMotivoOcorrenciaFuncionario(MotivoOcorrenciaFuncionario motivoOcorrenciaFuncionario) {
		this.motivoOcorrenciaFuncionario = motivoOcorrenciaFuncionario;
	}

}