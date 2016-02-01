package br.com.milkmoney.controller.lactacao.renderer;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import br.com.milkmoney.model.Lactacao;

@SuppressWarnings("hiding")
public class TableCellOpcoesFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Integer, Boolean> encerrarLactacaoFunction;
	private Function<Integer, Boolean> desfazerEncerramentoLactacaoFunction;
	
	public TableCellOpcoesFactory(Function<Integer, Boolean> encerrarLactacaoFunction, Function<Integer, Boolean> desfazerEncerramentoLactacaoFunction) {
		this.encerrarLactacaoFunction = encerrarLactacaoFunction;
		this.desfazerEncerramentoLactacaoFunction = desfazerEncerramentoLactacaoFunction;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
		  TableCell<S, String> cell = new TableCell<S, String>() {
			  
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
		        		if(item!=null){
		        			
		        			Lactacao lactacao = (Lactacao) tableViewProperty().get().getItems().get(tableRowProperty().get().getIndex());
		        			
							HBox cell = new HBox();
							cell.setAlignment(Pos.CENTER);
							cell.setSpacing(1);
							cell.setMaxHeight(10);
							
							if ( lactacao.getDataFim() == null ){
								Button btnSecar = new Button("Encerrar");
								btnSecar.setTooltip(new Tooltip("Encerrar Lactação"));
								btnSecar.setMaxHeight(12);
								btnSecar.setCursor(Cursor.HAND);
								btnSecar.setFocusTraversable(false);
								btnSecar.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
										encerrarLactacaoFunction.apply(tableRowProperty().get().getIndex());
									}
								});
									
								cell.getChildren().add(btnSecar);
							}else{
								Button btnDesfazer = new Button("Reabrir");
								btnDesfazer.setTooltip(new Tooltip("Reabrir Lactação"));
								btnDesfazer.setMaxHeight(12);
								btnDesfazer.setCursor(Cursor.HAND);
								btnDesfazer.setFocusTraversable(false);
								btnDesfazer.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
										desfazerEncerramentoLactacaoFunction.apply(tableRowProperty().get().getIndex());
									}
								});
									
								cell.getChildren().add(btnDesfazer);
							}
							
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
