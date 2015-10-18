package br.com.milkmoney.controller.cobertura.renderer;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.util.DateUtil;

public class TableCellConfirmarPrenhesHyperlinkFactory<S, LocalDate> implements Callback<TableColumn<S ,LocalDate>, TableCell<S, LocalDate>>{
	
	private Function<Integer, Boolean> confirmarPrenhesFunction;
	private String property;
	
	public TableCellConfirmarPrenhesHyperlinkFactory(String property, Function<Integer, Boolean> confirmarPrenhesFunction) {
		this.confirmarPrenhesFunction = confirmarPrenhesFunction;
		this.property = property;
	}

	@Override
	public TableCell<S, LocalDate> call(TableColumn<S, LocalDate> param) {
		
		  TableCell<S, LocalDate> cell = new TableCell<S, LocalDate>() {
			  
		        @Override
		        protected void updateItem(LocalDate item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
		        		super.updateItem(item, empty);
		        		
		        		Cobertura cobertura = (Cobertura) tableViewProperty().get().getItems().get(tableRowProperty().get().getIndex());
		        		if ( cobertura.getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
		        			Label lbl = new Label();
				            if ( (item == null || empty) ) {
				            	lbl.setText("--");			            		
				            } else {
				            	lbl.setText( DateUtil.format((java.time.LocalDate)item));
							} 
				            setGraphic(lbl);
		        		}else{
		        			Hyperlink hpS = new Hyperlink();
				            if ( (item == null || empty) ) {
				            	hpS.setText("Confirmar");			            		
				            } else {
								hpS.setText( DateUtil.format((java.time.LocalDate)item));
							} 
				            
				            hpS.setFocusTraversable(false);
			            	hpS.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									confirmarPrenhesFunction.apply(tableRowProperty().get().getIndex());
									hpS.setUnderline(false);
									hpS.setVisited(false);
								}
							});
							
				            setGraphic(hpS);
		        		}
		        		
			            
		        	}else{
		        		setText("");
		        		setGraphic(null);
		        	}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    
		    param.setCellValueFactory(new PropertyValueFactory<S, LocalDate>(property));
		    return cell;
		    
	}
	
}
