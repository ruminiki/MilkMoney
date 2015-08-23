package br.com.milkmoney.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="sistema")
public class Sistema implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String versao;
	
	public String getVersao() {
		return versao;
	}
	
	public void setVersao(String versao) {
		this.versao = versao;
	}
}