package br.com.milksys.components.events;

import javafx.event.Event;
import javafx.event.EventType;

public class SelectAfterInsertEvent extends Event {
	
	private Object object;
    
	private static final long serialVersionUID = 1L;
	public static final EventType<SelectAfterInsertEvent> BUTTON_PRESSED = new EventType<SelectAfterInsertEvent>(ANY, "BUTTON_PRESSED");
    
    public SelectAfterInsertEvent() {
        this(BUTTON_PRESSED);
    }
    
    public SelectAfterInsertEvent(EventType<? extends Event> arg0) {
        super(arg0);
    }
    public SelectAfterInsertEvent(Object arg0, EventType<? extends Event> arg2) {
        super(arg2);
        this.object = arg2;
    }

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}  
    
}