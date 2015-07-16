package br.com.milksys.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.Indicador;
import br.com.milksys.model.MorteAnimal;
import br.com.milksys.model.Parto;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.VendaAnimal;
import br.com.milksys.util.DateUtil;

@Repository
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Override
	public List<Indicador> findAll(Class<Indicador> clazz) {
		
		List<Indicador> indicadores = super.findAll(Indicador.class);

		for ( Indicador indicador : indicadores ){
			
			switch (indicador.getSigla()) {
			case "DEL":
				indicador.setValorApurado(getValorApuradoDiasEmLactacao());
				break;
				
			case "DEA":
				indicador.setValorApurado(getValorApuradoDiasEmAberto());
				break;

			case "IEP":
				indicador.setValorApurado(getValorApuradoIntervaloEntrePartos());
				break;
				
			case "TR":
				indicador.setValorApurado(getValorApuradoTamanhoRebanho());
				break;	
				
			case "FE":
				indicador.setValorApurado(getValorApuradoTotalFemeas());
				break;
				
			case "VC":
				indicador.setValorApurado(getValorApuradoTotalVacas());
				break;
				
			case "NV":
				indicador.setValorApurado(getValorApuradoTotalNovilhas());
				break;
				
			case "NV1":
				indicador.setValorApurado(getValorApuradoTotalNovilhasAteUmAno());
				break;
				
			case "NV2":
				indicador.setValorApurado(getValorApuradoTotalNovilhasMaisUmAno());
				break;
				
			case "VCL":
				indicador.setValorApurado(getValorApuradoTotalVacasLactacao());
				break;
				
			case "VCS":
				indicador.setValorApurado(getValorApuradoTotalVacasSecas());
				break;
				
			case "TP":
				indicador.setValorApurado(getValorApuradoTaxaPrenhez());
				break;
			default:
				break;
			}
			
		}
		
		return indicadores;
		/*for ( Indicador indicador : indicadores ){
			
			if ( indicador.getQuery() != null && !indicador.getQuery().isEmpty()){
				
				Query query = entityManager.createNativeQuery(indicador.getQuery());
				Object value = query.getSingleResult();
				
				if ( value != null ){
					indicador.setValorApurado( value.toString() );
				}else{
					indicador.setValorApurado("--");
				}
				
			}else{
				indicador.setValorApurado("fórmula não cadastrada");
			}
			
			
			return indicadores;
			
			
		}*/
		
		
		
	}
	
	//============DIAS LACTAÇÃO===========
	@SuppressWarnings("unchecked")
	private BigDecimal getValorApuradoDiasEmLactacao(){
		
		Date       dataInicio     = DateUtil.asDate(LocalDate.of(LocalDate.now().getYear(), 01, 01));
		Date       dataFim        = DateUtil.asDate(LocalDate.of(LocalDate.now().getYear(), 12, 31));
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		
		//busca todas as fêmeas que já tiveram parto
		Query query = entityManager.createQuery("select a from viewAnimaisAtivos a where a.sexo = '" + Sexo.FEMEA + "' and exists  " +
				"(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)");
		
		List<Animal> femeas = query.getResultList();
		
		for ( Animal femea : femeas ){
			
			//localiza o parto anterior a data de inicio, para verificar se ele se sobrepõe ao período atual
			query = entityManager.createQuery("SELECT p FROM Parto p where p.data <= :dataInicio and p.cobertura.femea = :femea order by p.data desc");
			query.setParameter("dataInicio", dataInicio);
			query.setParameter("femea", femea);
			query.setMaxResults(1);
			
			Parto ultimoPartoAnteriorPeriodo = (Parto) query.getSingleResult();
			
			if ( ultimoPartoAnteriorPeriodo != null ){
				dataInicio = ultimoPartoAnteriorPeriodo.getData();
			}
			
			//localiza o parto anterior a data de inicio, para verificar se ele se sobrepõe ao período atual
			query = entityManager.createQuery("SELECT p FROM Parto p where p.data between :dataInicio and :dataFim and p.cobertura.femea = :femea order by p.data asc");
			query.setParameter("dataInicio", dataInicio);
			query.setParameter("dataFim", dataFim);
			query.setParameter("femea", femea);
			
			List<Parto> partos = query.getResultList();
			
			for ( Parto  parto : partos ){
				diasEmLactacao = diasEmLactacao.add(contaDiasLactacaoParto(dataInicio, dataFim, parto, femea));
			}
			
		}
		
		if ( diasEmLactacao.compareTo(BigDecimal.ZERO) > 0 && femeas.size() > 0 ){
			diasEmLactacao = diasEmLactacao.divide(BigDecimal.valueOf(femeas.size()));
		}
		return diasEmLactacao;
		
	}
	
	private BigDecimal contaDiasLactacaoParto(Date dataInicio, Date dataFim, Parto parto, Animal femea){
		
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		
		//verifica se o parto teve o encerramento da lactação
		EncerramentoLactacao encerramento = parto.getEncerramentoLactacao();
		if ( encerramento != null && encerramento.getData().before(dataFim) ){
			
			long diasEntreEncerramentoEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(encerramento.getData()));
			if ( diasEntreEncerramentoEInicioPeriodo > 0 ){//a lactação avançou pelo período
				diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreEncerramentoEInicioPeriodo));
			}
			
		}else{
			
			//Procura registro venda animal após inicio do ano
			VendaAnimal vendaAnimal = findVendaAnimal(dataInicio, femea);
			
			if ( vendaAnimal != null ){
				long diasEntreVendaEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(vendaAnimal.getDataVenda()));
				if ( diasEntreVendaEInicioPeriodo > 0 ){//a lactação avançou pelo período
					diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreVendaEInicioPeriodo));
				}
			}
			
			//Procura registro morte animal após inicio do ano
			MorteAnimal morteAnimal = findMorteAnimal(dataInicio, femea);
			if ( morteAnimal != null ){
				long diasEntreMorteEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(morteAnimal.getDataMorte()));
				if ( diasEntreMorteEInicioPeriodo > 0 ){//a lactação avançou pelo período
					diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreMorteEInicioPeriodo));
				}
			}
			
		}
		
		return diasEmLactacao;
		
	}
	
	
	private VendaAnimal findVendaAnimal(Date dataInicio, Animal animal){
		
		Query query = entityManager.createQuery("SELECT v FROM VendaAnimal va inner join va.animaisVendidos av where av.animal = :animal and va.dataVenda > :dataInicio order by va.dataVenda desc");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		VendaAnimal vendaAnimal = null;
		try{
			vendaAnimal = (VendaAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
		return vendaAnimal;
		
	}
	
	private MorteAnimal findMorteAnimal(Date dataInicio, Animal animal){
		Query query = entityManager.createQuery("SELECT m FROM MorteAnimal m where m.animal = :animal and m.dataMorte > :dataInicio order by m.dataMorte desc");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		MorteAnimal morteAnimal = null;
		try{
			morteAnimal = (MorteAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		return morteAnimal;
	}
	
	//============FIM DIAS LACTAÇÃO===========
	
	private BigDecimal getValorApuradoDiasEmAberto(){
		return BigDecimal.ZERO;
	}
	
	private BigDecimal getValorApuradoIntervaloEntrePartos(){
		return BigDecimal.ZERO;
	}
	
	private BigDecimal getValorApuradoTamanhoRebanho(){
		
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos a").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
		
	}
	
	private BigDecimal getValorApuradoTotalFemeas(){
		
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos where sexo = '" + Sexo.FEMEA + "'").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
		
	}
	
	private BigDecimal getValorApuradoTotalVacas(){
		
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a where a.sexo = '" + Sexo.FEMEA + "' and exists  " +
				"(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));

	}
	
	private BigDecimal getValorApuradoTotalNovilhas(){
		
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a where a.sexo = '" + Sexo.FEMEA + "' and not exists  " +
				"(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));

	}
	
	private BigDecimal getValorApuradoTotalNovilhasAteUmAno(){
		
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(a.dataNascimento, current_date()) <= 360  " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
						+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));

	}
	
	private BigDecimal getValorApuradoTotalNovilhasMaisUmAno(){
		
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(a.dataNascimento, current_date()) > 360  " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
						+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
	
	}
	
	private BigDecimal getValorApuradoTotalVacasLactacao(){
		
		Object result = entityManager.createNativeQuery(" select count(*) from viewAnimaisAtivos a "
				+ "inner join cobertura c inner join parto p on (p.cobertura = c.id) "
				+ "where c.femea = a.id and not exists "
				+ "(select 1 from encerramentoLactacao e where e.data > p.data and e.animal = a.id)").getSingleResult();
		
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
	}
	
	private BigDecimal getValorApuradoTotalVacasSecas(){
		
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos a "
				+ "inner join encerramentoLactacao e on (e.animal = a.id) where not exists "
				+ "(select 1 from parto p inner join cobertura c on (c.id = p.cobertura) where p.data > e.data and c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
		
	}
	
	private BigDecimal getValorApuradoTaxaPrenhez(){
		return BigDecimal.ZERO;
	}
}