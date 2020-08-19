package com.suma.venus.resource.base.bo;

/**
 * 
 * @author lxw
 *
 */
public class NumParamBO {
	
	public NumParamBO() {}
	
	public NumParamBO(int min_value, int max_value, int step) {
		this.min_value = min_value;
		this.max_value = max_value;
		this.step = step;
	}

	private int min_value;
	
	private int max_value;
	
	private int step;

	public int getMin_value() {
		return min_value;
	}

	public void setMin_value(int min_value) {
		this.min_value = min_value;
	}

	public int getMax_value() {
		return max_value;
	}

	public void setMax_value(int max_value) {
		this.max_value = max_value;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
	
}
