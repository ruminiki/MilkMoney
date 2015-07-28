package br.com.milkmoney.controller.fichaAnimal;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.TableCellIndexFactory;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.indicadores.IndicadorService;

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
	
	@FXML private TableView<FichaAnimal> tableFichaAnimal;
	@FXML private TableColumn<FichaAnimal, String> ultimaCoberturaColumn;
	@FXML private TableColumn<FichaAnimal, String> numeroServicosColumn;
	@FXML private TableColumn<FichaAnimal, String> proximoServicoColumn;
	@FXML private TableColumn<FichaAnimal, String> numeroPartosColumn;
	@FXML private TableColumn<FichaAnimal, String> criasFemeasColumn;
	@FXML private TableColumn<FichaAnimal, String> criasMachosColumn;
	@FXML private TableColumn<FichaAnimal, String> diasEmAbertoColumn;
	@FXML private TableColumn<FichaAnimal, String> diasEmLactacaoAnimalColumn;
	@FXML private TableColumn<FichaAnimal, String> intervaloEntrePartosColumn;
	@FXML private TableColumn<FichaAnimal, String> idadePrimeiroPartoColumn;
	@FXML private TableColumn<FichaAnimal, String> idadePrimeiraCoberturaColumn;
	
	
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private AnimalService animalService;
	@Autowired private IndicadorService indicadorService;
	
	private Animal animal;
	
	@FXML
	public void initialize() {
		
		//tabela eventos
		dataColumn.setCellFactory(new TableCellDateFactory<FichaAnimal,String>("data"));
		eventoColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("evento"));
		//filter over table view eventos
		FilteredList<FichaAnimal> filteredData = new FilteredList<>(fichaAnimalService.findAllEventosByAnimal(animal), ficha -> true);
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
		ultimaCoberturaColumn.setCellFactory(new TableCellDateFactory<FichaAnimal,String>("dataUltimaCobertura"));
		numeroServicosColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("numeroServicosAtePrenhes"));
		proximoServicoColumn.setCellFactory(new TableCellDateFactory<FichaAnimal,String>("proximoServico"));
		numeroPartosColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("numeroPartos"));
		criasFemeasColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("numeroCriasFemea"));
		criasMachosColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("numeroCriasMacho"));
		diasEmAbertoColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("diasEmAberto"));
		diasEmLactacaoAnimalColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("diasEmLactacao"));
		intervaloEntrePartosColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("intervaloEntrePartos"));
		idadePrimeiroPartoColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("idadePrimeiroParto"));
		idadePrimeiraCoberturaColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("idadePrimeiraCobertura"));
		
		tableFichaAnimal.setItems(FXCollections.observableArrayList(fichaAnimalService.generateFichaAnimal(animal)));
		
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
