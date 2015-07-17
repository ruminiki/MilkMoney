package br.com.milksys.controller.fichaAnimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.FichaAnimal;
import br.com.milksys.model.Lactacao;
import br.com.milksys.service.AnimalService;
import br.com.milksys.service.FichaAnimalService;
import br.com.milksys.util.NumberFormatUtil;

@Controller
public class FichaAnimalOverviewController extends AbstractOverviewController<Integer, FichaAnimal>{

	@FXML private TableView<FichaAnimal> tableEventos;
	@FXML private TableColumn<FichaAnimal, String> dataColumn;
	@FXML private TableColumn<FichaAnimal, String> eventoColumn;
	
	@FXML private TableView<Lactacao> tableLactacoes;
	@FXML private TableColumn<Lactacao, String> dataInicioLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> dataTerminoLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> diasEmLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> mesesEmLactacaoColumn;
	
	@FXML private Label lblHeader, lblNumeroPartos, lblTotalDiasLactacao, lblMediaDiasLactacao, lblTotalDiasAbertos;
	
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private AnimalService animalService;
	
	private Animal animal;
	
	@FXML
	public void initialize() {
		
		//tabela eventos
		dataColumn.setCellFactory(new TableCellDateFactory<FichaAnimal,String>("data"));
		eventoColumn.setCellValueFactory(new PropertyValueFactory<FichaAnimal,String>("evento"));
		
		tableEventos.getItems().clear();
		tableEventos.setItems(fichaAnimalService.findAllByAnimal(animal));

		//tabela lactações
		dataInicioLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataInicio"));
		dataTerminoLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataFim"));
		diasEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("duracaoLactacaoDias"));
		mesesEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("duracaoLactacaoMeses"));
		
		tableLactacoes.getItems().clear();
		tableLactacoes.setItems(animalService.findLactacoesAnimal(animal));
		
		lblHeader.setText("FICHA ANIMAL " + animal.toString());
		
		lblNumeroPartos.setText(String.valueOf(animalService.getNumeroPartos(animal)));
		
		long totalDiasLactacao = 0;
		for ( Lactacao lactacao : tableLactacoes.getItems() ){
			totalDiasLactacao += lactacao.getDuracaoLactacaoDias();
		}
		lblTotalDiasLactacao.setText(String.valueOf(totalDiasLactacao));
		
		if ( tableLactacoes.getItems() != null &&  tableLactacoes.getItems().size() > 0 && totalDiasLactacao > 0 ){
			lblMediaDiasLactacao.setText( NumberFormatUtil.decimalFormat(
					BigDecimal.valueOf(totalDiasLactacao).divide(BigDecimal.valueOf(tableLactacoes.getItems().size()), 1, RoundingMode.HALF_EVEN)) );
		}else{
			lblMediaDiasLactacao.setText("0");
		}
		
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
