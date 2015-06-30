package br.com.milksys.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import br.com.milksys.MainApp;

/**
 * O controlador para o root layout. O root layout prov� um layout b�sico
 * para a aplica��o contendo uma barra de menu e um espa�o onde outros elementos
 * JavaFX podem ser colocados.
 * 
 * @author Marco Jakob
 */
public class RootLayoutController {

    @FXML
    private void handleCadastroAnimal() throws IOException {
        
    	openForm("view/animal/AnimalOverview.fxml");
    	
    }

    @FXML
    private void handleCadastroRaca() {
        
    	openForm("view/raca/RacaOverview.fxml");
    	
    }

    @FXML
    private void handleCadastroFinalidadeLote() {
        
    	openForm("view/finalidadeLote/FinalidadeLoteOverview.fxml");
    	
    }
       
    @FXML
    private void handleCadastroPrecoLeite() {
        
    	openForm("view/precoLeite/PrecoLeiteOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroProducaoLeite() {
        
    	openForm("view/producaoLeite/ProducaoLeiteOverview.fxml");
    	
    }
       
    @FXML
    private void handleCadastroEntregaLeite() {
        
    	openForm("view/entregaLeite/EntregaLeiteOverview.fxml");
    	
    }

    @FXML
    private void handleCadastroProducaoIndividual() {
    	
    	openForm("view/producaoIndividual/ProducaoIndividualOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroSemen() {
    	
    	openForm("view/semen/SemenOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroCobertura() {
    	
    	openForm("view/cobertura/CoberturaOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroMotivoOcorrenciaFuncionario() {
    	
    	openForm("view/motivoOcorrenciaFuncionario/MotivoOcorrenciaFuncionarioOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroFuncionario() {
    	
    	openForm("view/funcionario/FuncionarioOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroOcorrenciaFuncionario() {
    	
    	openForm("view/ocorrenciaFuncionario/OcorrenciaFuncionarioOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroPrestadorServico() {
    	
    	openForm("view/prestadorServico/PrestadorServicoOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroServico() {
    	
    	openForm("view/servico/ServicoOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroFornecedor() {
    	
    	openForm("view/fornecedor/FornecedorOverview.fxml");
    	
    }
    
    private void openForm(String formPath){
    	AnchorPane form = (AnchorPane) MainApp.load(formPath);
    	MainApp.rootLayout.setCenter(form);
    }

    /**
     * Abre uma janela Sobre.
     */
    @FXML
    private void handleAbout() {
    	
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Milk Money 1.0");
		alert.setHeaderText("Sobre");
		alert.setContentText("Autor: Ruminiki Schmoeller");
		alert.showAndWait();
		
    }

    /**
     * Fecha a aplica��o.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}