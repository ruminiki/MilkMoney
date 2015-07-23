package br.com.milkmoney.components;

import java.sql.Timestamp;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import br.com.milkmoney.util.DateUtil;

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
		            if ( getTableRow()!= null && getTableRow().getItem() != null ){
			            if ( (item == null || empty) ) {
		            		setText("--");
			            } else {
			                try{
			                	
			                	if ( item instanceof java.sql.Timestamp )
			                		setText( DateUtil.format(((Timestamp)item).toLocalDateTime().toLocalDate()) );
			                	
			                	if ( item instanceof java.util.Date )
			                	   	setText(DateUtil.format((java.util.Date)item));
			                	
			                	if ( item instanceof java.time.LocalDate )
			                	   	setText(DateUtil.format((java.time.LocalDate)item));
			                	
			                }catch(Exception e){
			                	System.out.println(e);
			                }
			            }
		            }else{
		            	setText(null);
		            }
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    
		    param.setCellValueFactory(new PropertyValueFactory<S, LocalDate>(property));
		    
		    return cell;
	}
	
}
