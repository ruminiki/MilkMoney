package br.com.milksys.components.events;

import org.springframework.context.ApplicationEvent;

public class ActionEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	
	public static final String EVENT_INSERT = "EVENT_INSERT";
	public static final String EVENT_REMOVE = "EVENT_REMOVE";
	public static final String EVENT_UPDATE = "EVENT_UPDATE";
	public static final String EVENT_NEW    = "EVENT_NEW";
	
	private String eventType;
	
    public ActionEvent(Object source, String eventType) {
    	super(source);
    	this.eventType = eventType;
    }

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
}