package br.com.milksys.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="fichaAnimal")
@NamedQuery(name="FichaAnimal.findAll", query="SELECT a FROM FichaAnimal a")
public class FichaAnimal {

	@Id
	private Animal animal;
	private Date   dataUltimaCobertura;
	private int    numeroServicosAtePrenhez;
	private Date   proximoServico;
	private int    numeroPartos;
	private int    numeroCriasMacho;
	private int    numeroCriasFemeas;
	private int    diasEmAberto;
	private int    diasEmLactacao;
	private int    intervaloEntrePartos;
	private int    idadePrimeiroParto;
	private int    idadePrimeiraCobertura;
	@Transient
	private Date   data;
	@Transient
	private String evento;

	public FichaAnimal() {
	}
	
	public FichaAnimal(Date data, String evento) {
		this.data = data;
		this.evento = evento;
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

	public int getNumeroCriasFemeas() {
		return numeroCriasFemeas;
	}

	public void setNumeroCriasFemeas(int numeroCriasFemeas) {
		this.numeroCriasFemeas = numeroCriasFemeas;
	}

	public int getDiasEmAberto() {
		return diasEmAberto;
	}

	public void setDiasEmAberto(int diasEmAberto) {
		this.diasEmAberto = diasEmAberto;
	}

	public int getDiasEmLactacao() {
		return diasEmLactacao;
	}

	public void setDiasEmLactacao(int diasEmLactacao) {
		this.diasEmLactacao = diasEmLactacao;
	}

	public int getIntervaloEntrePartos() {
		return intervaloEntrePartos;
	}

	public void setIntervaloEntrePartos(int intervaloEntrePartos) {
		this.intervaloEntrePartos = intervaloEntrePartos;
	}

	public int getIdadePrimeiroParto() {
		return idadePrimeiroParto;
	}

	public void setIdadePrimeiroParto(int idadePrimeiroParto) {
		this.idadePrimeiroParto = idadePrimeiroParto;
	}

	public int getIdadePrimeiraCobertura() {
		return idadePrimeiraCobertura;
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

}
