package br.com.milksys.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CustomCellFactory<R, P> implements Callback<ListView<R>, ListCell<P>> {

	String attrToGet;
	
	public CustomCellFactory(String attrToGet) {
		this.attrToGet = attrToGet;
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
                	
    				String methodName;
    				if ( !attrToGet.startsWith("get")){
    					methodName = "get"+attrToGet.substring(0, 1).toUpperCase()+attrToGet.substring(1, attrToGet.length());
    				}else{
    					methodName = attrToGet;
    				}
    				
					try {
						method = item.getClass().getMethod(methodName);
						setText((String) method.invoke(item));
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
						setText("");
					}
                }
            }
        };
	}

}
