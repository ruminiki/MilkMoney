package br.com.milkmoney.controller.animal;

import java.math.BigDecimal;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.cobertura.CoberturaFormController;
import br.com.milkmoney.controller.cobertura.CoberturaOverviewController;
import br.com.milkmoney.controller.confirmacaoPrenhes.ConfirmacaoPrenhesFormController;
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.indicador.IndicadorOverviewController;
import br.com.milkmoney.controller.lactacao.LactacaoOverviewController;
import br.com.milkmoney.controller.morteAnimal.MorteAnimalFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.producaoIndividual.ProducaoIndividualOverviewController;
import br.com.milkmoney.controller.raca.RacaOverviewController;
import br.com.milkmoney.controller.raca.RacaReducedOverviewController;
import br.com.milkmoney.controller.touro.TouroReducedOverviewController;
import br.com.milkmoney.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.Procedimento;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.MorteAnimalService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.service.PartoService;
import br.com.milkmoney.service.ProcedimentoService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.VendaAnimalService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;

@Controller
public class AcessoRapidoAnimalController extends AbstractFormController<Integer, Animal> {

	@FXML private TableView<Cobertura> tableCoberturas;
	@FXML private TableColumn<Cobertura, String> dataCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> reprodutorColumn;
	
	@FXML private TableView<Parto> tablePartos;
	@FXML private TableColumn<Parto, String> dataPartoColumn;
	@FXML private TableColumn<Parto, String> tipoPartoColumn;
	@FXML private TableColumn<Parto, String> complicacaoPartoColumn;
	
	@FXML private TableView<Procedimento> tableProcedimentos;
	@FXML private TableColumn<Procedimento, String> dataProcedimentoColumn;
	@FXML private TableColumn<Procedimento, String> tipoProcedimentoColumn;
	@FXML private TableColumn<Procedimento, String> observacaoProcedimentoColumn;
	
	@FXML private Label lblNumeroServicos, lblDataUltimaCobertura, lblProximoServico, lblNumeroPartos, lblIdadePrimeiroParto, 
	lblIdadePrimeiraCobertura, lblDiasEmLactacao, lblDiasEmAberto, lblIntervaloPrimeiroParto, lblDataSecar,
	lblAnimal, lblDataUltimoParto, lblDataProximoParto, lblSituacaoUltimaCobertura, lblPai, lblMae, 
	lblEmLactacao, lblSecas, lblNaoDefinidas, lblTotalVacas, lblNovilhas, lblNumeroLactacoes, lblMediaProducao, 
	lblUltimoTratamento, lblLote, lblEficienciaReprodutiva;
	
	@FXML private Hyperlink hlVisualizarUltimoParto, hlSecarAnimal, hlRegistrarParto, hlEditarCobertura, hlRegistrarCobertura, hlConfirmarPrenhes;
	
	//services
	@Autowired private MorteAnimalService morteAnimalService;
	@Autowired private VendaAnimalService vendaAnimalService;
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private RelatorioService relatorioService;
	@Autowired private ParametroService parametroService;
	@Autowired private PartoService partoService;
	@Autowired private CoberturaService coberturaService;
	@Autowired private ProcedimentoService procedimentoService;
	
	//controllers
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private TouroReducedOverviewController touroReducedOverviewController;
	@Autowired private RacaReducedOverviewController racaReducedOverviewController;
	@Autowired private ConfirmacaoPrenhesFormController confirmacaoPrenhesFormController;
	@Autowired private CoberturaOverviewController coberturaOverviewController;
	@Autowired private CoberturaFormController coberturaFormController;
	@Autowired private RacaOverviewController racaController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private MorteAnimalFormController morteAnimalFormController;
	@Autowired private VendaAnimalFormController vendaAnimalFormController;
	@Autowired private LactacaoOverviewController lactacaoOverviewController;
	@Autowired private FichaAnimalOverviewController fichaAnimalOverviewController;
	@Autowired private ProducaoIndividualOverviewController producaoIndividualOverviewController;
	@Autowired private IndicadorOverviewController indicadorOverviewController;
	@Autowired private PartoFormController partoFormController;
	
