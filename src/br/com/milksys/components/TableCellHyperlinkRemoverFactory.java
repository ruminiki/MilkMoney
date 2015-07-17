package br.com.milksys.components;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

@SuppressWarnings("hiding")
public class TableCellHyperlinkRemoverFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Integer, Boolean> function;
	private Boolean disabled;
	
	public TableCellHyperlinkRemoverFactory(Function<Integer, Boolean> function, Boolean disabled) {
		this.function = function;
		this.disabled = disabled;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
		  TableCell<S, String> cell = new TableCell<S, String>() {
			  
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
						if(item!=null){
							Hyperlink hp = new Hyperlink();
							hp.setText("Remover");
							hp.setFocusTraversable(false);
							hp.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									function.apply(getTableRow().getIndex());
								}
							});
							hp.setDisable(disabled);
							setGraphic(hp);
						} 
					}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    return cell;
	}
	
}
