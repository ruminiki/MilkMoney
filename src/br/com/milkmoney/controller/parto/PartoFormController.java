package br.com.milkmoney.controller.parto;

import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.TableCellHyperlinkRemoverFactory;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.animal.AnimalCriaFormController;
import br.com.milkmoney.controller.complicacaoParto.ComplicacaoPartoReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.Cria;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.model.Parto;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SimNao;
import br.com.milkmoney.model.SituacaoNascimento;
import br.com.milkmoney.model.State;
import br.com.milkmoney.model.TipoCobertura;
import br.com.milkmoney.model.TipoParto;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.ComplicacaoPartoService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;
import br.com.milkmoney.validation.CriaValidation;
import br.com.milkmoney.validation.PartoValidation;

@Controller
public class PartoFormController extends AbstractFormController<Integer, Parto> {

	@FXML private TableView<Cria>            table;
	@FXML private TableColumn<Cria, String>  animalColumn;
	@FXML private TableColumn<Cria, String>  sexoColumn;
	@FXML private TableColumn<Cria, String>  incorporadoAoRebanhoColumn;
	@FXML private TableColumn<Cria, String>  situacaoNascimentoColumn;
	@FXML private TableColumn<Cria, String>  removerColumn;
	 
	@FXML private UCTextField                inputCobertura;
	@FXML private DatePicker                 inputData, inputDataInicioLactacao;
	@FXML private UCTextField                inputObservacao;
	@FXML private ChoiceBox<String>          inputSituacaoNascimento;
	@FXML private ChoiceBox<String>          inputSexo;
	@FXML private ChoiceBox<String>          inputIncorporadoAoRebanho;
	@FXML private ChoiceBox<String>          inputTipoParto;
	@FXML private UCTextField                inputComplicacaoParto;
	@FXML private UCTextField                inputPeso;
	
	@FXML private Button btnSalvar, btnAdicionarCria, btnRemover;
	
	private Cria cria;
	
	@Autowired private AnimalCriaFormController animalCriaFormController;
	@Autowired private ComplicacaoPartoReducedOverviewController complicacaoPartoReducedOverviewController;
	@Autowired private CoberturaService coberturaService;
	@Autowired private ComplicacaoPartoService complicacaoPartoService;
	
	private ObservableList<Cria> data = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		//parto
		inputCobertura.setText(getObject().getCobertura().toString());
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputDataInicioLactacao.valueProperty().bindBidirectional(getObject().dataInicioLactacaoProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputTipoParto.setItems(TipoParto.getItems());
		inputTipoParto.valueProperty().bindBidirectional(getObject().tipoPartoProperty());
		
		if ( getObject().getComplicacaoParto() != null ){
			inputComplicacaoParto.setText(getObject().getComplicacaoParto().getDescricao());
		}
		
		if ( getObject().getId() <= 0 ){
			cria = new Cria(getObject());
			inputData.setValue(DateUtil.asLocalDate(getObject().getCobertura().getPrevisaoParto()));
			inputDataInicioLactacao.setValue(inputData.getValue().plusDays(3));
		}
		
		//cria
		inputIncorporadoAoRebanho.setItems(SimNao.getItems());
		inputSexo.setItems(Sexo.getItems());
		inputSituacaoNascimento.setItems(SituacaoNascimento.getItems());
		
		MaskFieldUtil.numeroInteiro(inputPeso);

		//table crias
		animalColumn.setCellValueFactory(new PropertyValueFactory<Cria,String>("animalFormatado"));
		sexoColumn.setCellValueFactory(new PropertyValueFactory<Cria,String>("sexo"));
		incorporadoAoRebanhoColumn.setCellValueFactory(new PropertyValueFactory<Cria,String>("incorporadoAoRebanho"));
		situacaoNascimentoColumn.setCellValueFactory(new PropertyValueFactory<Cria,String>("situacaoNascimento"));
		removerColumn.setCellValueFactory(new PropertyValueFactory<Cria,String>("situacaoNascimento"));
		removerColumn.setCellFactory(new TableCellHyperlinkRemoverFactory<Cria, String>(removerCriaParto, getObject().getId() > 0));
		
		btnAdicionarCria.setDisable(getObject().getId() > 0 );
		btnSalvar.setDisable(getObject().getId() > 0 );
		inputData.setDisable(getObject().getId() > 0 );
		inputObservacao.setDisable(getObject().getId() > 0);
		
		btnRemover.setDisable(getObject().getId() <= 0);
		
		data.clear();
		data.addAll(getObject().getCrias());
		table.setItems(data);
		
	}
	
