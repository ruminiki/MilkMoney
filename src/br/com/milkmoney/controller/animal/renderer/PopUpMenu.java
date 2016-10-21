package br.com.milkmoney.controller.animal.renderer;

import java.util.function.Function;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import org.controlsfx.control.PopOver;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.SituacaoAnimal;

public class PopUpMenu extends PopOver {
		
		private Animal animal;
		private HBox container = new HBox();
		
		public PopUpMenu(Function<Animal, Boolean> coberturasFunction, 
							Function<Animal, Boolean> novaCoberturaFunction,	
							Function<Animal, Boolean> confirmarPrenhezFunction,
							Function<Animal, Boolean> novoPartoFunction,
							Function<Animal, Boolean> ultimaCoberturaFunction,
							Function<Animal, Boolean> ultimoPartoFunction,
							Function<Animal, Boolean> lactacoesFunction,
							Function<Animal, Boolean> controleLeiteiroFunction,
							Function<Animal, Boolean> vendaFunction,
							Function<Animal, Boolean> morteFunction,
							Function<Animal, Boolean> linhaTempoAnimalFunction,  
							Function<Animal, Boolean> fichaAnimalFunction) {

			container.setSpacing(5);
			container.setAlignment(Pos.TOP_LEFT);
			container.setStyle("-fx-background-color: #FFF");
			
			VBox column = new VBox();
			column.setSpacing(3);
			column.setStyle("-fx-background-color: #FFF");
			column.getChildren().add(createItem("Coberturas", coberturasFunction, "img/lista16.png"));
			column.getChildren().add(createItem("Nova Cobertura", novaCoberturaFunction, "img/novacobertura16.png"));
			column.getChildren().add(createItem("Confirmar Prenhez", confirmarPrenhezFunction, "img/confirmarprenhez16.png"));
			column.getChildren().add(createItem("Novo Parto", novoPartoFunction, "img/novoparto16.png"));
			container.getChildren().add(column);

			column = new VBox();
			column.setSpacing(3);
			column.setStyle("-fx-background-color: #FFF");

			column.getChildren().add(createItem("Última Cobertura", ultimaCoberturaFunction, "img/ultimacobertura16.png"));
			column.getChildren().add(createItem("Último Parto", ultimoPartoFunction, "img/ultimoparto16.png"));
			column.getChildren().add(createItem("Lactações/Encerrar", lactacoesFunction, "img/encerrarlactacao16.png"));
			column.getChildren().add(createItem("Controle Leiteiro", controleLeiteiroFunction, "img/producao16.png"));
			container.getChildren().add(new Separator(Orientation.VERTICAL));
			container.getChildren().add(column);
			
			column = new VBox();
			column.setSpacing(5);
			column.setStyle("-fx-background-color: #FFF");
			
			column.getChildren().add(createItem("Reg. Venda", vendaFunction, "img/venda16.png"));
			column.getChildren().add(createItem("Reg. Morte", morteFunction, "img/morte16.png"));
			column.getChildren().add(createItem("Linha do Tempo", linhaTempoAnimalFunction, "img/timeline16.png"));
			column.getChildren().add(createItem("Imprimir Ficha", fichaAnimalFunction, "img/ficha16.png"));
			container.getChildren().add(new Separator(Orientation.VERTICAL));
			container.getChildren().add(column);

			this.setDetached(false);
			this.setDetachable(false);
			this.setCornerRadius(3);
			this.setAutoHide(true);
			this.setAutoFix(true);
			
			this.setContentNode(container);
			
		}
			
		private HBox createItem(java.lang.String string, Function<Animal, Boolean> function, java.lang.String icon){
			
			HBox itemRow = new HBox();
			itemRow.setMinWidth(120);
			itemRow.setMinHeight(25);
			itemRow.setSpacing(3);
			
			VBox c = new VBox();
			c.setStyle("-fx-padding: 0; " +
					"-fx-min-height: 24; -fx-min-width: 24;" +
					"-fx-background-insets: 0;" +
				    "-fx-background-image: url('" + icon + "'); " +	
				    "-fx-background-repeat: no-repeat; " + 
				    "-fx-background-position: center; " +
				    "-fx-cursor: HAND; " +
				    "-fx-border-width:0; ");
			
			Label l = new Label(string.toString());
			
			VBox.setVgrow(l, Priority.SOMETIMES);
	        HBox.setHgrow(l, Priority.SOMETIMES);
			
			itemRow.getChildren().addAll(c,l);
			itemRow.setAlignment(Pos.CENTER_LEFT);
			
			//criar listener ao passar mouse
			itemRow.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						itemRow.setStyle("-fx-background-color: #CCC;");
					}
			});
			
			itemRow.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						itemRow.setStyle("-fx-background-color: #FFF");
					}
			});
			
			itemRow.addEventHandler(MouseEvent.MOUSE_RELEASED,
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							function.apply(animal);
						}
			});
			
			return itemRow;
		}
		
		protected void show(Window window, Animal animal) {
			this.animal = animal;
			
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
				VBox column = (VBox)container.getChildren().get(4);
				HBox item = (HBox) column.getChildren().get(0);
				((Label)item.getChildren().get(1)).setText("Editar/rem. venda");
			}
			
			if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
				VBox column = (VBox)container.getChildren().get(4);
				HBox item = (HBox) column.getChildren().get(0);
				((Label)item.getChildren().get(2)).setText("Editar/rem. morte");
			}
			
			super.show(window);
		}
		
		
	}