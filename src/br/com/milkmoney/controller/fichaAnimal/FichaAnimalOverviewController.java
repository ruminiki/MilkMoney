package br.com.milkmoney.controller.fichaAnimal;

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
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.LactacaoService;
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
	@FXML private TableColumn<Lactacao, String> mediaProducaoColumn;
	
	@FXML private Label lblHeader;
	
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private LactacaoService lactacaoService;
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
		mediaProducaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("mediaProducao"));
		
		tableLactacoes.getItems().clear();
		tableLactacoes.setItems(lactacaoService.findLactacoesAnimal(animal));
		
		lblHeader.setText("FICHA ANIMAL " + animal.toString());
		
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
