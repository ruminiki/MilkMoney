package br.com.milksys.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.controller.propriedade.PropriedadeReducedOverviewController;
import br.com.milksys.exception.ValidationException;
import br.com.milksys.model.Propriedade;
import br.com.milksys.reports.GenericPentahoReport;
import br.com.milksys.service.PropriedadeService;
import br.com.milksys.service.RelatorioService;
import br.com.milksys.validation.Validator;


@Controller
public class RootLayoutController {

	
	@FXML private Label lblHeader;
	@FXML private Hyperlink hlPropriedade;
	
	@FXML
	private void initialize(){
		
		PropriedadeReducedOverviewController propriedadeReducedOverviewController = 
				(PropriedadeReducedOverviewController)MainApp.getBean(PropriedadeReducedOverviewController.class);
		
		try{
			PropriedadeService propriedadeService = (PropriedadeService)MainApp.getBean(PropriedadeService.class);
			Propriedade propriedade = propriedadeService.findAll().get(0);
			hlPropriedade.setText("PROPRIEDADE "+propriedade.getDescricao());
		}catch(Exception e){
			throw new ValidationException(Validator.VALIDACAO_FORMULARIO, "Houve um erro ao recuperar a propriedade. Por favor, verifique se foi executa a inicialização do banco de dados.");
		}
		
		hlPropriedade.setOnAction(action -> {
			propriedadeReducedOverviewController.showForm();
			if ( propriedadeReducedOverviewController.getObject() != null ){
				hlPropriedade.setText("PROPRIEDADE "+propriedadeReducedOverviewController.getObject().getDescricao());
			}
		});
		
		lblHeader.setText("Milk Money® - Gestão de Rebanhos Leiteiros");
		
	}
	
	private void setTitle(String title){
		lblHeader.setText(title);
	}
	
    @FXML
    private void handleCadastroAnimal() throws IOException {
    	openForm("view/animal/AnimalOverview.fxml");
    	setTitle("Rebanho > Animais");
    }

    @FXML
    private void handleCadastroVendaAnimal() {
    	openFormAsPopUp("view/vendaAnimal/VendaAnimalOverview.fxml");
    }
    
    @FXML
    private void handleCadastroFinalidadeLote() {
    	openFormAsPopUp("view/finalidadeLote/FinalidadeLoteOverview.fxml");
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
    private void handleCadastroSemen() {
    	openFormAsPopUp("view/semen/SemenReducedOverview.fxml");
    }
    
    @FXML
    private void handleCadastroTouro() {
    	openFormAsPopUp("view/touro/TouroReducedOverview.fxml");
    }
    
    @FXML
    private void handleCadastroCobertura() {
    	openForm("view/cobertura/CoberturaOverview.fxml");
    	setTitle("Rebanho > Reprodução");
    }
    
    @FXML
    private void handleCadastroFuncionario() {
    	openFormAsPopUp("view/funcionario/FuncionarioReducedOverview.fxml");
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
    	setTitle("Miscelânea > Indicadores");
    }
    
    @FXML
    private void handleCadastroParametro() {
    	openFormAsPopUp("view/parametro/ParametroOverview.fxml");
    }
    
    @FXML
    private void  handleCadastroPropriedade() {
    	openFormAsPopUp("view/propriedade/PropriedadeReducedOverview.fxml");
    }
    
    @FXML
    private void handleFormularioCampoRegistroCobertura(){
    	RelatorioService relatorioService = (RelatorioService)MainApp.getBean(RelatorioService.class);
    	relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, 
				RelatorioService.FORMULARIO_CAMPO_REGISTRO_COBERTURA);
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