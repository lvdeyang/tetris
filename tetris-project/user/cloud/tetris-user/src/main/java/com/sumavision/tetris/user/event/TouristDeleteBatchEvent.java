package com.sumavision.tetris.user.event;

import java.util.Collection;

import org.springframework.context.ApplicationEvent;

public class TouristDeleteBatchEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;

	private Collection<Long> touristIds;
	
	public TouristDeleteBatchEvent(
			Object source,
			Collection<Long> touristIds) {
		super(source);
		this.touristIds = touristIds;
	}

}
