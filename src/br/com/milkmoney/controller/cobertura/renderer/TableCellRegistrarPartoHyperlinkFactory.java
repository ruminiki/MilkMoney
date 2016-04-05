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

public class TableCellRegistrarPartoHyperlinkFactory<S, LocalDate> implements Callback<TableColumn<S ,LocalDate>, TableCell<S, LocalDate>>{
	
	private Function<Integer, Boolean> registrarPartoFunction;
	private String property;
	
	public TableCellRegistrarPartoHyperlinkFactory(String property, Function<Integer, Boolean> confirmarPrenhezFunction) {
		this.registrarPartoFunction = confirmarPrenhezFunction;
		this.property = property;
	}

	@Override
	public TableCell<S, LocalDate> call(TableColumn<S, LocalDate> param) {
		
		  TableCell<S, LocalDate> cell = new TableCell<S, LocalDate>() {
			  
		        @Override
		        protected void updateItem(LocalDate item, boolean empty) {
		        	if ( tableRowProperty().getValue() != null && tableRowProperty().getValue().getItem() != null ){
		        		super.updateItem(item, empty);
		        		
		        		Cobertura cobertura = (Cobertura) tableViewProperty().get().getItems().get(tableRowProperty().get().getIndex());
		        		if ( cobertura.getSituacaoCobertura().matches(SituacaoCobertura.VAZIA + "|" + SituacaoCobertura.ABORTADA) ){
		        			Label lbl = new Label();
				            lbl.setText("--");			            		
				            setGraphic(lbl);
		        		}else{
		        			Hyperlink hpS = new Hyperlink();
				            if ( (item == null || empty) ) {
				            	hpS.setText("Registrar");
				            } else {
				            	hpS.setText( DateUtil.format((java.util.Date)item));
							} 
				            hpS.setFocusTraversable(false);
							hpS.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									registrarPartoFunction.apply(tableRowProperty().get().getIndex());
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
