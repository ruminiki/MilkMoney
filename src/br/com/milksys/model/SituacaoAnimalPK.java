package br.com.milksys.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SituacaoAnimalPK implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Integer animal;
    protected String situacao;

    public SituacaoAnimalPK() {}

    public SituacaoAnimalPK(Integer animal, String situacao) {
        this.animal = animal;
        this.situacao = situacao;
    }
    @Override
    public boolean equals(Object obj) {
    	return ((SituacaoAnimal)obj).getAnimal() == animal && ((SituacaoAnimal)obj).getSituacao().equals(situacao);
    }
    
    @Override
    public int hashCode() {
    	return super.hashCode();
    }
}