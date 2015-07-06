package br.com.milksys.components.events;

import javafx.event.Event;
import javafx.event.EventType;

public class SelectAfterInsertEvent extends Event {
	
	private Object object;
	private static final long serialVersionUID = 1L;
    
    public SelectAfterInsertEvent(Object arg0, EventType<? extends Event> arg2) {
        super(arg2);
        this.object = arg0;
    }

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}  
    
}