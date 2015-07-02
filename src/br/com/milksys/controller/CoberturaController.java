package br.com.milksys.controller;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.AbstractEntity;
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
import br.com.milksys.service.ServicoService;
import br.com.milksys.service.searchers.SearchFemeasAtivas;
import br.com.milksys.service.searchers.SearchReprodutoresAtivos;
import br.com.milksys.util.DateUtil;

@Controller
public class CoberturaController extends AbstractController<Integer, Cobertura> {

	@FXML private TableColumn<Cobertura, String> dataColumn;
	@FXML private TableColumn<Animal, String> femeaColumn;
	@FXML private TableColumn<Cobertura, String> reprodutorColumn;
	@FXML private TableColumn<Cobertura, String> previsaoPartoColumn;
	@FXML private TableColumn<TipoCobertura, String> tipoCoberturaColumn;
	@FXML private TableColumn<SituacaoCobertura, String> situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, LocalDate> repeticaoCioColumn;
	@FXML private TableColumn<Cobertura, String> primeiroToqueColumn;
	@FXML private TableColumn<Cobertura, String> reconfirmacaoColumn;
	
	@FXML private UCTextField inputDescricao;
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
	@FXML private Label lblReprodutor;
	@FXML private Label lblQuantidadeDosesSemen;
	
	@FXML private GridPane gridPane;
	
	//services
	@Autowired private ServicoService servicoService;
	
	//controllers
	@Autowired private SemenReducedController semenReducedController;
	@Autowired private FuncionarioReducedController funcionarioReducedController;
	@Autowired private AnimalReducedController animalReducedController;
	@Autowired private AnimalController animalController;
	@Autowired private ServicoController servicoController;
	
