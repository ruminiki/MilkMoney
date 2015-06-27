package br.com.milksys.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Animal;
import br.com.milksys.model.PrecoLeite;
import br.com.milksys.model.ProducaoIndividual;
import br.com.milksys.model.State;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.PrecoLeiteService;
import br.com.milksys.service.ProducaoIndividualService;

@Controller
public class ProducaoIndividualController extends AbstractController<Integer, ProducaoIndividual> {

	@FXML private TableView<Animal> tableAnimal; 
	@FXML private TableColumn<Animal, String> animalColumn;
	
	@FXML private TableColumn<Animal, String> animalListColumn;
	@FXML private TableColumn<ProducaoIndividual, LocalDate> dataColumn;
	@FXML private TableColumn<ProducaoIndividual, String> primeiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> segundaOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> terceiraOrdenhaColumn;
	@FXML private TableColumn<ProducaoIndividual, String> observacaoColumn;
	@FXML private TableColumn<ProducaoIndividual, String> valorColumn;
	
	@FXML private DatePicker inputData;
	@FXML private UCTextField inputObservacao;
	@FXML private UCTextField inputAnimal;
	@FXML private TextField inputPrimeiraOrdenha;
	@FXML private TextField inputSegundaOrdenha;
	@FXML private TextField inputTerceiraOrdenha;
	
	@FXML private Button btnOk;
	@FXML private Button btnAdicionar;
	@FXML private Button btnRemover;
	
	@Autowired private AnimalService animalService;
	@Autowired private ProducaoIndividualService producaoIndividualService;
	@Autowired private PrecoLeiteService precoLeiteService;
	
	private Animal selectedAnimal;
	