	/**
	 * Quando a data é informada atualiza a previsão de parto
	 */
	@FXML
	private void updateDataInicioLactacao(){
		if ( inputData.getValue() != null ){
			getObject().setDataInicioLactacao(DateUtil.asDate(inputData.getValue().plusDays(3)));
		}
	}
	
	@FXML
	private void handleSelecionarComplicacaoParto(){
		
		complicacaoPartoReducedOverviewController.showForm();
		
		if ( complicacaoPartoReducedOverviewController.getObject() != null && complicacaoPartoReducedOverviewController.getObject().getId() > 0 ){
			getObject().setComplicacaoParto(complicacaoPartoReducedOverviewController.getObject());
		}
		
		if ( getObject().getComplicacaoParto() != null ){
			inputComplicacaoParto.textProperty().set(getObject().getComplicacaoParto().toString());	
		}else{
			inputComplicacaoParto.textProperty().set("");
		}
		
	}
	
	@Override
	protected void beforeSave() {
		super.beforeSave();
		
		PartoValidation.validate(getObject());
		
		Lactacao lactacao = new Lactacao();
		lactacao.setParto(getObject());
		lactacao.setAnimal(getObject().getCobertura().getFemea());
		lactacao.setDataInicio(DateUtil.asDate(inputDataInicioLactacao.getValue()));
		
		getObject().setLactacao(lactacao);
		
	}
	
	private void cadastrarAnimal() {
		
		animalCriaFormController.setState(State.CREATE_TO_SELECT);
		Animal animal = new Animal(cria.getSexo());
		animal.setPeso(cria.getPeso());
		animal.setSexo(inputSexo.getSelectionModel().getSelectedItem());
		animal.setRaca(getObject().getCobertura().getFemea().getRaca());
		animal.setDataNascimento(DateUtil.asDate(inputData.getValue()));
		
		Cobertura cobertura = getObject().getCobertura();
		
		animal.setMae(cobertura.getFemea());
		
		if ( cobertura.getTipoCobertura().equals(TipoCobertura.MONTA_NATURAL) ){
			animal.setPaiMontaNatural(cobertura.getTouroMontaNatural());
		}else{
			animal.setPaiEnseminacaoArtificial(cobertura.getTouroInseminacaoArtificial());
		}
		
		animalCriaFormController.setObject(animal);
		animalCriaFormController.showForm();
		
		if ( animalCriaFormController.getObject() != null ){
			cria.setAnimal(animalCriaFormController.getObject());
			handleAdicionarCria();
		}else{
			cria.setAnimal(null);
		}
			
	}
	
	@FXML
	protected void handleAdicionarCria() {
		
		if ( inputIncorporadoAoRebanho.getValue() != null && 
				inputIncorporadoAoRebanho.getValue().equals(SimNao.SIM) ){
			
			if ( inputSituacaoNascimento.getValue() != null &&
					inputSituacaoNascimento.getValue().equals(SituacaoNascimento.NASCIDO_VIVO) ){
				
				if ( cria.getAnimal() == null ){
					cadastrarAnimal();
					return;
				}
				
			}
			
		}
		
		cria.setIncorporadoAoRebanho(inputIncorporadoAoRebanho.getValue());
		cria.setParto(getObject());
		cria.setPeso(NumberFormatUtil.fromString(inputPeso.getText()));
		cria.setSexo(inputSexo.getValue());
		cria.setSituacaoNascimento(inputSituacaoNascimento.getValue());
		
		CriaValidation.validate(cria);
		
		this.getObject().getCrias().add(cria);
		data.clear();
		data.addAll(getObject().getCrias());
		
		inputPeso.setText("");
		inputIncorporadoAoRebanho.getSelectionModel().clearSelection();
		inputSexo.getSelectionModel().clearSelection();
		inputSituacaoNascimento.getSelectionModel().clearSelection();
		
		cria = new Cria(getObject());
		
	}
	
	Function<Integer, Boolean> removerCriaParto = index -> {
		
		if ( index <= table.getItems().size() ){
			
			if ( !getObject().getCrias().remove(index) ){
				getObject().getCrias().remove(table.getItems().get(index));
			}
			table.getItems().clear();
			table.getItems().addAll(getObject().getCrias());
			
			return true;
		}
		return false;
	};
	
	@FXML
	private void handleRemover(){
		CustomAlert.confirmarExclusao();
		if (CustomAlert.response == ButtonType.OK) {
			coberturaService.removerParto(getObject().getCobertura());
			closeForm();
		}
	}
	
	@Override
	public String getFormName() {
		return "view/parto/PartoForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Parto";
	}
	
	@Override
	@Resource(name = "partoService")
	protected void setService(IService<Integer, Parto> service) {
		super.setService(service);
	}

}
