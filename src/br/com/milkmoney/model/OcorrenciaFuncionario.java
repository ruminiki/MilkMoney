package br.com.milkmoney.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.milkmoney.util.DateUtil;


@Entity
@NamedQuery(name="OcorrenciaFuncionario.findAll", query="SELECT o FROM OcorrenciaFuncionario o")
public class OcorrenciaFuncionario extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private ObjectProperty<LocalDate> data = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private StringProperty descricao = new SimpleStringProperty();
	private StringProperty justificativa = new SimpleStringProperty();
	private ObjectProperty<Funcionario> funcionario = new SimpleObjectProperty<Funcionario>();
	private ObjectProperty<MotivoOcorrenciaFuncionario> motivoOcorrenciaFuncionario = new SimpleObjectProperty<MotivoOcorrenciaFuncionario>();

	public OcorrenciaFuncionario() {
		// TODO Auto-generated constructor stub
	}
	
	public OcorrenciaFuncionario(Funcionario funcionario) {
		this.funcionario.set(funcionario);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getData() {
		return DateUtil.asDate(this.data.get());
	}

	public void setData(Date data) {
		this.data.set(DateUtil.asLocalDate(data));
	}
	
	public ObjectProperty<LocalDate> dataProperty(){
		return data;
	}

	@Access(AccessType.PROPERTY)
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
	public String getJustificativa() {
		return this.justificativa.get();
	}

	public void setJustificativa(String justificativa) {
		this.justificativa.set(justificativa);
	}
	
	public StringProperty justificativaProperty(){
		return justificativa;
	}
	
	@ManyToOne
	@JoinColumn(name="funcionario")
	@Access(AccessType.PROPERTY)
	public Funcionario getFuncionario() {
		return this.funcionario.get();
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario.set(funcionario);
	}
	
	public ObjectProperty<Funcionario> funcionarioProperty(){
		return funcionario;
	}
	
	@ManyToOne
	@JoinColumn(name="motivo")
	@Access(AccessType.PROPERTY)
	public MotivoOcorrenciaFuncionario getMotivoOcorrenciaFuncionario() {
		return this.motivoOcorrenciaFuncionario.get();
	}

	public void setMotivoOcorrenciaFuncionario(MotivoOcorrenciaFuncionario motivoOcorrenciaFuncionario) {
		this.motivoOcorrenciaFuncionario.set(motivoOcorrenciaFuncionario);
	}

	public ObjectProperty<MotivoOcorrenciaFuncionario> motivoOcorrenciaFuncionarioProperty(){
		return motivoOcorrenciaFuncionario;
	}
}