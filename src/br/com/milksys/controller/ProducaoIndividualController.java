package br.com.milksys.controller;

import java.util.Calendar;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomComboBoxCellFactory;
import br.com.milksys.components.CustomStringConverter;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.State;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.PrecoLeiteService;
import br.com.milksys.service.ProducaoIndividualService;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;
import br.com.milksys.util.Util;

@Controller
public class ProducaoIndividualController extends AbstractController<Integer, ProducaoIndividual> {

	@FXML private TableColumn<ProducaoIndividual, String> dataColumn;
	@FXML private TableColumn<ProducaoIndividual, String> animalColumn;
	@FXML private TableColumn<ProducaoIndividual, String> primeiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> segundaOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> terceiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> observacaoColumn;
	@FXML private TableColumn<ProducaoIndividual, String> valorColumn;
	
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputObservacao;
	@FXML private TextField inputPrimeiraOrdenha;
	@FXML private TextField inputSegundaOrdenha;
	@FXML private TextField inputTerceiraOrdenha;
	
	@FXML private ComboBox<Animal> inputAnimal;
	
	@FXML private Button btnOk;
	@FXML private Button btnAdicionar;
	@FXML private Button btnRemover;
	
	@Autowired private AnimalService animalService;
	@Autowired private ProducaoIndividualService producaoIndividualService;
	@Autowired private PrecoLeiteService precoLeiteService;
	
	private PrecoLeite precoLeite;
	
	private ObservableList<String> meses = Util.generateListMonths();
	
	@FXML@SuppressWarnings("unchecked")
	public void initialize() {
		
		super.service = producaoIndividualService;
		
		if ( state.equals(State.LIST) ){
			
			dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getData())));
			animalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAnimal().getNumeroNome())));
			primeiraOrdenhaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getPrimeiraOrdenha())));
			segundaOrdenhaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getSegundaOrdenha())));
			terceiraOrdenhaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getTerceiraOrdenha())));
			valorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValor())));
			observacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservacao()));
			
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			
			//@TODO filtrar apenas animais femeas 
			inputAnimal.setItems(animalService.findAllAsObservableList());
			inputAnimal.setCellFactory(new CustomComboBoxCellFactory<>("numeroNome"));
			inputAnimal.setConverter(new CustomStringConverter("numeroNome"));
			
			configureBinds();
			
		}
		
		if ( state.equals(State.INSERT_TO_SELECT) ){
			
			dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getData())));
			animalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroNomeAnimal())));
			primeiraOrdenhaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getPrimeiraOrdenha())));
			segundaOrdenhaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getSegundaOrdenha())));
			terceiraOrdenhaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getTerceiraOrdenha())));
			valorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValor())));
			
			data.clear();
			data.addAll(producaoIndividualService.findByDate(((ProducaoIndividual)object).getData()));
			table.setItems(data);
			
			inputData.setDisable(true);
			btnOk.setText("Adicionar");
			btnOk.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	handleOk();
			    }
			});			
			
			btnRemover.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			       handleDelete();
			    }
			});		
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) ){
			
			table.setVisible(false);
			btnRemover.setVisible(false);
			table.setMaxWidth(0);
			table.setMaxHeight(0);
			btnRemover.setMaxHeight(0);
			btnRemover.setMaxWidth(0);
			
		}
		
	}
	
	private void configureBinds(){
		inputAnimal.valueProperty().bindBidirectional(((ProducaoIndividual)object).animalProperty());
		inputData.valueProperty().bindBidirectional(((ProducaoIndividual)object).dataProperty());
		inputObservacao.textProperty().bindBidirectional(((ProducaoIndividual)object).observacaoProperty());
		inputPrimeiraOrdenha.textProperty().bindBidirectional(((ProducaoIndividual)object).primeiraOrdenhaProperty());
		inputSegundaOrdenha.textProperty().bindBidirectional(((ProducaoIndividual)object).segundaOrdenhaProperty());
		inputTerceiraOrdenha.textProperty().bindBidirectional(((ProducaoIndividual)object).terceiraOrdenhaProperty());
	}
	
	@Override
	protected void handleOk() {
		
		//força a atualização do objeto com os dados da tela
    	ProducaoIndividual producaoIndividual = (ProducaoIndividual)object;
    	producaoIndividual.setAnimal(inputAnimal.getSelectionModel().getSelectedItem());
    	producaoIndividual.setData(DateUtil.asDate(inputData.getValue()));
    	producaoIndividual.setPrimeiraOrdenha(NumberFormatUtil.fromString(inputPrimeiraOrdenha.getText()));
    	producaoIndividual.setSegundaOrdenha(NumberFormatUtil.fromString(inputSegundaOrdenha.getText()));
    	producaoIndividual.setTerceiraOrdenha(NumberFormatUtil.fromString(inputTerceiraOrdenha.getText()));
    	producaoIndividual.setObservacao(inputObservacao.getText());
    	
    	if ( precoLeite == null ){
    		Calendar date = Calendar.getInstance();
    		date.setTime(producaoIndividual.getData());
    		precoLeite = precoLeiteService.findByMesAno(meses.get(date.get(Calendar.MONTH)), date.get(Calendar.YEAR));
    	}
    	
    	producaoIndividual.setPrecoLeite(precoLeite);
    	
    	//verifica se já existe registro para o mesmo animal no mesmo dia
    	for ( int index = 0; index < data.size(); index++ ){
    		ProducaoIndividual p = data.get(index);
    		
			if ( p.getData().equals(producaoIndividual.getData()) && 
					p.getAnimal().getId() == producaoIndividual.getAnimal().getId() ){
				
				//atualiza o volume para atualizar a table view
				p.setPrimeiraOrdenha(producaoIndividual.getPrimeiraOrdenha());
				p.setSegundaOrdenha(producaoIndividual.getSegundaOrdenha());
				p.setTerceiraOrdenha(producaoIndividual.getTerceiraOrdenha());
				
				//seta o id para fazer update e não insert
				producaoIndividual = p;
				data.set(index, p);
				break;
			}
			
		}
    	
    	//salva o objeto
    	service.save(producaoIndividual);
    	
    	//adiciona na tabela somente novos registros
    	if ( producaoIndividual.getId() <= 0 )
    		data.add(producaoIndividual);
    	
		data.clear();
		data.addAll(producaoIndividualService.findByDate(producaoIndividual.getData()));
		table.setItems(data);
    	
    	if ( !state.equals(State.INSERT_TO_SELECT) ){
    		object = new ProducaoIndividual();
    		super.dialogStage.close();
    		super.updateLabelNumRegistros();
    		super.state = State.LIST;
    	}else{
        	producaoIndividual = new ProducaoIndividual();
        	producaoIndividual.setData(((ProducaoIndividual)object).getData());
        	object = producaoIndividual;
    		configureBinds();
    	}
    	
    }
	
	@Override
	protected void showForm(int width, int height) {
		
		if ( state.equals(State.INSERT_TO_SELECT) ){
			super.showForm(758, 328);
		}else{
			super.showForm(668, 180);	
		}
		
	}

	@Override
	protected boolean isInputValid() {
		
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/producaoIndividual/ProducaoIndividualForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Produção Individual";
	}

	public void setPrecoLeite(PrecoLeite precoLeite) {
		this.precoLeite = precoLeite;
	}

}
