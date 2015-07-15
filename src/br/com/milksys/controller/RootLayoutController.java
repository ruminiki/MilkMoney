package br.com.milksys.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import br.com.milksys.MainApp;


@Controller
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
    private void handleCadastroComprador() {
        
    	openForm("view/comprador/CompradorOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroCausaMorteAnimal() {
        
    	openForm("view/causaMorteAnimal/CausaMorteAnimalOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroMorteAnimal() {
        
    	openForm("view/morteAnimal/MorteAnimalOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroVendaAnimal() {
        
    	openForm("view/vendaAnimal/VendaAnimalOverview.fxml");
    	
    }
    
    
    @FXML
    private void handleCadastroMotivoVendaAnimal() {
        
    	openForm("view/motivoVendaAnimal/MotivoVendaAnimalOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroSituacaoAnimal() {
        
    	openForm("view/situacaoAnimal/SituacaoAnimalOverview.fxml");
    	
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
    private void handleCadastroTouro() {
    	
    	openForm("view/touro/TouroOverview.fxml");
    	
    }
    
    @FXML
    private void handleCadastroEncerramentoLactacao() {
    	
    	openForm("view/encerramentoLactacao/EncerramentoLactacaoOverview.fxml");
    	
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
    
    @FXML
    private void handlePainel() {
    	
    	openForm("view/painel/PainelOverview.fxml");
    	
    }
    
    public void openForm(String formPath){
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

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}