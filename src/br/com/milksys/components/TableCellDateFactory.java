package br.com.milksys.components;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import br.com.milksys.util.DateUtil;

public class TableCellDateFactory<S, LocalDate> implements Callback<TableColumn<S ,LocalDate>, TableCell<S, LocalDate>>{
	
	String property;
	
	public TableCellDateFactory(String property) {
		this.property = property;
	}

	@Override
	public TableCell<S, LocalDate> call(TableColumn<S, LocalDate> param) {
		
		  TableCell<S, LocalDate> cell = new TableCell<S, LocalDate>() {
			  
		        @Override
		        protected void updateItem(LocalDate item, boolean empty) {
		            super.updateItem(item, empty);

		            if ( (item == null || empty) ) {
		            	if ( getTableRow().getItem() != null )
		            		setText("--");
		            } else {
		                try{
		                	setText(DateUtil.format((java.time.LocalDate) item));
		                }catch(Exception e){
		                	
		                }
		            }
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    
		    param.setCellValueFactory(new PropertyValueFactory<S, LocalDate>(property));
		    
		    return cell;
	}
	
}
