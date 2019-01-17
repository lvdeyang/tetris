package com.sumavision.tetris.easy.process.core;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.easy.process.access.point.AccessPointPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointType;

public class EntryVO {

	private Long id;
	
	private String uuid;
	
	private String name;
	
	private String expression;
	
	private boolean removeable;
	
	private String type;

	public Long getId() {
		return id;
	}

	public EntryVO setId(Long id) {
		this.id = id;
		return this;
	}
	
	public String getUuid() {
		return uuid;
	}

	public EntryVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public EntryVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getExpression() {
		return expression;
	}

	public EntryVO setExpression(String expression) {
		this.expression = expression;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public EntryVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getType() {
		return type;
	}

	public EntryVO setType(String type) {
		this.type = type;
		return this;
	}
	
	public EntryVO set(AccessPointPO entity, boolean removeable){
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setName(entity.getName())
			.setRemoveable(removeable)
			.setType(entity.getType().toString());
		
		if(entity.getType().equals(AccessPointType.INTERNAL)){
			this.setExpression(entity.getMethod());
		}else if(entity.getType().equals(AccessPointType.REMOTE_ASYNCHRONOUS) ||
				entity.getType().equals(AccessPointType.REMOTE_SYNCHRONOUS)){
			this.setExpression(new StringBufferWrapper().append("remoteAccessPoint.invoke(execution, ")
													    .append(entity.getId())
													    .append(")").toString());
		}
		
		return this;
	}
	
}