	//searchers
	@Autowired private SearchFemeasAtivas searchFemeasAtivas;
	@Autowired private SearchReprodutoresAtivos searchReprodutoresAtivos;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
			femeaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("femea"));
			reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
			previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
			tipoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<TipoCobertura,String>("tipoCobertura"));
			situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<SituacaoCobertura,String>("situacaoCobertura"));
			
			repeticaoCioColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,LocalDate>("dataRepeticaoCio"));
			repeticaoCioColumn.setCellFactory(new Callback<TableColumn<Cobertura,LocalDate>, TableCell<Cobertura,LocalDate>>(){
				@Override
				public TableCell<Cobertura, LocalDate> call(TableColumn<Cobertura, LocalDate> param) {
					TableCell<Cobertura, LocalDate> cell = new TableCell<Cobertura, LocalDate>(){
						@Override
						public void updateItem(LocalDate item, boolean empty) {
							Hyperlink link = new Hyperlink();
							if ( getTableRow().getIndex() < data.size() ){
								/*Cobertura c = data.get(getTableRow().getIndex());
								if ( c.getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) )
									getTableRow().setStyle("-fx-background-color: #99FF00;");
								if ( c.getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA)  )
									getTableRow().setStyle("-fx-background-color: #FF3300;");
								if ( c.getSituacaoCobertura().equals(SituacaoCobertura.VAZIA) )
									getTableRow().setStyle("-fx-background-color: #FF3300;");*/
								if(item!=null){
									link.setText(DateUtil.format(item));
								}else{
									link.setText("--");
								}
							}
							link.setFocusTraversable(false);
							link.setOnAction(new EventHandler<ActionEvent>() {
							    @Override
							    public void handle(ActionEvent e) {
							    	setObject(data.get(getTableRow().getIndex()));
							    	state = State.REPETICAO;
							    	showForm("view/cobertura/RegistrarRepeticaoCioForm.fxml");
							    }
							});
							setGraphic(link);
						} 
					};                           
					return cell;
				}
			});
			
			primeiroToqueColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("primeiroToque"));
			primeiroToqueColumn.setCellFactory(new Callback<TableColumn<Cobertura,String>, TableCell<Cobertura,String>>(){
				@Override
				public TableCell<Cobertura, String> call(TableColumn<Cobertura, String> param) {
					TableCell<Cobertura, String> cell = new TableCell<Cobertura, String>(){
						@Override
						public void updateItem(String item, boolean empty) {
							if(item!=null){
								Hyperlink link = new Hyperlink();
								if ( getTableRow().getIndex() < data.size() )
									link.setText(item);
								else
									link.setText("--");
								link.setFocusTraversable(false);
								link.setOnAction(new EventHandler<ActionEvent>() {
								    @Override
								    public void handle(ActionEvent e) {
								    	setObject(data.get(getTableRow().getIndex()));
								    	state = State.PRIMEIRO_TOQUE;
								    	showForm("view/cobertura/RegistrarPrimeiroToqueForm.fxml");
								    }
								});
								setGraphic(link);
							} 
						}
					};                           
					return cell;
				}
			});
			reconfirmacaoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reconfirmacao"));
			reconfirmacaoColumn.setCellFactory(new Callback<TableColumn<Cobertura,String>, TableCell<Cobertura,String>>(){
				@Override
				public TableCell<Cobertura, String> call(TableColumn<Cobertura, String> param) {
					TableCell<Cobertura, String> cell = new TableCell<Cobertura, String>(){
						@Override
						public void updateItem(String item, boolean empty) {
							if(item!=null){
								Hyperlink link = new Hyperlink();
								if ( getTableRow().getIndex() < data.size() )
									link.setText(item);
								else
									link.setText("--");
								link.setFocusTraversable(false);
								link.setOnAction(new EventHandler<ActionEvent>() {
								    @Override
								    public void handle(ActionEvent e) {
								    	setObject(data.get(getTableRow().getIndex()));
								    	state = State.RECONFIRMACAO;
								    	showForm("view/cobertura/RegistrarReconfirmacaoForm.fxml");
								    }
								});
								setGraphic(link);
							} 
						}
					};                           
					return cell;
				}
			});
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			inputData.valueProperty().bindBidirectional(getObject().dataProperty());
			inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
			inputPrevisaoParto.valueProperty().bindBidirectional(getObject().previsaoPartoProperty());
			
			inputTipoCobertura.setItems(TipoCobertura.getItems());
			inputTipoCobertura.valueProperty().bindBidirectional(getObject().tipoCoberturaProperty());
			inputResponsavelServico.setItems(ResponsavelServico.getItems());
			inputNomeResponsavel.textProperty().bindBidirectional(getObject().nomeResponsavelProperty());
			
			MaskFieldUtil.numeroInteiro(inputQuantidadeDosesSemen);
			inputQuantidadeDosesSemen.textProperty().bindBidirectional(getObject().quantidadeDosesSemenProperty());
			
			if ( getObject().getSituacaoCobertura() == null || getObject().getSituacaoCobertura().isEmpty() ){
				getObject().setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
			}
			
		}
		
		if ( state.equals(State.UPDATE)  ){
			
			if ( getObject().getFemea() != null ){
				inputFemea.textProperty().bindBidirectional(getObject().getFemea().numeroNomeProperty());
			}
			
			if ( getObject().getTouro() != null ){
				if ( inputReprodutor == null ){
					
					inputReprodutor = new UCTextField();
					inputReprodutor.prefWidthProperty().set(320);
					
				}
				gridPane.getChildren().remove(inputSemen);
				gridPane.getChildren().remove(inputReprodutor);
				gridPane.getChildren().add(inputReprodutor);
				GridPane.setConstraints(inputReprodutor, 1, 4, 3, 1);
				
				inputReprodutor.textProperty().bindBidirectional(getObject().getTouro().numeroNomeProperty());
				inputReprodutor.setDisable(true);
			}
			
			if ( getObject().getSemen() != null ){
				if ( inputSemen == null ){
					inputSemen = new UCTextField();
					inputSemen.prefWidthProperty().set(320);
				}
				
				gridPane.getChildren().remove(inputReprodutor);
				gridPane.getChildren().remove(inputSemen);
				gridPane.getChildren().add(inputSemen);
				GridPane.setConstraints(inputSemen, 1, 4, 3, 1);
				
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
			
		}
		
		if ( state.equals(State.PRIMEIRO_TOQUE) ){
			inputData.valueProperty().bindBidirectional(getObject().dataPrimeiroToqueProperty());
			inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
			inputSituacaoCobertura.valueProperty().bindBidirectional(getObject().resultadoPrimeiroToqueProperty());
			inputObservacao.textProperty().bindBidirectional(getObject().observacaoPrimeiroToqueProperty());
		}
		
		if ( state.equals(State.RECONFIRMACAO) ){
			inputData.valueProperty().bindBidirectional(getObject().dataReconfirmacaoProperty());
			inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
			inputSituacaoCobertura.valueProperty().bindBidirectional(getObject().resultadoReconfirmacaoProperty());
			inputObservacao.textProperty().bindBidirectional(getObject().observacaoReconfirmacaoProperty());
		}
		
		if ( state.equals(State.REPETICAO) ){
			getObject().setSituacaoCobertura(SituacaoCobertura.REPETIDA);
			inputData.valueProperty().bindBidirectional(getObject().dataRepeticaoCioProperty());
			inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
			inputSituacaoCobertura.getSelectionModel().select(SituacaoCobertura.REPETIDA);
			inputSituacaoCobertura.setDisable(true);
			inputObservacao.textProperty().bindBidirectional(getObject().observacaoRepeticaoCioProperty());
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
	
	@FXML
	private void handleSavePrimeiroToque(){
		try {
			((CoberturaService)service).registrarPrimeiroToque(getObject());
			super.dialogStage.close();
			refreshRegistroTabela(getObject());
		} catch (ValidationException e) {
			CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
			return;
		}
	}
	
	@FXML
	private void handleSaveReconfirmacao(){
		try {
			((CoberturaService)service).registrarReconfirmacao(getObject());
			super.dialogStage.close();
			refreshRegistroTabela(getObject());
		} catch (ValidationException e) {
			CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
			return;
		}
	}
	
	@FXML
	private void handleSaveRepeticaoCio(){
		try {
			((CoberturaService)service).registrarRepeticaoCio(getObject());
			super.dialogStage.close();
			refreshRegistroTabela(getObject());
		} catch (ValidationException e) {
			CustomAlert.mensagemAlerta(e.getTipo(), e.getMessage());
			return;
		}
	}
	
	private void refreshRegistroTabela(Cobertura cobertura){
		if (table != null && data != null) {
			for (int index = 0; index < data.size(); index++) {
				AbstractEntity o = (AbstractEntity) data.get(index);
				if (o.getId() == cobertura.getId()) {
					data.set(index, cobertura);
				}
			}
		}
	}
	
	/*
	 * Quando for monta natural configura tela para na tabela de animal touro reprodutor
	 */
	private void configuraTelaMontaNatural(){
		
		if ( inputReprodutor == null ){
			
			inputReprodutor = new UCTextField();
			inputReprodutor.prefWidthProperty().set(320);
			
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
		GridPane.setConstraints(inputReprodutor, 1, 4, 3, 1);
		
		dialogStage.show();
		
		btnNovoReprodutor.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
				
				animalReducedController.object = new Animal(Sexo.MACHO, FinalidadeAnimal.REPRODUCAO);
				animalReducedController.setSearch(searchReprodutoresAtivos);
				
				animalReducedController.showForm(null);
				if ( animalReducedController.getObject() != null && animalReducedController.getObject().getId() > 0 ){
					getObject().setTouro(animalReducedController.getObject());
					inputReprodutor.setText(animalReducedController.getObject().getNumeroNome());
				}else{
					inputReprodutor.setText("");
				}
		    }
		    
		});
	}
	
	/*
	 * Quando for enseminação artificial habilita busca de semen
	 * e do campo para informar a quantidade de doses utilizadas
	 */
	private void configuraTelaEnseminacaoArtificial(){
		
		if ( inputSemen == null ){
			inputSemen = new UCTextField();
			inputSemen.prefWidthProperty().set(320);
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
		GridPane.setConstraints(inputSemen, 1, 4, 3, 1);
		
		dialogStage.show();
		
		btnNovoReprodutor.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	
				semenReducedController.object = new Semen();
				semenReducedController.showForm(null);
				
				if ( semenReducedController.getObject() != null && semenReducedController.getObject().getId() > 0 ){
					getObject().setSemen(semenReducedController.getObject());
					inputSemen.setText(semenReducedController.getObject().getDescricao());
				}else{
					inputSemen.setText("");
				}
				
		    }
		});
		
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
			funcionarioReducedController.showForm(funcionarioReducedController.getFormName());
			
			if ( funcionarioReducedController.getObject() != null && funcionarioReducedController.getObject().getId() > 0 ){
				
				getObject().setFuncionarioResponsavel(funcionarioReducedController.getObject());
				inputNomeResponsavel.setText(getObject().getFuncionarioResponsavel().getNome());
				inputNomeResponsavel.setDisable(true);
				
			}else{
				inputNomeResponsavel.setText("");
			}
			
			break;
		}case 2:{
			
			if ( getObject().getFemea() != null ){
				
				servicoController.setState(State.CREATE_TO_SELECT);
				servicoController.object = new Servico("COBERTURA " + getObject().getFemea().getNumeroNome());
				servicoController.showForm(null);
				
				if ( servicoController.getObject() != null ){
					getObject().setServico(servicoController.getObject());
					inputNomeResponsavel.setText(servicoController.getObject().getPrestadorServico().getNome() 
							+ " [R$ " + servicoController.getObject().getValor() + "]");
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
		
		animalReducedController.object = new Animal(Sexo.FEMEA);
		animalReducedController.setSearch(searchFemeasAtivas);
		animalReducedController.showForm(animalReducedController.getFormName());
		
		if ( animalReducedController.getObject() != null && animalReducedController.getObject().getId() > 0 ){
			getObject().setFemea(animalReducedController.getObject());
			inputFemea.setText(animalReducedController.getObject().getNumeroNome());
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
	protected Cobertura getObject() {
		return (Cobertura)super.object;
	}

	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}
	
}
