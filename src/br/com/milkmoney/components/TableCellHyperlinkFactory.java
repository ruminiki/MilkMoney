package br.com.milkmoney.components;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

@SuppressWarnings("hiding")
public class TableCellHyperlinkFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Integer, Boolean> function;
	
	public TableCellHyperlinkFactory(Function<Integer, Boolean> showAnimaisVenda) {
		this.function = showAnimaisVenda;
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
							hp.setFocusTraversable(false);
							hp.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									function.apply(tableRowProperty().get().getIndex());
								}
							});
							setGraphic(hp);
						} 
					}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    return cell;
	}
	
}
