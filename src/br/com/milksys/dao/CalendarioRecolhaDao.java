package br.com.milksys.dao;

import org.springframework.stereotype.Component;

import br.com.milksys.model.CalendarioRecolha;

@Component
public class CalendarioRecolhaDao extends AbstractGenericDao<Integer, CalendarioRecolha> {

	public CalendarioRecolha getCalendarioVigente() {
		return (CalendarioRecolha) entityManager
				.createQuery("SELECT c FROM CalendarioRecolha c WHERE c.calendarioVigente = 'SIM'").getSingleResult();
	}
}