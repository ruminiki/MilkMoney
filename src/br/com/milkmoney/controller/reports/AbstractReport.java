package br.com.milkmoney.controller.reports;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.service.RelatorioService;

public abstract class AbstractReport {
	
	@FXML protected ToggleButton btnPDF, btnXLS;
	@FXML protected Button btnExecutar;
	@Autowired protected RelatorioService relatorioService;
	@Autowired protected RootLayoutController rootLayoutController;
	
	protected ToggleGroup toggleGroupFormato = new ToggleGroup();

	@FXML
	public void initialize() {
		
		if ( btnPDF != null && btnXLS != null ){
			btnXLS.setToggleGroup(toggleGroupFormato);
			btnPDF.setToggleGroup(toggleGroupFormato);
			toggleGroupFormato.selectToggle(btnPDF);
			
			btnXLS.selectedProperty().addListener((observable, oldValue, newValue) -> {
				btnPDF.setSelected(!btnXLS.isSelected());
				
			});
			
			btnPDF.selectedProperty().addListener((observable, oldValue, newValue) -> {
				btnXLS.setSelected(!btnPDF.isSelected());
			});
			
		}
		
		if ( btnExecutar != null ){
			
			Scene scene = btnExecutar.getScene();
			if ( scene != null ){
				scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

					@Override
					public void handle(KeyEvent event) {
						if ( event.getCode().equals(KeyCode.ENTER) ){
							handleExecutar();
						}
					}
					
				});
			}
			
		}
		
	}
	
	@FXML
	protected abstract void handleExecutar();
	
	@FXML
	protected void handleClose(){
		if ( btnPDF != null ){
			Stage stage = (Stage)btnPDF.getScene().getWindow();
			// se for popup
			if ( stage.getModality().equals(Modality.APPLICATION_MODAL) ){
				((Stage)btnPDF.getScene().getWindow()).close();	
			}else{
				MainApp.resetLayout();
			}
		}
	}
	
}
