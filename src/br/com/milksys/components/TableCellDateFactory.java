package br.com.milksys.components;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import br.com.milksys.util.DateUtil;

public class TableCellDateFactory<S, Date> implements Callback<TableColumn<S ,Date>, TableCell<S, Date>>{
	
	String property;
	
	public TableCellDateFactory(String property) {
		this.property = property;
	}

	@Override
	public TableCell<S, Date> call(TableColumn<S, Date> param) {
		
		  TableCell<S, Date> cell = new TableCell<S, Date>() {
			  
		        @Override
		        protected void updateItem(Date item, boolean empty) {
		            super.updateItem(item, empty);
		            if ( getTableRow().getItem() != null ){
			            if ( (item == null || empty) ) {
		            		setText("--");
			            } else {
			                try{
			                	setText(DateUtil.format((java.util.Date)item));
			                }catch(Exception e){
			                }
			            }
		            }else{
		            	setText(null);
		            }
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    
		    param.setCellValueFactory(new PropertyValueFactory<S, Date>(property));
		    
		    return cell;
	}
	
}
