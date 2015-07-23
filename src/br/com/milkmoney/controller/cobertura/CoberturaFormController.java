package br.com.milkmoney.controller.cobertura;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.animal.AnimalReducedOverviewController;
import br.com.milkmoney.controller.funcionario.FuncionarioReducedController;
import br.com.milkmoney.controller.semen.SemenReducedOverviewController;
import br.com.milkmoney.controller.servico.ServicoFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.FinalidadeAnimal;
import br.com.milkmoney.model.ResponsavelServico;
import br.com.milkmoney.model.Semen;
import br.com.milkmoney.model.Servico;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.model.State;
import br.com.milkmoney.model.TipoCobertura;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.ServicoService;
import br.com.milkmoney.service.searchers.SearchReprodutoresAtivos;
import br.com.milkmoney.util.DateUtil;

@Controller
public class CoberturaFormController extends AbstractFormController<Integer, Cobertura> {

	@FXML private DatePicker inputData;
	@FXML private Label lblPrevisaoParto;
	@FXML private ComboBox<String> inputSituacaoCobertura;
	@FXML private UCTextField inputReprodutor;
	@FXML private UCTextField inputSemen;
	@FXML private UCTextField inputQuantidadeDosesSemen;
	@FXML private UCTextField inputNomeResponsavel;
	@FXML private ComboBox<String> inputResponsavelServico;
	@FXML private ComboBox<String> inputTipoCobertura;
	@FXML private UCTextField inputObservacao;
	@FXML private Button btnNovoReprodutor;
	@FXML private Button btnSalvar;
	@FXML private Label lblReprodutor, lblQuantidadeDosesSemen, lblHeader;
	@FXML private GridPane gridPane;
	
	@Autowired private CoberturaService service;
	@Autowired private ServicoService servicoService;
	
	//controllers
	@Autowired private SemenReducedOverviewController semenReducedOverviewController;
	@Autowired private FuncionarioReducedController funcionarioReducedOverviewController;
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private ServicoFormController servicoFormController;
	
