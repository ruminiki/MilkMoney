package br.com.milkmoney.controller.atividadesSemana;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.aborto.AbortoFormController;
import br.com.milkmoney.controller.atividadesSemana.renderer.TableCellTextHyperlinkFactory;
import br.com.milkmoney.controller.cobertura.CoberturaFormController;
import br.com.milkmoney.controller.confirmacaoPrenhes.ConfirmacaoPrenhesFormController;
import br.com.milkmoney.controller.lactacao.LactacaoFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.fichaAnimal.AbstractFichaAnimal;
import br.com.milkmoney.service.fichaAnimal.DataUltimaCobertura;
import br.com.milkmoney.service.fichaAnimal.DataUltimoParto;
import br.com.milkmoney.service.fichaAnimal.ProximoServico;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.validation.CoberturaValidation;


@Controller
public class AtividadesSemanaOverviewController {

	//COBERTURAS
	@FXML private TableView<Cobertura> tableConfirmacaoPrenhes;
	@FXML private TableView<FichaAnimal> tableCoberturas;
	@FXML private TableView<Cobertura> tablePartos;
	@FXML private TableView<Lactacao>  tableEncerramentoLactacao;
	
	@FXML private TableColumn<Cobertura, String>        animalConfirmacaoColumn;
	@FXML private TableColumn<Cobertura, String>        dataCoberturaConfirmacaoColumn;
	@FXML private TableColumn<Cobertura, String>        registrarConfirmacaoColumn;
	
	@FXML private TableColumn<FichaAnimal, String>      animalCoberturaColumn;
	@FXML private TableColumn<FichaAnimal, String>      situacaoAnimalColumn;
	@FXML private TableColumn<FichaAnimal, String>      ultimaCoberturaColumn;
	@FXML private TableColumn<FichaAnimal, String>      ultimoPartoColumn;
	@FXML private TableColumn<FichaAnimal, String>      registrarCoberturaColumn;
	
	@FXML private TableColumn<Cobertura, String>        animalPartoColumn;
	@FXML private TableColumn<Cobertura, String>        dataCoberturaColumn;
	@FXML private TableColumn<Cobertura, String>        previsaoPartoColumn;
	@FXML private TableColumn<Cobertura, String>        registrarPartoColumn;
	
	@FXML private TableColumn<Lactacao, String>         animalEncerramentoColumn;
	@FXML private TableColumn<Lactacao, String>         duracaoLactacaoColumn;
	@FXML private TableColumn<Lactacao, String>         dataPrevistaEncerramentoColumn;
	@FXML private TableColumn<Lactacao, String>         registrarEncerramentoColumn;
	
	@FXML private Label									lblHeader;
		
	@Autowired private CoberturaFormController          coberturaFormController;
	@Autowired private ConfirmacaoPrenhesFormController confirmacaoPrenhesFormController;
	@Autowired private AbortoFormController             abortoFormController;
	@Autowired private PartoFormController              partoFormController;
	@Autowired private LactacaoFormController           lactacaoFormController;
	@Autowired private AnimalService                    animalService;
	@Autowired private ParametroService                 parametroService;
	@Autowired private RelatorioService					relatorioService;
	@Autowired private LactacaoService                  lactacaoService;
	
	@Autowired private CoberturaService 				coberturaService;
	@Autowired private FichaAnimalService               fichaAnimalService;
	
	@FXML
	public void initialize() {
		
		tableConfirmacaoPrenhes.setFixedCellSize(25);
		tableEncerramentoLactacao.setFixedCellSize(25);
		tableCoberturas.setFixedCellSize(25);
		
		animalConfirmacaoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("femea"));
		dataCoberturaConfirmacaoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		registrarConfirmacaoColumn.setCellFactory(new TableCellTextHyperlinkFactory<Cobertura,String>("Confirmar", confirmarPrenhes));
		
		tableConfirmacaoPrenhes.setItems(FXCollections.observableArrayList(coberturaService.findAllNaoConfirmadas()));
		
		animalCoberturaColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("animal"));
		situacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("situacaoAnimal"));
		ultimaCoberturaColumn.setCellFactory(new TableCellDateFactory<FichaAnimal, String>("dataUltimaCobertura"));
		ultimoPartoColumn.setCellFactory(new TableCellDateFactory<FichaAnimal,String>("dataUltimoParto"));
		registrarCoberturaColumn.setCellFactory(new TableCellTextHyperlinkFactory<FichaAnimal,String>("Registrar", registrarCobertura));
		
		tableCoberturas.setItems(FXCollections.observableArrayList(
										fichaAnimalService.generateFichaAnimal(
												animalService.findAnimaisDisponiveisParaCobertura(new Date(), new Date()),
												Arrays.asList(new AbstractFichaAnimal[] {
														(AbstractFichaAnimal)MainApp.getBean(DataUltimaCobertura.class),
														(AbstractFichaAnimal)MainApp.getBean(DataUltimoParto.class),
														(AbstractFichaAnimal)MainApp.getBean(ProximoServico.class)
												}))));
		
		animalPartoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("femea"));
		dataCoberturaColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		previsaoPartoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura, String>("previsaoParto"));
		registrarPartoColumn.setCellFactory(new TableCellTextHyperlinkFactory<Cobertura,String>("Registrar", registrarParto));
		
		tableConfirmacaoPrenhes.setItems(FXCollections.observableArrayList(coberturaService.findAllNaoConfirmadas()));

		animalEncerramentoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("femea"));
		duracaoLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("getDiasLactacao"));
		dataPrevistaEncerramentoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao, String>("dataPrevistaEncerramento"));
		registrarEncerramentoColumn.setCellFactory(new TableCellTextHyperlinkFactory<Lactacao,String>("Registrar", registrarEncerramento));
		
		tableEncerramentoLactacao.setItems(FXCollections.observableArrayList(
											lactacaoService.findAllWithPrevisaoEncerramentoIn(new Date(), DateUtil.asDate(LocalDate.now().plusDays(7)))));

	}
	
	public void showForm() {	
		
		AnchorPane form = (AnchorPane) MainApp.load(getFormName());
		Stage dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);
		dialogStage.setResizable(true);
		dialogStage.setMaximized(false);
		
		dialogStage.show();
		
	}
	
	public String getFormTitle() {
		return "Atividades da Semana";
	}
	
	public String getFormName() {
		return "view/atividadesSemana/AtividadesSemanaOverview.fxml";
	}

	//====FUNCTIONS
	
	Function<Integer, Boolean> registrarParto = index -> {
		tablePartos.getSelectionModel().select(index);
		
		if ( tablePartos.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		Cobertura cobertura = tablePartos.getSelectionModel().getSelectedItem();	
		CoberturaValidation.validaRegistroPartoCobertura(cobertura, lactacaoService.findLastBeforeDate(cobertura.getFemea(), cobertura.getData()));
		
		partoFormController.setState(State.CREATE_TO_SELECT);
		partoFormController.setObject(new Parto(cobertura));
		partoFormController.showForm();
		
		if ( partoFormController.getObject() != null && partoFormController.getObject().getLactacao() != null ){
			coberturaService.registrarParto(cobertura);
			tablePartos.getItems().remove(cobertura);
		}	
		
		return true;
	};
	
	Function<Integer, Boolean> registrarCobertura = index -> {
		tableCoberturas.getSelectionModel().select(index);
		if ( tableCoberturas.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		FichaAnimal ficha = tableCoberturas.getSelectionModel().getSelectedItem();
		
		coberturaFormController.setObject(new Cobertura(ficha.getAnimal(), ficha.getProximoServico() != null ? ficha.getProximoServico() : new Date()));
		coberturaFormController.showForm();
    	
		if ( coberturaFormController.getObject() != null && coberturaFormController.getObject().getId() > 0 ){
			tableCoberturas.getItems().remove(ficha);
		}
		
		return true;
	};
	
	
	Function<Integer, Boolean> confirmarPrenhes = index -> {
		tableConfirmacaoPrenhes.getSelectionModel().select(index);
		if ( tableConfirmacaoPrenhes.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		Cobertura cobertura = tableConfirmacaoPrenhes.getSelectionModel().getSelectedItem();
		
		confirmacaoPrenhesFormController.setObject(cobertura);
    	confirmacaoPrenhesFormController.showForm();
    			
    	if ( cobertura.getSituacaoCobertura().matches(SituacaoCobertura.PRENHA + '|' + SituacaoCobertura.VAZIA) ){
    		tableConfirmacaoPrenhes.getItems().remove(cobertura);
    	}

    	return true;
	};
	
	Function<Integer, Boolean> registrarEncerramento = index -> {
		tableEncerramentoLactacao.getSelectionModel().select(index);
		if ( tableEncerramentoLactacao.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return false;
		}
		
		Lactacao lactacao = tableEncerramentoLactacao.getSelectionModel().getSelectedItem();
		
		lactacaoFormController.setObject(lactacao);
		coberturaFormController.showForm();
    	
		if ( lactacao.getDataFim() != null ){
			tableCoberturas.getItems().remove(lactacao);
		}
		
		return true;
	};
	
	
}
