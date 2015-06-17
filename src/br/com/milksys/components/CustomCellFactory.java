package br.com.milksys.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CustomCellFactory<R, P> implements Callback<ListView<R>, ListCell<P>> {

	String methodGet;
	
	public CustomCellFactory(String methodGet) {
		this.methodGet = methodGet;
	}

	@Override
	public ListCell<P> call(ListView<R> param) {
		return new ListCell<P>(){
            protected void updateItem(P item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                	Method method;
					try {
						method = item.getClass().getMethod(methodGet);
						setText((String) method.invoke(item));
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
						setText("");
					}
                }
            }
        };
	}

}
