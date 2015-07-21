package br.com.milksys.controller.fichaAnimal;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.TableCellIndexFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FichaAnimal;
import br.com.milksys.model.Indicador;
import br.com.milksys.model.Lactacao;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.FichaAnimalService;
import br.com.milksys.service.indicadores.IndicadorService;

@Controller
public class FichaAnimalOverviewController extends AbstractOverviewController<Integer, FichaAnimal>{

	@FXML private TableView<FichaAnimal> tableEventos;
	@FXML private TableColumn<FichaAnimal, String> dataColumn;
	@FXML private TableColumn<FichaAnimal, String> eventoColumn;
	@FXML private UCTextField inputPesquisaEventos;
	
	@FXML private TableView<Lactacao> tableLactacoes;
	@FXML private TableColumn<Lactacao, String> numeroLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> dataInicioLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> dataTerminoLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> diasEmLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> mesesEmLactacaoColumn;
	
	@FXML private Label lblHeader;
	
	@FXML private TableView<Indicador> tableIndicadores;
	@FXML private TableColumn<Indicador, String> indicadorColumn;
	@FXML private TableColumn<Indicador, String> valorApuradoColumn;
	
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private AnimalService animalService;
	@Autowired private IndicadorService indicadorService;
	
	private Animal animal;
	//ObservableList<FichaAnimal> eventos = FXCollections.observableArrayList();
	
	@FXML
	public void initialize() {
		
		//tabela eventos
		dataColumn.setCellFactory(new TableCellDateFactory<FichaAnimal,String>("data"));
		eventoColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("evento"));
		//filter over table view eventos
		FilteredList<FichaAnimal> filteredData = new FilteredList<>(fichaAnimalService.findAllByAnimal(animal), ficha -> true);
		inputPesquisaEventos.textProperty().addListener(obs->{
	        String filter = inputPesquisaEventos.getText(); 
	        if(filter == null || filter.length() == 0) {
	            filteredData.setPredicate(ficha -> true);
	        }else {
	            filteredData.setPredicate(ficha -> ficha.getEvento().contains(filter));
	        }
	    });
        SortedList<FichaAnimal> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableEventos.comparatorProperty());
		tableEventos.setItems(sortedData);
		
		//tabela lactações
		numeroLactacaoColumn.setCellFactory(new TableCellIndexFactory<Lactacao,String>());
		dataInicioLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataInicio"));
		dataTerminoLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataFim"));
		diasEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("diasLactacao"));
		mesesEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("mesesLactacao"));
		
		tableLactacoes.getItems().clear();
		tableLactacoes.setItems(animalService.findLactacoesAnimal(animal));
		
		lblHeader.setText("FICHA ANIMAL " + animal.toString());
		
		//tabela indicadores
		indicadorColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("descricao"));
		valorApuradoColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("valorApurado"));
		
		tableIndicadores.setItems(indicadorService.findAllAsObservableList());
		
	}
	
	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public String getFormTitle() {
		return "Eventos Animal";
	}
	
	public String getFormName() {
		return "view/fichaAnimal/FichaAnimalOverview.fxml";
	}
	
}
