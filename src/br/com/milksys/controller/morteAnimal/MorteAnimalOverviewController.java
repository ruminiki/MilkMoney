package br.com.milksys.controller.morteAnimal;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.PropertyDecimalValueFactory;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.CausaMorteAnimal;
import br.com.milksys.model.MorteAnimal;
import br.com.milksys.service.IService;
import br.com.milksys.service.MorteAnimalService;

@Controller
public class MorteAnimalOverviewController extends AbstractOverviewController<Integer, MorteAnimal> {

	@FXML private TableColumn<MorteAnimal, Date> dataMorteColumn;
	@FXML private TableColumn<Animal, String> animalColumn;
	@FXML private TableColumn<CausaMorteAnimal, String> causaMorteAnimalColumn;
	@FXML private TableColumn<MorteAnimal, String> valorAnimalColumn;
	@FXML private HBox hbox;
	
	@FXML
	public void initialize() {
		
		dataMorteColumn.setCellFactory(new TableCellDateFactory<MorteAnimal,Date>("dataMorte"));
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("animal"));
		causaMorteAnimalColumn.setCellValueFactory(new PropertyValueFactory<CausaMorteAnimal,String>("causaMorteAnimal"));
		valorAnimalColumn.setCellValueFactory(new PropertyDecimalValueFactory<MorteAnimal,String>("valorAnimal"));
		
		super.initialize((MorteAnimalFormController)MainApp.getBean(MorteAnimalFormController.class));
		
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Causa");
        yAxis.setLabel("Quantidade");
        
        final BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setPrefHeight(200);
        barChart.setPrefWidth(200);
        
        barChart.setTitle("Principais Causas Mortes");
        
        barChart.setLegendVisible(false);
        
        //XYChart.Series<String, Number> serie = ((MorteAnimalService)service).getDataChart();
        
        barChart.getData().addAll(((MorteAnimalService)service).getDataChart());

        hbox.getChildren().addAll(barChart);
        
	}

	@Override
	protected String getFormTitle() {
		return "Morte Animal";
	}
	
	@Override
	protected String getFormName() {
		return "view/morteAnimal/MorteAnimalOverview.fxml";
	}
	
	@Override
	@Resource(name = "morteAnimalService")
	protected void setService(IService<Integer, MorteAnimal> service) {
		super.setService(service);
	}
	
}
