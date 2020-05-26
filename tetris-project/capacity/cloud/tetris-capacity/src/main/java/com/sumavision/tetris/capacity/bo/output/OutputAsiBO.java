package com.sumavision.tetris.capacity.bo.output;

public class OutputAsiBO {

	private Integer card_no;
	
	private Integer port_no;
	
	private Integer sya_rate;
	
	private Integer pkd_per_send;
	
	private boolean rate_control;
	
	private Integer pat_int;
	
	private Integer pmt_int;
	
	private Integer sdt_int;
	
	private Integer pcr_pid;
	
	private boolean pcr_replace;

	public Integer getCard_no() {
		return card_no;
	}

	public void setCard_no(Integer card_no) {
		this.card_no = card_no;
	}

	public Integer getPort_no() {
		return port_no;
	}

	public void setPort_no(Integer port_no) {
		this.port_no = port_no;
	}

	public Integer getSya_rate() {
		return sya_rate;
	}

	public void setSya_rate(Integer sya_rate) {
		this.sya_rate = sya_rate;
	}

	public Integer getPkd_per_send() {
		return pkd_per_send;
	}

	public void setPkd_per_send(Integer pkd_per_send) {
		this.pkd_per_send = pkd_per_send;
	}

	public boolean isRate_control() {
		return rate_control;
	}

	public void setRate_control(boolean rate_control) {
		this.rate_control = rate_control;
	}

	public Integer getPat_int() {
		return pat_int;
	}

	public void setPat_int(Integer pat_int) {
		this.pat_int = pat_int;
	}

	public Integer getPmt_int() {
		return pmt_int;
	}

	public void setPmt_int(Integer pmt_int) {
		this.pmt_int = pmt_int;
	}

	public Integer getSdt_int() {
		return sdt_int;
	}

	public void setSdt_int(Integer sdt_int) {
		this.sdt_int = sdt_int;
	}

	public Integer getPcr_pid() {
		return pcr_pid;
	}

	public void setPcr_pid(Integer pcr_pid) {
		this.pcr_pid = pcr_pid;
	}

	public boolean isPcr_replace() {
		return pcr_replace;
	}

	public void setPcr_replace(boolean pcr_replace) {
		this.pcr_replace = pcr_replace;
	}
	
}
