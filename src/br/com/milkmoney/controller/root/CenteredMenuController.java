package br.com.milkmoney.controller.root;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.controller.root.renderer.VBoxOption;


@Controller
public class CenteredMenuController {

	@FXML private VBox vBoxRebanho, vBoxReproducao, vBoxIndicadores, vBoxProducao, vBoxFinanceiro;
	@FXML private HBox hBoxOptions;
	
	@Autowired private RootLayoutController rootLayoutController;
	
	private static final String ICON_REBANHO = "img/rebanho.png";
	private static final String ICON_INDICADORES = "img/indicadores.png";
	private static final String ICON_PRODUCAO = "img/reproducao.png";
	private static final String ICON_FINANCEIRO = "img/financeiro.png";
	
	@FXML
	private void initialize(){
		
		VBoxOption vBoxRebanho = new VBoxOption(ICON_REBANHO, "Rebanho");
        vBoxRebanho.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroAnimal();
        	}
        });
        hBoxOptions.getChildren().add(vBoxRebanho);
        
        VBoxOption vBoxProducao = new VBoxOption(ICON_PRODUCAO, "Produção");
        vBoxProducao.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroProducaoLeite();
        	}
        });
        hBoxOptions.getChildren().add(vBoxProducao);
        
        VBoxOption vBoxFinanceiro = new VBoxOption(ICON_FINANCEIRO, "Financeiro");
        vBoxFinanceiro.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroLancamentoFinanceiro();
        	}
        });
        hBoxOptions.getChildren().add(vBoxFinanceiro);
        
        VBoxOption vBoxIndicadores = new VBoxOption(ICON_INDICADORES, "Indicadores");
        vBoxIndicadores.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handlePainel();
        	}
        });
        hBoxOptions.getChildren().add(vBoxIndicadores);
		
	}
	

}