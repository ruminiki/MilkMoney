package br.com.milkmoney.controller.animal.renderer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import org.springframework.stereotype.Component;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.confirmacaoPrenhez.ConfirmacaoPrenhezEmLoteFormController;
import br.com.milkmoney.controller.lote.MovimentacaoAnimalLoteFormController;
import br.com.milkmoney.model.Animal;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;

@Component
public class AnimalTableUtils {

	/**
	 * Make table menu button visible and replace the context menu with a custom context menu via reflection.
	 * The preferred height is modified so that an empty header row remains visible. This is needed in case you remove all columns, so that the menu button won't disappear with the row header.
	 * IMPORTANT: Modification is only possible AFTER the table has been made visible, otherwise you'd get a NullPointerException
	 * @param tableView
	 */
	public static void addCustomTableMenu( TableView<Animal> tableView) {

		// enable table menu
		tableView.setTableMenuButtonVisible(true);
		
		// replace internal mouse listener with custom listener 
		setCustomContextMenu( tableView);

	}
	
	private static void setCustomContextMenu( TableView<Animal> table ) {

		TableViewSkin<?> tableSkin = (TableViewSkin<?>) table.getSkin();

		// get all children of the skin
		ObservableList<Node> children = tableSkin.getChildren();

		// find the TableHeaderRow child
		for (int i = 0; i < children.size(); i++) {

			Node node = children.get(i);

			if (node instanceof TableHeaderRow) {
				
				TableHeaderRow tableHeaderRow = (TableHeaderRow) node;
				
				// setting the preferred height for the table header row
				// if the preferred height isn't set, then the table header would disappear if there are no visible columns
				// and with it the table menu button
				// by setting the preferred height the header will always be visible
				// note: this may need adjustments in case you have different heights in columns (eg when you use grouping)
				//double defaultHeight = tableHeaderRow.getHeight();
				//tableHeaderRow.setPrefHeight(defaultHeight);
				
				for( Node child: tableHeaderRow.getChildren()) {

					// child identified as cornerRegion in TableHeaderRow.java
					if( child.getStyleClass().contains( "show-hide-columns-button")) {
						
						// get the context menu
						ContextMenu columnPopupMenu = createContextMenu( table);
						
						// replace mouse listener
				        child.setOnMousePressed(me -> {
				            // show a popupMenu which lists all columns
				            columnPopupMenu.show(child, Side.BOTTOM, 0, 0);
				            me.consume();
				        });
					}
				}
				
			}
		}
	}
	
	/**
	 * Create a menu with custom items. The important thing is that the menu remains open while you click on the menu items.
	 * @param cm
	 * @param table
	 */
	private static ContextMenu createContextMenu( TableView<Animal> table ) {
		
		ContextMenu cm = new ContextMenu();
		
		// create new context menu
		CustomMenuItem cmi;

		// Movimentar Animais Lotes
		Label showAll = new Label("Movimentar Animais");
		showAll.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				MovimentacaoAnimalLoteFormController movimentacaoAnimalLoteFormController = 
						(MovimentacaoAnimalLoteFormController)MainApp.getBean(MovimentacaoAnimalLoteFormController.class);
				movimentacaoAnimalLoteFormController.setAnimaisSelecionados(table.getSelectionModel().getSelectedItems());
				movimentacaoAnimalLoteFormController.showForm();
			}

		});

		cmi = new CustomMenuItem(showAll);
		cmi.setHideOnClick(false);
		cm.getItems().add(cmi);

		// Confirmar Prenhez
		Label hideAll = new Label("Confirmar Prenhez");
		hideAll.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				ConfirmacaoPrenhezEmLoteFormController confirmacaoPrenhezEmLoteFormController = 
						(ConfirmacaoPrenhezEmLoteFormController)MainApp.getBean(ConfirmacaoPrenhezEmLoteFormController.class);
				confirmacaoPrenhezEmLoteFormController.setAnimaisSelecionados(table.getSelectionModel().getSelectedItems());
				confirmacaoPrenhezEmLoteFormController.showForm();
			}

		});

		cmi = new CustomMenuItem(hideAll);
		cmi.setHideOnClick(false);
		cm.getItems().add(cmi);

		/*cm.getItems().add(new SeparatorMenuItem());

		// menu item for each of the available columns
		for (Object obj : table.getColumns()) {

			TableColumn<?, ?> tableColumn = (TableColumn<?, ?>) obj;

			CheckBox cb = new CheckBox(tableColumn.getText());
			cb.selectedProperty().bindBidirectional(tableColumn.visibleProperty());

			cmi = new CustomMenuItem(cb);
			cmi.setHideOnClick(false);

			cm.getItems().add(cmi);
		}*/
		
		return cm;
	}
}