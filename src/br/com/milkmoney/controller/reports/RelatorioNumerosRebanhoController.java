package br.com.milkmoney.controller.reports;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.exception.ValidationException;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.indicadores.IndicadorService;
import br.com.milkmoney.validation.Validator;

@Controller
public class RelatorioNumerosRebanhoController extends AbstractReport{

	@FXML protected ListView<Indicador> listIndicadores, listSelecionados;
	@FXML protected Button btnAdicionar, btnAdicionarTodos, btnRemover, btnRemoverTodos;
	@Autowired protected IndicadorService indicadorService;

	@FXML
	public void initialize() {
		
		listIndicadores.setItems(indicadorService.findAllAsObservableList());
		listIndicadores.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		// captura o evento de double click da table
		listIndicadores.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					if ( !listSelecionados.getItems().contains(listIndicadores.getSelectionModel().getSelectedItem()) ){
						listSelecionados.getItems().add(listIndicadores.getSelectionModel().getSelectedItem());
					}
					listIndicadores.getSelectionModel().clearSelection();
				}
			}
		});
		
		btnAdicionar.setOnAction(action -> {
			
			if ( listIndicadores.getSelectionModel().getSelectedItems() != null ){
				
				for ( Indicador Indicador : listIndicadores.getSelectionModel().getSelectedItems() ){
					
					if ( !listSelecionados.getItems().contains(Indicador) ){
						listSelecionados.getItems().add(Indicador);
					}
					
				}
				
				listIndicadores.getSelectionModel().clearSelection();
				
			}
			
		});
		
		btnAdicionarTodos.setOnAction(action -> {
			
			for ( Indicador Indicador : listIndicadores.getItems() ){
				
				if ( !listSelecionados.getItems().contains(Indicador) ){
					listSelecionados.getItems().add(Indicador);
				}
				
			}
			
		});
		
		btnRemover.setOnAction(action -> {
			if ( listSelecionados.getSelectionModel().getSelectedItem() != null ){
				listSelecionados.getItems().remove(listSelecionados.getSelectionModel().getSelectedItem());	
			}
		});
		
		btnRemoverTodos.setOnAction(action -> {
			listSelecionados.getItems().clear();
		});
		
		super.initialize();
		
	}
	
	@Override
	protected void handleExecutar(){
		
		if ( listSelecionados.getItems().size() <= 0 ){
			throw new ValidationException(Validator.CAMPO_OBRIGATORIO, "Por favor, selecione os animais para executar o relatório.");
		}
		
		//os ids dos animais selecionados são passados como parâmetro
		StringBuilder sb = new StringBuilder();
		
		for ( Indicador indicador : listSelecionados.getItems() ){
			sb.append(indicador.getId());
			sb.append(",");
		}
		
		sb.replace(sb.length()-1, sb.length(), "");
		
		if ( toggleGroupFormato.getSelectedToggle().equals(btnPDF) ){
			relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.RELATORIO_INDICADORES, sb.toString());
		}else{
			relatorioService.executeRelatorio(GenericPentahoReport.XLS_OUTPUT_FORMAT, 
					RelatorioService.RELATORIO_INDICADORES, sb.toString());
		}
		
		rootLayoutController.setMessage("O relatório está sendo executado...");
		super.handleClose();
	}

}
