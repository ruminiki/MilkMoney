package br.com.milksys.controller.parto;

import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.TableCellHyperlinkRemoverFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractFormController;
import br.com.milksys.controller.animal.AnimalCriaFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.ComplicacaoParto;
import br.com.milksys.model.Cria;
import br.com.milksys.model.Lactacao;
import br.com.milksys.model.Parto;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SimNao;
import br.com.milksys.model.SituacaoNascimento;
import br.com.milksys.model.State;
import br.com.milksys.model.TipoCobertura;
import br.com.milksys.model.TipoParto;
import br.com.milksys.service.IService;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;
import br.com.milksys.validation.CriaValidation;
import br.com.milksys.validation.PartoValidation;

@Controller
public class PartoFormController extends AbstractFormController<Integer, Parto> {

	@FXML private TableView<Cria> table;
	@FXML private TableColumn<Cria, String> animalColumn;
	@FXML private TableColumn<Cria, String> sexoColumn;
	@FXML private TableColumn<Cria, String> incorporadoAoRebanhoColumn;
	@FXML private TableColumn<Cria, String> situacaoNascimentoColumn;
	@FXML private TableColumn<Cria, String> removerColumn;
	
	@FXML private UCTextField inputCobertura;
	@FXML private DatePicker inputData, inputDataInicioLactacao;
	@FXML private UCTextField inputObservacao;
	@FXML private ComboBox<String> inputSituacaoNascimento;
	@FXML private ComboBox<String> inputSexo;
	@FXML private ComboBox<String> inputIncorporadoAoRebanho;
	@FXML private ComboBox<String> inputTipoParto;
	@FXML private ComboBox<String> inputComplicacaoParto;
	@FXML private UCTextField inputPeso;
	
	@FXML private Button btnSalvar, btnAdicionarCria;
	
	private Cria cria;
	
	@Autowired private AnimalCriaFormController animalCriaFormController;
	
	private ObservableList<Cria> data = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		
		//parto
		inputCobertura.setText(getObject().getCobertura().toString());
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		inputTipoParto.setItems(TipoParto.getItems());
		inputTipoParto.valueProperty().bindBidirectional(getObject().tipoPartoProperty());
		inputComplicacaoParto.setItems(ComplicacaoParto.getItems());
		inputComplicacaoParto.valueProperty().bindBidirectional(getObject().complicacaoPartoProperty());
		
		
		if ( getObject().getId() <= 0 ){
			cria = new Cria(getObject());
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
		
		data.clear();
		data.addAll(getObject().getCrias());
		table.setItems(data);
		
	}
	
	@Override
	protected void beforeSave() {
		super.beforeSave();
		
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
		animal.setDataNascimento(DateUtil.asDate(inputData.getValue()));
		
		Cobertura cobertura = getObject().getCobertura();
		
		animal.setMae(cobertura.getFemea());
		
		if ( cobertura.getTipoCobertura().equals(TipoCobertura.MONTA_NATURAL) ){
			animal.setPaiMontaNatural(cobertura.getTouro());
		}else{
			animal.setPaiEnseminacaoArtificial(cobertura.getSemen().getTouro());
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
		
		PartoValidation.validate(getObject());
		
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
	
	@Override
	public String getFormName() {
		return "view/parto/PartoForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Parto";
	}
	
	@Override
	@Resource(name = "partoService")
	protected void setService(IService<Integer, Parto> service) {
		super.setService(service);
	}

}
