package com.sumavision.tetris.cs.channel.broad.ability.request;

public class BroadAbilityBroadRequestInputVO {
	/** 前一个源 */
	private BroadAbilityBroadRequestInputPrevVO prev;
	
	/** 后一个源 */
	private BroadAbilityBroadRequestInputPrevVO next;

	public BroadAbilityBroadRequestInputPrevVO getPrev() {
		return prev;
	}

	public BroadAbilityBroadRequestInputVO setPrev(BroadAbilityBroadRequestInputPrevVO prev) {
		this.prev = prev;
		return this;
	}

	public BroadAbilityBroadRequestInputPrevVO getNext() {
		return next;
	}

	public BroadAbilityBroadRequestInputVO setNext(BroadAbilityBroadRequestInputPrevVO next) {
		this.next = next;
		return this;
	}
}