	@FXML
	public void initialize() {
		
		super.service = producaoIndividualService;
		
		if ( state.equals(State.LIST) ){
			
			//@TODO filtrar apenas animais femeas 
			tableAnimal.setItems(animalService.findAllAsObservableList());
			tableAnimal.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> findByAnimal(newValue));
			animalColumn.setCellValueFactory(new PropertyValueFactory<Animal, String>("numeroNome"));
		
			animalListColumn.setCellValueFactory(new PropertyValueFactory<Animal, String>("animal"));
			dataColumn.setCellFactory(new TableCellDateFactory<ProducaoIndividual, LocalDate>("data"));
			primeiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("primeiraOrdenha"));
			segundaOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("segundaOrdenha"));
			terceiraOrdenhaColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("terceiraOrdenha"));
			valorColumn.setCellValueFactory(new PropertyDecimalValueFactory<ProducaoIndividual, String>("valor"));
			observacaoColumn.setCellValueFactory(new PropertyValueFactory<ProducaoIndividual, String>("observacao"));
			
			super.initialize();
			
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputData.valueProperty().bindBidirectional(getObject().dataProperty());
			inputAnimal.setText(selectedAnimal.getNumeroNome());
			getObject().setAnimal(selectedAnimal);
			inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
			inputPrimeiraOrdenha.textProperty().bindBidirectional(getObject().primeiraOrdenhaProperty());
			inputSegundaOrdenha.textProperty().bindBidirectional(getObject().segundaOrdenhaProperty());
			inputTerceiraOrdenha.textProperty().bindBidirectional(getObject().terceiraOrdenhaProperty());
		}
		
		/*if ( state.equals(State.INSERT_TO_SELECT) ){
			
			dataColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
			animalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroNomeAnimal()));
			primeiraOrdenhaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getPrimeiraOrdenha())));
			segundaOrdenhaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getSegundaOrdenha())));
			terceiraOrdenhaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getTerceiraOrdenha())));
			valorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(NumberFormatUtil.decimalFormat(cellData.getValue().getValor())));
			
			data.clear();
			data.addAll(producaoIndividualService.findByDate(getObject().getData()));
			table.setItems(data);
			
			super.initialize();
			
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
			
		}*/
		
		/*if ( state.equals(State.INSERT) || state.equals(State.UPDATE) ){
			
			table.setVisible(false);
			btnRemover.setVisible(false);
			table.setMaxWidth(0);
			table.setMaxHeight(0);
			btnRemover.setMaxHeight(0);
			btnRemover.setMaxWidth(0);
			
		}*/
		
	}
	
	
	@Override
	protected void initializeTableOverview() {
		data.clear();
		
		if ( selectedAnimal == null || selectedAnimal.getId() <= 0 ){
			if ( tableAnimal.getItems() != null && tableAnimal.getItems().size() > 0 ){
				table.getSelectionModel().select(0);
				selectedAnimal = tableAnimal.getItems().get(0);
				initializeTableOverview();
			}	
		}else{
			data.addAll(producaoIndividualService.findByAnimal(selectedAnimal));
			
		}
		atualizaValorProducao();
		updateLabelNumRegistros();
		
		//this.precoLeite = precoLeiteService.findByMesAno(meses.get(selectedMesReferencia-1), selectedAnoReferencia);
		//service.updatePrecoLeitePeriodo(precoLeite, DateUtil.asDate(dataInicioMes()), DateUtil.asDate(dataFimMes()));
		
	}
	
	/**
	 * Método que percorre lista de objetos atualizando o valor com base no preço do leite do mês
	 */
	private void atualizaValorProducao(){
		
		//varre a tabela atualizando os valores diários
		for ( int index = 0; index < data.size(); index++ ){
			ProducaoIndividual producaoIndividual = data.get(index);
			PrecoLeite precoLeite = precoLeiteService.findByMesAno(producaoIndividual.getMes(), producaoIndividual.getAno());
			producaoIndividual.setValor(precoLeite.getValor().multiply(producaoIndividual.getTotalProducaoDia()));
			data.set(index, producaoIndividual);
		}
		
	}

	private void findByAnimal(Animal animal) {
		selectedAnimal = animal;
		initializeTableOverview();
	}

	@Override
	protected void handleOk() {
		
		ProducaoIndividual producaoIndividual = producaoIndividualService.findByAnimalAndData(getObject().getAnimal(), getObject().getData());
		if ( producaoIndividual != null ){
			producaoIndividual.setPrimeiraOrdenha(getObject().getPrimeiraOrdenha());
			producaoIndividual.setSegundaOrdenha(getObject().getSegundaOrdenha());
			producaoIndividual.setTerceiraOrdenha(getObject().getTerceiraOrdenha());
			super.setObject(producaoIndividual);
		}
		
		PrecoLeite precoLeite = precoLeiteService.findByMesAno(getObject().getMes(), getObject().getAno());
		if ( precoLeite != null ){
			getObject().setValor(precoLeite.getValor().multiply(getObject().getTotalProducaoDia()));
		}
		
		super.handleOk();
		
		/*
		//forÃ§a a atualizaÃ§Ã£o do objeto com os dados da tela
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
    	
    	//verifica se jÃ¡ existe registro para o mesmo animal no mesmo dia
    	for ( int index = 0; index < data.size(); index++ ){
    		ProducaoIndividual p = data.get(index);
    		
			if ( p.getData().equals(producaoIndividual.getData()) && 
					p.getAnimal().getId() == producaoIndividual.getAnimal().getId() ){
				
				//atualiza o volume para atualizar a table view
				p.setPrimeiraOrdenha(producaoIndividual.getPrimeiraOrdenha());
				p.setSegundaOrdenha(producaoIndividual.getSegundaOrdenha());
				p.setTerceiraOrdenha(producaoIndividual.getTerceiraOrdenha());
				
				//seta o id para fazer update e nÃ£o insert
				producaoIndividual = p;
				data.set(index, p);
				break;
			}
			
		}
    	
    	//adiciona na tabela somente novos registros
    	if ( producaoIndividual.getId() <= 0 )
    		data.add(producaoIndividual);
    	
    	//salva o objeto
    	service.save(producaoIndividual);
    	
    	//data.clear();
    	
    	if ( !state.equals(State.INSERT_TO_SELECT) ){
    		object = new ProducaoIndividual();
    		super.dialogStage.close();
    		super.updateLabelNumRegistros();
    		super.state = State.LIST;
    		//data.addAll(producaoIndividualService.findAll());
    	}else{
    		//data.addAll(producaoIndividualService.findByDate(producaoIndividual.getData()));
        	object = new ProducaoIndividual(producaoIndividual.getData());
    		configureBinds();
    	}
    	
		//table.setItems(data);
    */	
    }
	
	/*@Override
	protected void showForm(int width, int height) {
		
		if ( state.equals(State.INSERT_TO_SELECT) ){
			super.showForm(758, 328);
		}else{
			super.showForm(668, 180);	
		}
		
	}*/

	@Override
	protected boolean isInputValid() {
		
		if ( getObject().getAnimal() == null ){
			CustomAlert.campoObrigatorio("animal");
			return false;
		}
		
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

	@Override
	protected ProducaoIndividual getObject() {
		return (ProducaoIndividual) super.object;
	}

}