	EventHandler<ActionEvent> selectSemenEventHandler = new EventHandler<ActionEvent>() {
	    @Override public void handle(ActionEvent e) {
	    	
			semenReducedOverviewController.setObject(new Semen());
			semenReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, false);
			semenReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, false);
			semenReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, false);
			semenReducedOverviewController.showForm();
			
			if ( semenReducedOverviewController.getObject() != null && semenReducedOverviewController.getObject().getId() > 0 ){
				getObject().setSemen(semenReducedOverviewController.getObject());
			}
			
			if ( getObject().getSemen() != null ){
				inputSemen.setText(getObject().getSemen().getTouro().toString());	
			}else{
				inputSemen.setText("");
			}
			
	    }
	};
	
	EventHandler<ActionEvent> selectReprodutorEventHandler = new EventHandler<ActionEvent>() {
	    @Override public void handle(ActionEvent e) {
			
			animalReducedOverviewController.setObject(new Animal(Sexo.MACHO, FinalidadeAnimal.REPRODUCAO));
			animalReducedOverviewController.setSearch((SearchReprodutoresAtivos)MainApp.getBean(SearchReprodutoresAtivos.class));
			
			animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.NEW_DISABLED, false);
			animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.EDIT_DISABLED, false);
			animalReducedOverviewController.getFormConfig().put(AbstractOverviewController.REMOVE_DISABLED, false);
			
			animalReducedOverviewController.showForm();
			
			if ( animalReducedOverviewController.getObject() != null && animalReducedOverviewController.getObject().getId() > 0 ){
				getObject().setTouro(animalReducedOverviewController.getObject());
			}
			
			if ( getObject().getTouro() != null ) {
				inputReprodutor.setText(getObject().getTouro().getNumeroNome());
			}else{
				inputReprodutor.setText("");
			}
	    }
	};
	
	@FXML
	public void initialize() {
	
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		lblPrevisaoParto.textProperty().set(DateUtil.format(getObject().getPrevisaoParto()));
		
		inputTipoCobertura.setItems(TipoCobertura.getItems());
		inputTipoCobertura.getSelectionModel().select(TipoCobertura.ENSEMINACAO_ARTIFICIAL);
		inputTipoCobertura.valueProperty().bindBidirectional(getObject().tipoCoberturaProperty());
		
		inputResponsavelServico.setItems(ResponsavelServico.getItems());
		inputNomeResponsavel.textProperty().bindBidirectional(getObject().nomeResponsavelProperty());
		
		MaskFieldUtil.numeroInteiro(inputQuantidadeDosesSemen);
		inputQuantidadeDosesSemen.textProperty().bindBidirectional(getObject().quantidadeDosesUtilizadasProperty());
		
		if ( getObject().getSituacaoCobertura() == null || getObject().getSituacaoCobertura().isEmpty() ){
			getObject().setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
		}
		
		btnSalvar.setDisable(getObject().getParto() != null);
		
		if ( getObject().getTouro() != null ){
			if ( inputReprodutor == null ){
				inputReprodutor = new UCTextField();
				inputReprodutor.setDisable(true);
			}
			
			gridPane.getChildren().remove(inputSemen);
			gridPane.getChildren().remove(inputReprodutor);
			gridPane.getChildren().add(inputReprodutor);
			GridPane.setConstraints(inputReprodutor, 1, 2, 3, 1);
			
			inputReprodutor.textProperty().bindBidirectional(getObject().getTouro().numeroNomeProperty());
			inputReprodutor.setDisable(true);
			btnNovoReprodutor.setDisable(false);
			btnNovoReprodutor.setOnAction(selectReprodutorEventHandler);
		}
		
		if ( getObject().getSemen() != null ){
			if ( inputSemen == null ){
				inputSemen = new UCTextField();
				inputSemen.setDisable(true);
			}
			
			gridPane.getChildren().remove(inputReprodutor);
			gridPane.getChildren().remove(inputSemen);
			gridPane.getChildren().add(inputSemen);
			GridPane.setConstraints(inputSemen, 1, 2, 3, 1);
			
			btnNovoReprodutor.setDisable(false);
			btnNovoReprodutor.setOnAction(selectSemenEventHandler);
			
			inputSemen.setDisable(true);
			inputSemen.setText(getObject().getSemen().getTouro().toString());
			
			lblQuantidadeDosesSemen.setVisible(true);
			inputQuantidadeDosesSemen.setVisible(true);
			
		}
		
		if ( getObject().getFuncionarioResponsavel() != null ){
			inputResponsavelServico.getSelectionModel().select(1);
			inputNomeResponsavel.setDisable(true);
		}else{
			if ( getObject().getServico() != null ){
				inputResponsavelServico.getSelectionModel().select(2);
				inputNomeResponsavel.setDisable(true);
			}else{
				inputResponsavelServico.getSelectionModel().select(0);
			}
		}
		
		lblHeader.setText("REGISTRANDO COBERTURA PARA O ANIMAL: " + getObject().getFemea().toString());
		
		if ( getObject().getId() <= 0 ){
			btnNovoReprodutor.setDisable(false);
			btnNovoReprodutor.setOnAction(selectSemenEventHandler);
		}
		
	}
	
	/**
	 * Quando a data é informada atualiza a previsão de parto
	 */
	@FXML
	private void updateDataPrevisaoParto(){
		if ( inputData.getValue() != null ){
			getObject().setPrevisaoParto(DateUtil.asDate(inputData.getValue().plusDays(282)));
			lblPrevisaoParto.setText(DateUtil.format(getObject().getPrevisaoParto()));
		}
	}

	/**
	 * Quando altera o tipo de cobertura 
	 * carregam os semens ou os reprodutores
	 */
	@FXML
	private void changeTipoCobertura(){
		if ( inputTipoCobertura.getValue() != null ){
			
			if ( inputTipoCobertura.getValue().equals(TipoCobertura.MONTA_NATURAL) ){
				configuraTelaMontaNatural();
			}else{
				configuraTelaEnseminacaoArtificial();
			}
		}
	}
	
	/*
	 * Quando for monta natural configura tela para na tabela de animal touro reprodutor
	 */
	private void configuraTelaMontaNatural(){
		
		if ( inputReprodutor == null ){
			inputReprodutor = new UCTextField();
			inputReprodutor.setDisable(true);
		}

		lblReprodutor.setText("Reprodutor: ");
		inputQuantidadeDosesSemen.setDisable(true);
		btnNovoReprodutor.setDisable(false);
		inputReprodutor.setText("");
		getObject().setTouro(null);
		
		gridPane.getChildren().remove(inputSemen);
		gridPane.getChildren().remove(inputReprodutor);
		gridPane.getChildren().add(inputReprodutor);
		GridPane.setConstraints(inputReprodutor, 1, 2, 3, 1);
		
		super.getDialogStage().show();
		
		btnNovoReprodutor.setOnAction(selectReprodutorEventHandler);
	}
	
	/*
	 * Quando for enseminação artificial habilita busca de semen
	 * e do campo para informar a quantidade de doses utilizadas
	 */
	private void configuraTelaEnseminacaoArtificial(){
		
		if ( inputSemen == null ){
			inputSemen = new UCTextField();
			inputSemen.setDisable(true);
		}
		
		lblReprodutor.setText("Sêmen: ");
		inputQuantidadeDosesSemen.setDisable(false);
		inputSemen.setText("");
		btnNovoReprodutor.setDisable(false);
		getObject().setSemen(null);

		gridPane.getChildren().remove(inputReprodutor);
		gridPane.getChildren().remove(inputSemen);
		gridPane.getChildren().add(inputSemen);
		GridPane.setConstraints(inputSemen, 1, 2, 3, 1);
		
		super.getDialogStage().show();
		
		btnNovoReprodutor.setOnAction(selectSemenEventHandler);
	}
	
	@FXML
	private void alterarResponsavelServico(){
		switch (inputResponsavelServico.getSelectionModel().getSelectedIndex()) {
			case 0:{
				
				inputNomeResponsavel.setText("");
				inputNomeResponsavel.requestFocus();
				inputNomeResponsavel.setDisable(false);
				getObject().setFuncionarioResponsavel(null);
				
				getObject().setServico(null);
				
				break;
			}case 1:{
				
				getObject().setServico(null);
				
				funcionarioReducedOverviewController.showForm();
				
				if ( funcionarioReducedOverviewController.getObject() != null && funcionarioReducedOverviewController.getObject().getId() > 0 ){
					getObject().setFuncionarioResponsavel(funcionarioReducedOverviewController.getObject());
					inputNomeResponsavel.setText(getObject().getFuncionarioResponsavel().getNome());
					inputNomeResponsavel.setDisable(true);
				}
				
				if ( getObject().getFuncionarioResponsavel() != null ){
					inputNomeResponsavel.setText(getObject().getFuncionarioResponsavel().getNome());
				}else{
					inputNomeResponsavel.setText("");
				}
				
				
				break;
			}case 2:{
				
				servicoFormController.setState(State.CREATE_TO_SELECT);
				servicoFormController.setObject(new Servico("COBERTURA " + getObject().getFemea().getNumeroNome()));
				servicoFormController.showForm();
				
				if ( servicoFormController.getObject() != null ){
					getObject().setServico(servicoFormController.getObject());
					inputNomeResponsavel.setText(servicoFormController.getObject().toString());
				}else{
					getObject().setServico(null); 	
					inputNomeResponsavel.setText("");
				}
				
				inputNomeResponsavel.setDisable(true);
				getObject().setFuncionarioResponsavel(null);
				
				break;
			}default:{
				
				getObject().setFuncionarioResponsavel(null);
				getObject().setServico(null);
				break;
			}
		}
	}
	
	@Override
	protected String getFormName() {
		return "view/cobertura/CoberturaForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Cobertura";
	}

	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}
	
}
