package br.com.milkmoney.components;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

@SuppressWarnings("hiding")
public class TableCellIndexFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
	  TableCell<S, String> cell = new TableCell<S, String>() {
		  
	        @Override
	        protected void updateItem(String item, boolean empty) {
        		super.updateItem(item, empty);

        		if (this.getTableRow() != null && this.getTableRow().getItem() != null ) {
                    setText((this.getTableRow().getIndex()+1)+"");
                } else {
                    setText("");
                }
	        }
	    };
	    
	    return cell;
	    
	}
	
}
