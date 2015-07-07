package br.com.milksys.controller.cobertura;

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

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.animal.AnimalReducedOverviewController;
import br.com.milksys.controller.funcionario.FuncionarioReducedController;
import br.com.milksys.controller.semen.SemenReducedOverviewController;
import br.com.milksys.controller.servico.ServicoFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.ResponsavelServico;
import br.com.milksys.model.Semen;
import br.com.milksys.model.Servico;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.State;
import br.com.milksys.model.TipoCobertura;
import br.com.milksys.service.CoberturaService;
import br.com.milksys.service.IService;

@Controller
public class CoberturaFormController extends AbstractFormController<Integer, Cobertura> {

	@FXML private DatePicker inputData;
	@FXML private DatePicker inputPrevisaoParto;
	@FXML private ComboBox<String> inputSituacaoCobertura;
	@FXML private UCTextField inputFemea;
	@FXML private UCTextField inputReprodutor;
	@FXML private UCTextField inputSemen;
	@FXML private UCTextField inputQuantidadeDosesSemen;
	@FXML private UCTextField inputNomeResponsavel;
	@FXML private ComboBox<String> inputResponsavelServico;
	@FXML private ComboBox<String> inputTipoCobertura;
	@FXML private UCTextField inputObservacao;
	@FXML private Button btnNovoReprodutor;
	@FXML private Button btnSalvar;
	@FXML private Label lblReprodutor;
	@FXML private Label lblQuantidadeDosesSemen;
	@FXML private GridPane gridPane;
	
	@Autowired private CoberturaService service;
	
	//controllers
	@Autowired private SemenReducedOverviewController semenReducedController;
	@Autowired private FuncionarioReducedController funcionarioReducedController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private ServicoFormController servicoFormController;
	
	EventHandler<ActionEvent> selectSemenEventHandler = new EventHandler<ActionEvent>() {
	    @Override public void handle(ActionEvent e) {
	    	
			semenReducedController.setObject(new Semen());
			semenReducedController.showForm();
			
			if ( semenReducedController.getObject() != null && semenReducedController.getObject().getId() > 0 ){
				getObject().setSemen(semenReducedController.getObject());
			}
			
			if ( getObject().getSemen() != null ){
				inputSemen.setText(getObject().getSemen().getDescricao());	
			}else{
				inputSemen.setText("");
			}
			
	    }
	};
	
	EventHandler<ActionEvent> selectReprodutorEventHandler = new EventHandler<ActionEvent>() {
	    @Override public void handle(ActionEvent e) {
			
			animalReducedController.setObject(new Animal(Sexo.MACHO, FinalidadeAnimal.REPRODUCAO));
			//animalReducedController.setSearch(searchReprodutoresAtivos);
			
			animalReducedController.showForm();
			if ( animalReducedController.getObject() != null && animalReducedController.getObject().getId() > 0 ){
				getObject().setTouro(animalReducedController.getObject());
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
		inputPrevisaoParto.valueProperty().bindBidirectional(getObject().previsaoPartoProperty());
		
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
		
		if ( getObject().getId() > 0 ){
			
			if ( getObject().getFemea() != null ){
				inputFemea.textProperty().bindBidirectional(getObject().getFemea().numeroNomeProperty());
			}
			
			if ( getObject().getTouro() != null ){
				if ( inputReprodutor == null ){
					inputReprodutor = new UCTextField();
					inputReprodutor.setDisable(true);
				}
				gridPane.getChildren().remove(inputSemen);
				gridPane.getChildren().remove(inputReprodutor);
				gridPane.getChildren().add(inputReprodutor);
				GridPane.setConstraints(inputReprodutor, 1, 3, 3, 1);
				
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
				GridPane.setConstraints(inputSemen, 1, 3, 3, 1);
				
				btnNovoReprodutor.setDisable(false);
				btnNovoReprodutor.setOnAction(selectSemenEventHandler);
				
				inputSemen.setDisable(true);
				inputSemen.textProperty().bindBidirectional(getObject().getSemen().touroProperty());
				
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
			
		}else{
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
			inputPrevisaoParto.setValue(inputData.getValue().plusMonths(9));
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
		lblQuantidadeDosesSemen.setVisible(false);
		inputQuantidadeDosesSemen.setVisible(false);
		btnNovoReprodutor.setDisable(false);
		inputReprodutor.setText("");
		getObject().setTouro(null);
		
		gridPane.getChildren().remove(inputSemen);
		gridPane.getChildren().remove(inputReprodutor);
		gridPane.getChildren().add(inputReprodutor);
		GridPane.setConstraints(inputReprodutor, 1, 3, 3, 1);
		
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
		lblQuantidadeDosesSemen.setVisible(true);
		inputQuantidadeDosesSemen.setVisible(true);
		inputSemen.setText("");
		btnNovoReprodutor.setDisable(false);
		getObject().setSemen(null);

		gridPane.getChildren().remove(inputReprodutor);
		gridPane.getChildren().remove(inputSemen);
		gridPane.getChildren().add(inputSemen);
		GridPane.setConstraints(inputSemen, 1, 3, 3, 1);
		
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
			removerServico();
			
			break;
		}case 1:{
			
			removerServico(); 	
			funcionarioReducedController.showForm();
			
			if ( funcionarioReducedController.getObject() != null && funcionarioReducedController.getObject().getId() > 0 ){
				getObject().setFuncionarioResponsavel(funcionarioReducedController.getObject());
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
			
			if ( getObject().getFemea() != null ){
				
				servicoFormController.setState(State.CREATE_TO_SELECT);
				servicoFormController.setObject(new Servico("COBERTURA " + getObject().getFemea().getNumeroNome()));
				servicoFormController.showForm();
				
				if ( servicoFormController.getObject() != null ){
					getObject().setServico(servicoFormController.getObject());
					inputNomeResponsavel.setText(servicoFormController.getObject().getPrestadorServico().getNome() 
							+ " [R$ " + servicoFormController.getObject().getValor() + "]");
				}else{
					getObject().setServico(null); 	
					inputNomeResponsavel.setText("");
				}
				
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
	
	@FXML
	protected void handleSelecionarFemea() {
		
		animalReducedController.setObject(new Animal(Sexo.FEMEA));
		//animalReducedController.setSearch(searchFemeasAtivas);
		animalReducedController.showForm();
		
		if ( animalReducedController.getObject() != null && animalReducedController.getObject().getId() > 0 ){
			getObject().setFemea(animalReducedController.getObject());
		}
		
		if ( getObject().getFemea() != null ){
			inputFemea.setText(getObject().getFemea().getNumeroNome());
		}else{
			inputFemea.setText("");
		}
		
		
	}
	
	private void removerServico(){
		((CoberturaService)service).removeServicoFromCobertura(getObject());
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
