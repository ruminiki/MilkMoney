package br.com.milkmoney.service.fichaAnimal;

import org.springframework.stereotype.Service;

@Service
public abstract class AbstractFichaAnimal {

	public abstract void load(Object ... params);
	
}
