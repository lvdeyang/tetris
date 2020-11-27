package com.sumavision.signal.bvc.capacity.bo.input;

public class TriggerListBO {

	private boolean cutoff;
	
	private boolean media_lost;
	
	private boolean plp_high;

	public boolean isCutoff() {
		return cutoff;
	}

	public TriggerListBO setCutoff(boolean cutoff) {
		this.cutoff = cutoff;
		return this;
	}

	public boolean isMedia_lost() {
		return media_lost;
	}

	public TriggerListBO setMedia_lost(boolean media_lost) {
		this.media_lost = media_lost;
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
