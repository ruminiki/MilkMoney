package br.com.milkmoney.controller.evolucaoRebanho;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.model.EvolucaoRebanho;
import br.com.milkmoney.service.EvolucaoRebanhoService;
import br.com.milkmoney.util.Util;

import com.sun.javafx.scene.control.skin.TableViewSkinBase;

@Controller
public class EvolucaoRebanhoOverviewController{

	@FXML private TableView<EvolucaoRebanho> table, tableVariavel;
	@FXML private TableColumn<EvolucaoRebanho, String> categoriaColumn;
	@FXML private DatePicker inputDataInicio, inputDataFim;
	
	@Autowired EvolucaoRebanhoService service;
	
	private List<List<String>> valoresPeriodo = new ArrayList<List<String>>();
	
	@FXML
	public void initialize() {
		
		categoriaColumn.setCellValueFactory(new PropertyValueFactory<EvolucaoRebanho,String>("variavel"));
		tableVariavel.getItems().addAll(EvolucaoRebanho.getItems());
		tableVariavel.getSelectionModel().setCellSelectionEnabled(false);
		
		inputDataInicio.setValue(LocalDate.now().minusYears(1));
		inputDataFim.setValue(LocalDate.now());
		
		inputDataInicio.valueProperty().addListener((observable, oldValue, newValue) -> configureAndsearch(inputDataInicio.getValue(), inputDataFim.getValue()));
		inputDataFim.valueProperty().addListener((observable, oldValue, newValue) -> configureAndsearch(inputDataInicio.getValue(), inputDataFim.getValue()));
		
		configureAndsearch(inputDataInicio.getValue(), inputDataFim.getValue());
		
	}
	
	private void configureAndsearch(LocalDate dataInicio, LocalDate dataFim){
		
		if ( dataFim.compareTo(LocalDate.now()) > 0 ){
			CustomAlert.mensagemInfo("A data final não pode ser maior que a data atual. Por favor, selecione uma nova data.");
			return;
		}
		
		List<String> meses = Util.generateListMonthsAbrev();
		
		table.getItems().clear();
		table.getColumns().clear();
		
		//configura a tabela
		while ( dataInicio.compareTo(dataFim) <= 0 ){
			
			String header = meses.get(dataInicio.getMonthValue()-1) + "/" + dataInicio.getYear();
			
			TableColumn<EvolucaoRebanho, String> valorColumn = new TableColumn<EvolucaoRebanho, String>(header);
			valorColumn.setCellValueFactory(new PropertyValueFactory<EvolucaoRebanho,String>(header));
			valorColumn.setSortable(false);
			valorColumn.setCellFactory(new Callback<TableColumn<EvolucaoRebanho,String>,TableCell<EvolucaoRebanho,String>>(){        
				@Override
				public TableCell<EvolucaoRebanho, String> call(TableColumn<EvolucaoRebanho, String> param) {                
					TableCell<EvolucaoRebanho, String> cell = new TableCell<EvolucaoRebanho, String>(){
						@Override
						public void updateItem(String item, boolean empty) {
							
							if ( getIndex() < tableVariavel.getItems().size() ){
								int columnIndex = 0;
								for ( TableColumn<EvolucaoRebanho, ?> t : table.getColumns() ){
									if ( t.equals(getTableColumn()) ){
										break;
									}
									columnIndex++;
								}
								
								try{
									setText(valoresPeriodo.get(getIndex()).get(columnIndex));	
								}catch(Exception e ){
								}
							}
						}
					};                           
					return cell;
				}	
			});
			
			valorColumn.setStyle("-fx-alignment:CENTER");
			table.getColumns().add(valorColumn);
			table.getSelectionModel().setCellSelectionEnabled(false);
			
			dataInicio = dataInicio.plusMonths(1);
			
		}
		
		table.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
		
		valoresPeriodo.clear();
		for ( EvolucaoRebanho e : EvolucaoRebanho.getItems() ){
			
			valoresPeriodo.add(service.calculaEvolucaoRebanho(e.getVariavel(), 
															inputDataInicio.getValue().getMonthValue(), 
															inputDataInicio.getValue().getYear(),
															inputDataFim.getValue().getMonthValue(),
															inputDataFim.getValue().getYear()));
			
		}
		
		table.getItems().clear();
		table.getItems().addAll(EvolucaoRebanho.getItems());
	}
	
	public void showForm() {	
		
		AnchorPane form = (AnchorPane) MainApp.load(getFormName());
		Stage dialogStage = new Stage();
		dialogStage.setTitle(getFormTitle());
		dialogStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(MainApp.APPLICATION_ICON)));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);
		dialogStage.setResizable(false);
		
		dialogStage.show();
		
	}
	
	public String getFormTitle() {
		return "Evolução do Rebanho";
	}
	
	public String getFormName() {
		return "view/evolucaoRebanho/EvolucaoRebanhoOverview.fxml";
	}
	
}
