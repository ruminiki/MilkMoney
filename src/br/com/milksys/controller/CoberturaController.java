package br.com.milksys.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Funcionario;
import br.com.milksys.model.ResponsavelServico;
import br.com.milksys.model.Semen;
import br.com.milksys.model.Servico;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.State;
import br.com.milksys.model.TipoCobertura;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.FuncionarioService;
import br.com.milksys.service.IService;
import br.com.milksys.service.SemenService;
import br.com.milksys.service.ServicoService;

@Controller
public class CoberturaController extends AbstractController<Integer, Cobertura> {

	@FXML private TableColumn<Cobertura, String> dataColumn;
	@FXML private TableColumn<Animal, String> femeaColumn;
	@FXML private TableColumn<Cobertura, String> reprodutorColumn;
	@FXML private TableColumn<Cobertura, String> previsaoPartoColumn;
	@FXML private TableColumn<TipoCobertura, String> tipoCoberturaColumn;
	@FXML private TableColumn<SituacaoCobertura, String> situacaoCoberturaColumn;
	
	@FXML private UCTextField inputDescricao;
	@FXML private DatePicker inputData;
	@FXML private DatePicker inputPrevisaoParto;
	@FXML private DatePicker inputDataPrimeiroToque;
	@FXML private UCTextField inputResultadoPrimeiroToque;
	@FXML private DatePicker inputDataReconfirmacao;
	@FXML private UCTextField inputResultadoReconfirmacao;
	@FXML private ComboBox<String> inputTipoCobertura;
	@FXML private ComboBox<Animal> inputFemea;
	@FXML private ComboBox<Animal> inputReprodutor;
	@FXML private ComboBox<Semen> inputSemen;
	@FXML private TextField inputQuantidadeDosesSemen;
	@FXML private ComboBox<Funcionario> inputFuncionarioResponsavel;
	@FXML private ComboBox<String> inputResponsavelServico;
	@FXML private UCTextField inputNomeResponsavel;
	
	@FXML private Button btnNovoReprodutor;
	@FXML private Label lblReprodutor;
	
	@FXML private GridPane gridPane;
	
	//services
	@Autowired private AnimalService animalService;
	@Autowired private SemenService semenService;
	@Autowired private FuncionarioService funcionarioService;
	@Autowired private ServicoService servicoService;
	
	//controllers
	@Autowired private AnimalController animalController;
	@Autowired private SemenController semenController;
	@Autowired private FuncionarioController funcionarioController;
	@Autowired private FuncionarioReducedController funcionarioReducedController;
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
			
			inputTipoCobertura.setItems(FXCollections.observableArrayList(TipoCobertura.MONTA_NATURAL, TipoCobertura.ENSEMINACAO_ARTIFICIAL));
			inputTipoCobertura.valueProperty().bindBidirectional(getObject().tipoCoberturaProperty());
			
			inputFemea.setItems(animalService.findAllFemeasAsObservableList());
			inputFemea.valueProperty().bindBidirectional(getObject().femeaProperty());
			
			inputResponsavelServico.setItems(ResponsavelServico.getItems());
			
			getObject().setSituacaoCobertura(SituacaoCobertura.INDEFINIDA);
			
			if ( getObject().getFuncionarioResponsavel() != null ){
				inputResponsavelServico.getSelectionModel().select(1);
			}else{
				if ( getObject().getServico() != null ){
					inputResponsavelServico.setDisable(getObject().getServico() != null);
					inputResponsavelServico.getSelectionModel().select(3);
				}else{
					
				}
			}
			
			MaskFieldUtil.numeroInteiro(inputQuantidadeDosesSemen);
			
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
				
				lblReprodutor.setText("Reprodutor: ");
				
				if ( inputReprodutor == null ){
					inputReprodutor = new ComboBox<Animal>();
					inputReprodutor.prefWidthProperty().set(320);
				}
				inputReprodutor.setItems(animalService.findAllReprodutoresAsObservableList());

