package br.com.milkmoney.controller.reports;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.service.RelatorioService;

public class AbstractReport {
	
	@FXML protected ToggleButton btnPDF, btnXLS;
	@Autowired protected RelatorioService relatorioService;
	
	protected ToggleGroup toggleGroupFormato = new ToggleGroup();

	@FXML
	public void initialize() {
		
		if ( btnPDF != null && btnXLS != null ){
			btnXLS.setToggleGroup(toggleGroupFormato);
			btnPDF.setToggleGroup(toggleGroupFormato);
			toggleGroupFormato.selectToggle(btnPDF);
		}
		
		btnXLS.selectedProperty().addListener((observable, oldValue, newValue) -> {
			btnPDF.setSelected(!btnXLS.isSelected());
			
		});
		
		btnPDF.selectedProperty().addListener((observable, oldValue, newValue) -> {
			btnXLS.setSelected(!btnPDF.isSelected());
		});
		
	}
	
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
