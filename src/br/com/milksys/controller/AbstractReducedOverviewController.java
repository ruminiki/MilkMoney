package br.com.milksys.controller;

import org.springframework.context.ApplicationListener;

import br.com.milksys.components.events.ActionEvent;
import br.com.milksys.model.AbstractEntity;

public abstract class AbstractReducedOverviewController<K, E> extends AbstractOverviewController<K, E> implements ApplicationListener<ActionEvent>{

	@Override
	public void onApplicationEvent(ActionEvent event) {
		if ( event != null ){
			if ( ( event.getEventType().equals(ActionEvent.EVENT_INSERT) || event.getEventType().equals(ActionEvent.EVENT_UPDATE)) ){
				
				if ( event.getSource().getClass().isInstance(getObject()) ){
					this.setObject((AbstractEntity) event.getSource());
					this.closeForm();	
				}
				
			}
		}
	}
			
}