				gridPane.getChildren().set(15, inputReprodutor);
				GridPane.setConstraints(inputReprodutor, 1, 4, 3, 1);
				dialogStage.show();
				
				btnNovoReprodutor.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	animalController.state = State.INSERT_TO_SELECT;
						
						Animal touro = new Animal();
						touro.setSexo(Sexo.MACHO);
						touro.setFinalidadeAnimal(FinalidadeAnimal.REPRODUCAO);
						
						animalController.object = touro;
						
						//animalController.inputSexo.setDisable(true);
						//animalController.inputFinalidadeAnimal.setDisable(true);
						
						animalController.showForm(null);
						if ( animalController.getObject() != null && animalController.getObject().getId() > 0 ){
							inputReprodutor.getItems().add(animalController.getObject());
							inputReprodutor.getSelectionModel().select(animalController.getObject());
						}
				    }
				    
				});
			}else{
				
				if ( inputSemen == null ){
					inputSemen = new ComboBox<Semen>();
					inputSemen.prefWidthProperty().set(320);
				}
				inputSemen.setItems(semenService.findAllAsObservableList());

				gridPane.getChildren().set(15, inputSemen);
				GridPane.setConstraints(inputSemen, 1, 4, 3, 1);
				dialogStage.show();

				lblReprodutor.setText("Sêmen: ");
				
				inputQuantidadeDosesSemen.textProperty().bindBidirectional(getObject().quantidadeDosesSemenProperty());
				inputQuantidadeDosesSemen.setText("1");
				
				btnNovoReprodutor.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	semenController.state = State.INSERT_TO_SELECT;
						semenController.object = new Semen();
						semenController.showForm(null);
						if ( semenController.getObject() != null && semenController.getObject().getId() > 0 ){
							inputSemen.getItems().add(semenController.getObject());
							inputSemen.getSelectionModel().select(semenController.getObject());
						}
				    }
				});
				
			}
		}
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
			//abre uma tela reduzida para seleção do funcionario
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
				servicoController.state = State.INSERT_TO_SELECT;
				Servico servico = new Servico();
				servico.setDescricao("COBERTURA " + getObject().getFemea().getNumeroNome());
				servicoController.object = servico;
				servicoController.showForm(null);
				if ( servicoController.getObject() != null && servicoController.getObject().getId() > 0 ){
					getObject().setServico(servicoController.getObject());
					inputNomeResponsavel.setText(servicoController.getObject().getPrestadorServico().getNome() 
							+ " [R$ " + servicoController.getObject().getValor() + "]");
				}else{
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
	
	/**
	 * Caso o usuário deseje remover o serviço associado
	 */
	@FXML
	private void removerServico(){
		if ( getObject().getServico() != null ){
			servicoService.remove(getObject().getServico());
			getObject().setServico(null);
		}
	}
	
	@Override
	protected boolean isInputValid() {
		return true;
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
	
	@FXML
	protected void handleNovaFemea() {
		animalController.state = State.INSERT_TO_SELECT;
		Animal femea = new Animal();
		femea.setSexo(Sexo.FEMEA);
		animalController.object = femea;
		//animalController.inputSexo.setDisable(true);
		animalController.showForm(null);
		if ( animalController.getObject() != null && animalController.getObject().getId() > 0 ){
			inputFemea.getItems().add(animalController.getObject());
			inputFemea.getSelectionModel().select(animalController.getObject());
		}
	}
	
	@FXML
	protected void handleNovoFuncionario() {
		funcionarioController.state = State.INSERT_TO_SELECT;
		funcionarioController.object = new Funcionario();
		funcionarioController.showForm(null);
		if ( funcionarioController.getObject() != null && funcionarioController.getObject().getId() > 0 ){
			inputFuncionarioResponsavel.getItems().add(funcionarioController.getObject());
			inputFuncionarioResponsavel.getSelectionModel().select(funcionarioController.getObject());
		}
	}

}
