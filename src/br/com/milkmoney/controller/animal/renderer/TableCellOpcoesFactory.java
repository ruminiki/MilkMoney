package br.com.milkmoney.controller.animal.renderer;

import java.util.function.Function;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.controlsfx.control.PopOver;

import br.com.milkmoney.model.Animal;

@SuppressWarnings("hiding")
public class TableCellOpcoesFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Animal, Boolean> coberturasFunction;
	private Function<Animal, Boolean> lactacoesFunction;
	private Function<Animal, Boolean> vendaFunction;
	private Function<Animal, Boolean> morteFunction;
	private Function<Animal, Boolean> controleLeiteiroFunction;
	private Function<Animal, Boolean> novaCoberturaFunction;
	private Function<Animal, Boolean> novoPartoFunction;
	private Function<Animal, Boolean> ultimoPartoFunction;
	private Function<Animal, Boolean> ultimaCoberturaFunction;
	private Function<Animal, Boolean> fichaAnimalFunction;
	private Function<Animal, Boolean> linhaTempoAnimalFunction;
	private Function<Animal, Boolean> confirmarPrenhesFunction;
	
	private CustomMenuButton menu;
	
	public TableCellOpcoesFactory(Function<Animal, Boolean> coberturasFunction, 
									Function<Animal, Boolean> novaCoberturaFunction,	
									Function<Animal, Boolean> confirmarPrenhesFunction,
									Function<Animal, Boolean> novoPartoFunction,
									Function<Animal, Boolean> ultimaCoberturaFunction,
									Function<Animal, Boolean> ultimoPartoFunction,
									Function<Animal, Boolean> lactacoesFunction,
									Function<Animal, Boolean> controleLeiteiroFunction,
									Function<Animal, Boolean> vendaFunction,
									Function<Animal, Boolean> morteFunction,
									Function<Animal, Boolean> linhaTempoAnimalFunction,  
									Function<Animal, Boolean> fichaAnimalFunction) {
		
		this.coberturasFunction = coberturasFunction;
		this.lactacoesFunction = lactacoesFunction;
		this.vendaFunction = vendaFunction;
		this.morteFunction = morteFunction;
		this.controleLeiteiroFunction = controleLeiteiroFunction;
		this.novaCoberturaFunction = novaCoberturaFunction;
		this.novoPartoFunction = novoPartoFunction;
		this.ultimaCoberturaFunction = ultimaCoberturaFunction;
		this.ultimoPartoFunction = ultimoPartoFunction;
		this.fichaAnimalFunction = fichaAnimalFunction;
		this.linhaTempoAnimalFunction = linhaTempoAnimalFunction;
		this.confirmarPrenhesFunction = confirmarPrenhesFunction;
		
		this.menu = new CustomMenuButton();
		
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
		  TableCell<S, String> cell = new TableCell<S, String>() {
			  
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
		        		if(item!=null){
		        			
		        			Animal animal = (Animal) tableViewProperty().get().getItems().get(tableRowProperty().get().getIndex());
		        			
							HBox cell = new HBox();
							cell.setAlignment(Pos.CENTER);
							cell.setSpacing(8);
							
							VBoxOption btnOpcoes = new VBoxOption("img/options24.png", "Opções");
							btnOpcoes.setOnMouseReleased(new EventHandler<Event>() {
					        	@Override
					        	public void handle(Event event) {
					        		menu.hide();
					        		Bounds sourceNodeBounds = btnOpcoes.localToScreen(btnOpcoes.getBoundsInLocal());
					        		menu.setX(sourceNodeBounds.getMinX() + 20);
					        		menu.setY(sourceNodeBounds.getMaxY() - 55);
					        		menu.show(tableViewProperty().get().getScene().getWindow());
					        		menu.show(animal);
					        	}
					        });
							
							cell.getChildren().add(btnOpcoes);
							
							setGraphic(cell);
						}else{
							setGraphic(null);
						}
					}else{
						setGraphic(null);
					}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    return cell;
	}
	
	public class CustomMenuButton extends PopOver {
		
		private Animal animal;
		HBox container = new HBox();
		
		public CustomMenuButton() {
			
			container.setSpacing(5);
			container.setAlignment(Pos.TOP_LEFT);
			container.setStyle("-fx-background-color: #FFF");
			
			VBox column = new VBox();
			column.setSpacing(3);
			column.setStyle("-fx-background-color: #FFF");
			column.getChildren().add(createItem("Coberturas", coberturasFunction, "img/lista16.png"));
			column.getChildren().add(createItem("Nova Cobertura", novaCoberturaFunction, "img/novacobertura16.png"));
			column.getChildren().add(createItem("Confirmar Prenhes", confirmarPrenhesFunction, "img/confirmarprenhes16.png"));
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
			
			column.getChildren().add(createItem("Registrar Venda", vendaFunction, "img/venda16.png"));
			column.getChildren().add(createItem("Registrar Morte", morteFunction, "img/morte16.png"));
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
		
		protected void show(Animal animal) {
			this.animal = animal;
			super.show();
		}
		
		
	}
	
}
