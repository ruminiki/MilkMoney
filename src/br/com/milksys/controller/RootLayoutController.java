package br.com.milksys.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

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
    	openFormAsPopUp("view/precoLeite/PrecoLeiteOverview.fxml");
    }
    
    @FXML
    private void handleCadastroProducaoLeite() {
    	openForm("view/producaoLeite/ProducaoLeiteOverview.fxml");
    }
       
    @FXML
    private void handleCadastroEntregaLeite() {
    	openFormAsPopUp("view/entregaLeite/EntregaLeiteOverview.fxml");
    }

    @FXML
    private void handleCadastroProducaoIndividual() {
    	openFormAsPopUp("view/producaoIndividual/ProducaoIndividualOverview.fxml");
    }
    
    @FXML
    private void handleCadastroSemen() {
    	openFormAsPopUp("view/semen/SemenReducedOverview.fxml");
    }
    
    @FXML
    private void handleCadastroTouro() {
    	openFormAsPopUp("view/touro/TouroReducedOverview.fxml");
    }
    
    @FXML
    private void handleCadastroLactacao() {
    	openFormAsPopUp("view/encerramentoLactacao/LactacaoOverview.fxml");
    }
    
    @FXML
    private void handleCadastroCobertura() {
    	openForm("view/cobertura/CoberturaOverview.fxml");
    }
    
    @FXML
    private void handleCadastroMotivoOcorrenciaFuncionario() {
    	openFormAsPopUp("view/motivoOcorrenciaFuncionario/MotivoOcorrenciaFuncionarioOverview.fxml");
    }
    
    @FXML
    private void handleCadastroFuncionario() {
    	openFormAsPopUp("view/funcionario/FuncionarioReducedOverview.fxml");
    }
    
    @FXML
    private void handleCadastroPrestadorServico() {
    	openFormAsPopUp("view/prestadorServico/PrestadorReducedServicoOverview.fxml");
    }
    
    @FXML
    private void handleCadastroServico() {
    	openFormAsPopUp("view/servico/ServicoOverview.fxml");
    }
    
    @FXML
    private void handleCadastroFornecedor() {
    	openFormAsPopUp("view/fornecedor/FornecedorReducedOverview.fxml");
    }
    
    @FXML
    private void handlePainel() {
    	openForm("view/painel/PainelOverview.fxml");
    }
    
    @FXML
    private void handleCadastroParametro() {
    	openFormAsPopUp("view/parametro/ParametroOverview.fxml");
    }
    
    @FXML
    private void  handleCadastroPropriedade() {
    	openFormAsPopUp("view/propriedade/PropriedadeReducedOverview.fxml");
    }
    
    public void openFormAsPopUp(String formPath){
    	
    	AnchorPane form = (AnchorPane) MainApp.load(formPath);
    	
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(MainApp.primaryStage);

		Scene scene = new Scene(form);
		dialogStage.setScene(scene);

		dialogStage.setResizable(false);
		dialogStage.showAndWait();
		
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