	private FichaAnimal fichaAnimal;
	private String numeroDigitado;
	
	@FXML
	public void initialize() {
		
		setObject(((AnimalService)service).findByNumero(numeroDigitado));
		
		dataCoberturaColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("situacaoCobertura"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		
		tableCoberturas.getItems().addAll(coberturaService.findByAnimal(getObject()));
		
		dataPartoColumn.setCellFactory(new TableCellDateFactory<Parto,String>("data"));
		tipoPartoColumn.setCellValueFactory(new PropertyValueFactory<Parto,String>("tipoParto"));
		complicacaoPartoColumn.setCellValueFactory(new PropertyValueFactory<Parto,String>("complicacaoParto"));
		
		tablePartos.getItems().addAll(partoService.findByAnimal(getObject()));
		
		dataProcedimentoColumn.setCellFactory(new TableCellDateFactory<Procedimento,String>("dataRealizacao"));
		tipoProcedimentoColumn.setCellValueFactory(new PropertyValueFactory<Procedimento,String>("tipoProcedimento"));
		observacaoProcedimentoColumn.setCellValueFactory(new PropertyValueFactory<Procedimento,String>("observacao"));
		
		tableProcedimentos.getItems().addAll(procedimentoService.findByAnimal(getObject()));
		
		handleFichaAnimal();
		
	}
	
	private void handleFichaAnimal(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if ( getObject() != null ){
					
					fichaAnimal = fichaAnimalService.generateFichaAnimal(getObject());
					
					if ( fichaAnimal != null ){
						lblNumeroServicos.setText(""+fichaAnimal.getNumeroServicosAtePrenhes());
						lblDataUltimaCobertura.setText(fichaAnimal.getDataUltimaCobertura() != null ? DateUtil.format(fichaAnimal.getDataUltimaCobertura()) : "--"); 
						lblProximoServico.setText(fichaAnimal.getProximoServico() != null ? DateUtil.format(fichaAnimal.getProximoServico()) : "--"); 
						lblNumeroPartos.setText(""+fichaAnimal.getNumeroPartos() + " - "  + fichaAnimal.getNumeroCriasFemea() + "F" + " - " + fichaAnimal.getNumeroCriasMacho() + "M"); 
						lblIdadePrimeiroParto.setText(""+fichaAnimal.getIdadePrimeiroParto());
						lblIdadePrimeiraCobertura.setText(fichaAnimal.getIdadePrimeiraCobertura()); 
						lblDiasEmLactacao.setText(fichaAnimal.getDiasEmLactacao()); 
						lblDiasEmAberto.setText(fichaAnimal.getDiasEmAberto()); 
						lblIntervaloPrimeiroParto.setText(fichaAnimal.getIntervaloEntrePartos());
						lblDataSecar.setText(fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null ? DateUtil.format(fichaAnimal.getDataPrevisaoEncerramentoLactacao()) : "--");
						lblDataProximoParto.setText(fichaAnimal.getDataProximoParto() != null ? DateUtil.format(fichaAnimal.getDataProximoParto()) : "--");	
						lblDataUltimoParto.setText(fichaAnimal.getDataUltimoParto() != null ? DateUtil.format(fichaAnimal.getDataUltimoParto()) : "--");
						lblSituacaoUltimaCobertura.setText(fichaAnimal.getSituacaoUltimaCobertura());
						lblLote.setText(fichaAnimal.getLote());
						lblMediaProducao.setText(fichaAnimal.getMediaProducao() != null && fichaAnimal.getMediaProducao().compareTo(BigDecimal.ZERO) > 0 ? String.valueOf(fichaAnimal.getMediaProducao()) : "--");
						lblUltimoTratamento.setText(fichaAnimal.getUltimoTratamento());
						lblNumeroLactacoes.setText(fichaAnimal.getNumeroLactacoes() > 0 ? String.valueOf(fichaAnimal.getNumeroLactacoes()) : "--");
						lblEficienciaReprodutiva.setText(fichaAnimal.getEficienciaReprodutiva() + "%");
						
						//links
						hlVisualizarUltimoParto.setVisible(fichaAnimal.getDataUltimoParto() != null);
						if (fichaAnimal.getDataUltimoParto() != null){
							hlVisualizarUltimoParto.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									partoFormController.setObject(partoService.findLastParto(getObject()));
									partoFormController.showForm();
								}
							});
						}
						
						hlSecarAnimal.setVisible(fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null);
						if (fichaAnimal.getDataPrevisaoEncerramentoLactacao() != null){
							hlSecarAnimal.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									handleEncerrarLactacao();
								}
							});
						}
						
						hlRegistrarParto.setVisible(fichaAnimal.getDataProximoParto() != null);
						if (fichaAnimal.getDataProximoParto() != null){
							hlRegistrarParto.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									Cobertura cobertura = coberturaService.findCoberturaAtivaByAnimal(getObject());
									if ( cobertura != null && cobertura.getParto() == null ){
										
										CoberturaValidation.validaRegistroPartoCobertura(cobertura, lactacaoService.findLastBeforeDate(cobertura.getFemea(), cobertura.getData()));
										
										partoFormController.setState(State.CREATE_TO_SELECT);
										partoFormController.setObject(new Parto(cobertura));
										partoFormController.showForm();
										
										if ( partoFormController.getObject() != null ){
											cobertura.setParto(partoFormController.getObject());
											coberturaService.registrarParto(cobertura);
										}	
									}
								}
							});
						}
						
						hlEditarCobertura.setVisible(fichaAnimal.getDataUltimaCobertura() != null);
						if ( fichaAnimal.getDataUltimaCobertura() != null ){
							hlEditarCobertura.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									Cobertura cobertura = coberturaService.findLastCoberturaAnimal(getObject());
									if ( cobertura != null ){
										coberturaFormController.setObject(cobertura);
										coberturaFormController.showForm();
									}
								}
							});
						}
						
						hlRegistrarCobertura.setVisible(fichaAnimal.getProximoServico() != null);
						if ( fichaAnimal.getProximoServico() != null ){
							hlRegistrarCobertura.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									coberturaFormController.setObject(new Cobertura(getObject(), fichaAnimal.getProximoServico()));
									coberturaFormController.showForm();
								}
							});
						}
						
						hlConfirmarPrenhes.setVisible(fichaAnimal.getDataUltimaCobertura() != null);
						if ( fichaAnimal.getDataUltimaCobertura() != null ){
							if ( fichaAnimal.getSituacaoUltimaCobertura().equals(SituacaoCobertura.NAO_CONFIRMADA) ){
								hlConfirmarPrenhes.setText("confirmar");
							}else{
								if ( fichaAnimal.getSituacaoUltimaCobertura().matches(SituacaoCobertura.PRENHA + "|" + SituacaoCobertura.VAZIA) ){
									hlConfirmarPrenhes.setText("visualizar");
								}else{
									hlConfirmarPrenhes.setVisible(false);
								}
							}
							hlConfirmarPrenhes.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									Cobertura cobertura = coberturaService.findLastCoberturaAnimal(getObject());
									if ( cobertura != null ){
										confirmacaoPrenhesFormController.setObject(cobertura);
								    	confirmacaoPrenhesFormController.showForm();
									}
								}
							});
						}
					}
					lblPai.setText(getObject().getPaiEnseminacaoArtificial() != null ? getObject().getPaiEnseminacaoArtificial().toString() : ( getObject().getPaiMontaNatural() != null ? getObject().getPaiMontaNatural().toString() : "--" ));
					lblMae.setText(getObject().getMae() != null ? getObject().getMae().toString() : "--");
					lblAnimal.setText(getObject().toString());
					
				}
			}
		});
	}
	
	private void handleEncerrarLactacao() {
		if ( getObject().getSexo().equals(Sexo.FEMEA) ){
			lactacaoOverviewController.setAnimal(getObject());
			lactacaoOverviewController.showForm();
		}else{
			CustomAlert.mensagemInfo("Somente animais fêmeas podem ter a lactação encerrada.");
		}
	};

	public void setNumeroDigitado(String numeroDigitado) {
		this.numeroDigitado = numeroDigitado;
	}

	@Override
	public String getFormName() {
		return "view/animal/AcessoRapidoAnimal.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Animal";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}

	
}
