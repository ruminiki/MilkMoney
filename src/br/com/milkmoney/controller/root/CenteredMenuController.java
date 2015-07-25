package br.com.milkmoney.controller.root;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class CenteredMenuController {

	@FXML private VBox vBoxRebanho, vBoxReproducao, vBoxIndicadores;
	
	@Autowired private RootLayoutController rootLayoutController;
	
	private static final String ICON_REBANHO = "img/rebanho.png";
	private static final String ICON_REPRODUCAO = "img/reproducao.png";
	private static final String ICON_INDICADORES = "img/indicadores.png";
	
	@FXML
	private void initialize(){
		
        vBoxRebanho.setStyle(
                "-fx-padding: 1; " +
                "-fx-cursor: HAND; " +
                "-fx-background-image: url('" + ICON_REBANHO + "'); " +		
                "-fx-background-color: cornsilk; " +
                "-fx-border-width:0; " +
                "-fx-border-color: " +
                    "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                    ");"
        );
        vBoxRebanho.setEffect(new DropShadow());
        vBoxRebanho.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroAnimal();
        	}
        });
		
        vBoxReproducao.setStyle(
                "-fx-padding: 1; " +
                "-fx-cursor: HAND; " +
                "-fx-background-image: url('" + ICON_REPRODUCAO + "'); " +		
                "-fx-background-color: cornsilk; " +
                "-fx-border-width:0; " +
                "-fx-border-color: " +
                    "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                    ");"
        );
        vBoxReproducao.setEffect(new DropShadow());
        vBoxReproducao.setOnMouseReleased(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		rootLayoutController.handleCadastroCobertura();
        	}
        });
        
        vBoxIndicadores.setStyle(
                "-fx-padding: 1; " +
                "-fx-cursor: HAND; " +
                "-fx-background-image: url('" + ICON_INDICADORES + "'); " +		
                "-fx-background-color: cornsilk; " +
                "-fx-border-width:0; " +
                "-fx-border-color: " +
                    "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                    ");"
        );
        vBoxIndicadores.setEffect(new DropShadow());
        vBoxIndicadores.setOnMouseReleased(new EventHandler<Event>() {
            @Override
            public void handle(Event e) {
            	rootLayoutController.handlePainel();
            }
		});
        
	}
	

}