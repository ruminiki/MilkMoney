package br.com.milkmoney.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.milkmoney.dao.FichaAnimalDao;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.Procedimento;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.service.indicadores.EficienciaReprodutiva;
import br.com.milkmoney.util.DateUtil;

@Service
public class FichaAnimalService{

	@Autowired private FichaAnimalDao dao;
	@Autowired private CoberturaService coberturaService;
	@Autowired private PartoService partoService;
	@Autowired private AnimalService animalService;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private ProducaoIndividualService producaoIndividualService;
	@Autowired private ProcedimentoService procedimentoService;
	@Autowired private LoteService loteService;
	@Autowired private EficienciaReprodutiva eficienciaReprodutiva;

	public ObservableList<FichaAnimal> findAllEventosByAnimal(Animal animal) {
		return FXCollections.observableArrayList(dao.findAllByAnimal(animal));
	}
	
	@Transactional
	public FichaAnimal generateFichaAnimal(Animal animal){
		
		FichaAnimal fichaAnimal = dao.findFichaAnimal(animal);
		
		if (fichaAnimal == null){
			fichaAnimal = new FichaAnimal(animal);
		}
		//ultima cobertura
		fichaAnimal.setDataUltimaCobertura(coberturaService.getDataUltimaCoberturaAnimal(animal));
		//número serviços até prenhes, baseado na última cobertura
		fichaAnimal.setNumeroServicosAtePrenhes(coberturaService.getNumeroServicosPorConcepcao(animal));
		//próximo serviço
		fichaAnimal.setProximoServico(coberturaService.getProximoServico(animal));
		//número de partos
		fichaAnimal.setNumeroPartos(partoService.countByAnimal(animal));
		//número de crias machos
		fichaAnimal.setNumeroCriasMacho(partoService.countCriasByAnimalAndSexo(animal, Sexo.MACHO));
		//número de crias fêmeas
		fichaAnimal.setNumeroCriasFemea(partoService.countCriasByAnimalAndSexo(animal, Sexo.FEMEA));
		//número de dias em aberto
		fichaAnimal.setDiasEmAberto(coberturaService.getDiasEmAberto(animal));
		//dias em lactação
		fichaAnimal.setDiasEmLactacao(partoService.getDiasEmLactacao(animal));
		//intervalo entre partos
		fichaAnimal.setIntervaloEntrePartos(partoService.getIntervaloEntrePartos(animal));
		//idade primeiro parto
		fichaAnimal.setIdadePrimeiroParto(animalService.getIdadePrimeiroParto(animal));
		//idade primeira cobertura
		fichaAnimal.setIdadePrimeiraCobertura(animalService.getIdadePrimeiraCobertura(animal));
		//proximo parto
		Cobertura cobertura = coberturaService.findCoberturaAtivaByAnimal(animal);
		fichaAnimal.setDataProximoParto(cobertura != null ? cobertura.getPrevisaoParto() : null);
		//ultimo parto
		Parto parto = partoService.findLastParto(animal);
		fichaAnimal.setDataUltimoParto(parto != null ? parto.getData() : null);
		//situacao ultima cobertura
		cobertura = coberturaService.findLastCoberturaAnimal(animal);
		fichaAnimal.setSituacaoUltimaCobertura(cobertura != null ? cobertura.getSituacaoCobertura() : null);
		//encerramento lactação
		Lactacao lactacao = lactacaoService.findUltimaLactacaoAnimal(animal);
		Date dataEncerramentoLactacao = lactacao != null && lactacao.getDataFim() == null ? DateUtil.asDate(DateUtil.asLocalDate(lactacao.getDataInicio()).plusDays(305)) : null;
		fichaAnimal.setDataPrevisaoEncerramentoLactacao(dataEncerramentoLactacao);
		//numero de lactacoes do animal
		fichaAnimal.setNumeroLactacoes(lactacaoService.countLactacoesAnimal(animal).intValue());
		//media de produção da última lactação
		if ( lactacao != null ){
			fichaAnimal.setMediaProducao(BigDecimal.valueOf(producaoIndividualService.getMediaAnimalPeriodo(animal, lactacao.getDataInicio(), lactacao.getDataFim())));
			fichaAnimal.setMediaProducao(fichaAnimal.getMediaProducao().setScale(2, RoundingMode.HALF_EVEN));
		}
		//último procedimento/tratamento
		Procedimento procedimento = procedimentoService.getUltimoTratamento(animal);
		fichaAnimal.setUltimoTratamento(procedimento != null ? procedimento.getDescricao() : "--");
		//lotes que o animal faz parte
		fichaAnimal.setLote(loteService.getNomeLotes(animal));
		//eficiência reprodutiva do animal
		fichaAnimal.setEficienciaReprodutiva(eficienciaReprodutiva.getValue(animal));
		
		dao.persist(fichaAnimal);
				
		return fichaAnimal;
	}
	
	@Transactional
	public void generateFichaForAll(){
		
		List<Animal> animais = animalService.findAll();
		for ( Animal animal : animais ){
			generateFichaAnimal(animal);
		}
		
		
	}
	
	
}
