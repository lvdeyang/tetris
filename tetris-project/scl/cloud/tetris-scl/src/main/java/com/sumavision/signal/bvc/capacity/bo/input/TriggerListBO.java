package com.sumavision.signal.bvc.capacity.bo.input;

public class TriggerListBO {

	private boolean cutoff;
	
	private boolean meida_lost;
	
	private boolean plp_high;

	public boolean isCutoff() {
		return cutoff;
	}

	public TriggerListBO setCutoff(boolean cutoff) {
		this.cutoff = cutoff;
		return this;
	}

	public boolean isMeida_lost() {
		return meida_lost;
	}

	public TriggerListBO setMeida_lost(boolean meida_lost) {
		this.meida_lost = meida_lost;
		return this;
	}

	public boolean isPlp_high() {
		return plp_high;
	}

	public TriggerListBO setPlp_high(boolean plp_high) {
		this.plp_high = plp_high;
		return this;
	}
	
}
