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
import javafx.stage.Popup;
import javafx.util.Callback;
import br.com.milkmoney.model.Animal;

@SuppressWarnings("hiding")
public class TableCellOpcoesFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
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
	private Function<Animal, Boolean> painelControleAnimalFunction;
	private Function<Animal, Boolean> confirmarPrenhesFunction;
	
	private CustomMenuButton menu;
	
	public TableCellOpcoesFactory(Function<Animal, Boolean> painelControleAnimalFunction,
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
		
		this.painelControleAnimalFunction = painelControleAnimalFunction;
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
		
		menu = new CustomMenuButton();
		
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
							
							VBoxOption btnOpcoes = new VBoxOption("img/reproducao16.png", "Painel Controle Animal");
							btnOpcoes.setOnMouseReleased(new EventHandler<Event>() {
					        	@Override
					        	public void handle(Event event) {
					        		Bounds sourceNodeBounds = btnOpcoes.localToScreen(btnOpcoes.getBoundsInLocal());
					        		menu.setX(sourceNodeBounds.getMinX() - 5.0);
					        		menu.setY(sourceNodeBounds.getMaxY() + 5.0);
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
	
	public class CustomMenuButton extends Popup {
		
		private Animal animal;
		
		public CustomMenuButton() {
			
			this.setAutoHide(true);
			this.setAutoFix(true);
			  
			HBox container = new HBox();
			
			container.setSpacing(3);
			container.setAlignment(Pos.TOP_LEFT);
			container.setStyle("-fx-background-color: #FFF");
			
			VBox column = new VBox();
			column.setSpacing(3);
			column.setStyle("-fx-background-color: #FFF");
			
			column.getChildren().add(createItem("Gerenciar Animal", painelControleAnimalFunction));
			column.getChildren().add(createItem("Nova Cobertura", novaCoberturaFunction));
			column.getChildren().add(createItem("Última Cobertura", ultimaCoberturaFunction));
			column.getChildren().add(createItem("Confirmar Prenhes", confirmarPrenhesFunction));
			container.getChildren().add(column);

			column = new VBox();
			column.setSpacing(3);
			column.setStyle("-fx-background-color: #FFF");

			column.getChildren().add(createItem("Novo Parto", novoPartoFunction));
			column.getChildren().add(createItem("Último Parto", ultimoPartoFunction));
			column.getChildren().add(createItem("Lactações", lactacoesFunction));
			column.getChildren().add(createItem("Controle Leiteiro", controleLeiteiroFunction));
			container.getChildren().add(new Separator(Orientation.VERTICAL));
			container.getChildren().add(column);
			
			column = new VBox();
			column.setSpacing(5);
			column.setStyle("-fx-background-color: #FFF");
			
			column.getChildren().add(createItem("Registrar Venda", vendaFunction));
			column.getChildren().add(createItem("Registrar Morte", morteFunction));
			column.getChildren().add(createItem("Linha do Tempo", linhaTempoAnimalFunction));
			column.getChildren().add(createItem("Imprimir Ficha", fichaAnimalFunction));
			container.getChildren().add(new Separator(Orientation.VERTICAL));
			container.getChildren().add(column);

			getContent().add(container);
					
		}
			
		private HBox createItem(java.lang.String string, Function<Animal, Boolean> function){
			HBox itemRow = new HBox();
			itemRow.setMinWidth(120);
			itemRow.setMinHeight(25);
			
			Label l = new Label(string.toString());
			
			VBox.setVgrow(l, Priority.SOMETIMES);
	        HBox.setHgrow(l, Priority.SOMETIMES);
			
			itemRow.getChildren().add(l);
			itemRow.setAlignment(Pos.CENTER);
			
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
