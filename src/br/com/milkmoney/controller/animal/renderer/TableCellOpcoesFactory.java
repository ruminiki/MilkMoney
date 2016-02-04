package br.com.milkmoney.controller.animal.renderer;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;
import br.com.milkmoney.model.Animal;

@SuppressWarnings("hiding")
public class TableCellOpcoesFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private PopUpMenu menu;
	
	public TableCellOpcoesFactory(PopUpMenu menu){
		this.menu = menu;
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
									Bounds sourceNodeBounds = btnOpcoes.localToScreen(btnOpcoes.getBoundsInLocal());
									menu.hide(Duration.ZERO);
									if ( !menu.isShowing() ){
										menu.setX(sourceNodeBounds.getMinX() + 20);
						        		menu.setY(sourceNodeBounds.getMaxY() - 55);
										menu.show(tableViewProperty().get().getScene().getWindow(), animal);	
									}
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
	
}
