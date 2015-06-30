package br.com.milksys.controller;

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

@Controller
public class CoberturaController extends AbstractController<Integer, Cobertura> {

	@FXML private TableColumn<Cobertura, String> dataColumn;
	@FXML private TableColumn<Animal, String> femeaColumn;
	@FXML private TableColumn<Cobertura, String> reprodutorColumn;
	@FXML private TableColumn<Cobertura, String> previsaoPartoColumn;
	@FXML private TableColumn<TipoCobertura, String> tipoCoberturaColumn;
	@FXML private TableColumn<SituacaoCobertura, String> situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> primeiroToqueColumn;
	@FXML private TableColumn<Cobertura, String> reconfirmacaoColumn;
	
	@FXML private UCTextField inputDescricao;
	@FXML private DatePicker inputData;
	@FXML private DatePicker inputPrevisaoParto;
	@FXML private ComboBox<String> inputResultadoToque;
	@FXML private UCTextField inputFemea;
	@FXML private UCTextField inputReprodutor;
	@FXML private UCTextField inputSemen;
	@FXML private UCTextField inputQuantidadeDosesSemen;
	@FXML private UCTextField inputNomeResponsavel;
	@FXML private ComboBox<String> inputResponsavelServico;
	@FXML private ComboBox<String> inputTipoCobertura;
	
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
	@Autowired private ServicoController servicoController;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
			femeaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("femea"));
			reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
			previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
			tipoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<TipoCobertura,String>("tipoCobertura"));
			situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<SituacaoCobertura,String>("situacaoCobertura"));
			primeiroToqueColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("primeiroToque"));
			primeiroToqueColumn.setCellFactory(new Callback<TableColumn<Cobertura,String>, TableCell<Cobertura,String>>(){
				@Override
				public TableCell<Cobertura, String> call(TableColumn<Cobertura, String> param) {
					TableCell<Cobertura, String> cell = new TableCell<Cobertura, String>(){
						@Override
						public void updateItem(String item, boolean empty) {
							if(item!=null){
								Hyperlink link = new Hyperlink();
								link.setText(item);
								link.setFocusTraversable(false);
								link.setOnAction(new EventHandler<ActionEvent>() {
								    @Override
								    public void handle(ActionEvent e) {
								    	setObject(data.get(getTableRow().getIndex()));
								    	state = State.PRIMEIRO_TOQUE;
								    	showForm("view/cobertura/ResultadoToqueForm.fxml");
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
								link.setText(item);
								link.setFocusTraversable(false);
								link.setOnAction(new EventHandler<ActionEvent>() {
								    @Override
								    public void handle(ActionEvent e) {
								    	setObject(data.get(getTableRow().getIndex()));
								    	state = State.RECONFIRMACAO;
								    	showForm("view/cobertura/ResultadoToqueForm.fxml");
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
			
			/*inputDataPrimeiroToque.valueProperty().bindBidirectional(getObject().dataPrimeiroToqueProperty());
			inputResultadoPrimeiroToque.textProperty().bindBidirectional(getObject().resultadoPrimeiroToqueProperty());
			inputDataReconfirmacao.valueProperty().bindBidirectional(getObject().dataReconfirmacaoProperty());
			inputResultadoReconfirmacao.textProperty().bindBidirectional(getObject().resultadoReconfirmacaoProperty());*/
			
			inputTipoCobertura.setItems(TipoCobertura.getItems());
			inputTipoCobertura.valueProperty().bindBidirectional(getObject().tipoCoberturaProperty());
			inputResponsavelServico.setItems(ResponsavelServico.getItems());
			inputNomeResponsavel.textProperty().bindBidirectional(getObject().nomeResponsavelProperty());
			inputFemea.setEditable(false);
			
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
			inputResultadoToque.setItems(SituacaoCobertura.getItems());
			inputResultadoToque.valueProperty().bindBidirectional(getObject().resultadoPrimeiroToqueProperty());
		}
		
		if ( state.equals(State.RECONFIRMACAO) ){
			inputData.valueProperty().bindBidirectional(getObject().dataReconfirmacaoProperty());
			inputResultadoToque.setItems(SituacaoCobertura.getItems());
			inputResultadoToque.valueProperty().bindBidirectional(getObject().resultadoReconfirmacaoProperty());
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
			inputReprodutor.prefWidthProperty().set(320);
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
		GridPane.setConstraints(inputReprodutor, 1, 4, 3, 1);
		
		dialogStage.show();
		
		btnNovoReprodutor.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
				
				animalReducedController.object = new Animal(Sexo.MACHO, FinalidadeAnimal.REPRODUCAO);;
				animalReducedController.setControllerOrigin(CoberturaController.class);
				
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
	private void changeResponsavelServico(){
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
	protected void handleNovaFemea() {
		
		animalReducedController.object = new Animal(Sexo.FEMEA);
		animalReducedController.setControllerOrigin(CoberturaController.class);
		animalReducedController.showForm(animalReducedController.getFormName());
		
		if ( animalReducedController.getObject() != null && animalReducedController.getObject().getId() > 0 ){
			getObject().setFemea(animalReducedController.getObject());
			inputFemea.setText(animalReducedController.getObject().getNumeroNome());
		}else{
			inputFemea.setText("");
		}
		
	}
	
	@Override
	protected boolean isInputValid() {
		
		if ( !super.isInputValid() ){
			return false;
		}
		
		if ( getObject().getTipoCobertura() == null ){
			CustomAlert.campoObrigatorio("tipo de cobertura");
			return false;
		}
		
		if ( getObject().getTipoCobertura().equals(TipoCobertura.MONTA_NATURAL) ){
			if ( getObject().getTouro() == null ){
				CustomAlert.campoObrigatorio("reprodutor");
				return false;
			}
		}
		
		if ( getObject().getTipoCobertura().equals(TipoCobertura.ENSEMINACAO_ARTIFICIAL) ){
			if ( getObject().getSemen() == null ){
				CustomAlert.campoObrigatorio("sêmen");
				return false;
			}
			
			if ( getObject().getQuantidadeDosesSemen() <= 0 ){
				CustomAlert.campoObrigatorio("doses de sêmen utilizadas");
				return false;
			}
		}
		
		if ( getObject().getNomeResponsavel() == null || getObject().getNomeResponsavel().isEmpty() ){
			CustomAlert.campoObrigatorio("responsável pela enseminação");
			return false;
		}
		
		return true;
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
