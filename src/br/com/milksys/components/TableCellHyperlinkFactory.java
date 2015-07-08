package br.com.milksys.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

@SuppressWarnings("hiding")
public class TableCellHyperlinkFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private EventHandler<ActionEvent> handler;
	
	public TableCellHyperlinkFactory(EventHandler<ActionEvent> handler) {
		this.handler = handler;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
		  TableCell<S, String> cell = new TableCell<S, String>() {
			  
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
						if(item!=null){
							Hyperlink hp = new Hyperlink();
							hp.setText((java.lang.String)item);
							hp.setOnAction(handler);
							setGraphic(hp);
						} 
					}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    return cell;
	}
	
}
