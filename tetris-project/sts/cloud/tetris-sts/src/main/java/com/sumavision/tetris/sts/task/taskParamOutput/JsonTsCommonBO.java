package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonTsCommonBO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8385990724950274389L;
	private String ip;
	private Integer port;
	private String local_ip;
	private Integer pcr_int;
	private Integer pat_int;
	private Integer pmt_int;
	private Integer sdt_int;
	private Integer tsid_pid;
	private String rate_ctrl;
	private Integer bitrate;
	private String av_mode;
	private Integer buf_init;
	private String ts_mode;
	private Integer interlace_depth;
	private String ajust_mode;
	private Integer ts_send_cnt;
	private Integer target_cnt;
	private Integer pad_pid;
	private Integer pat_version;
	private Integer cut_allow;
	private String const_output;
	private String send_control;
	private String send_gap_min;
	private ArrayList<OutputProgramBO> program_array;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getLocal_ip() {
		return local_ip;
	}
	public void setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
	}
	public Integer getPcr_int() {
		return pcr_int;
	}
	public void setPcr_int(Integer pcr_int) {
		this.pcr_int = pcr_int;
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
	public Integer getTsid_pid() {
		return tsid_pid;
	}
	public void setTsid_pid(Integer tsid_pid) {
		this.tsid_pid = tsid_pid;
	}
	public String getRate_ctrl() {
		return rate_ctrl;
	}
	public void setRate_ctrl(String rate_ctrl) {
		this.rate_ctrl = rate_ctrl;
	}
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	public String getAv_mode() {
		return av_mode;
	}
	public void setAv_mode(String av_mode) {
		this.av_mode = av_mode;
	}
	public Integer getBuf_init() {
		return buf_init;
	}
	public void setBuf_init(Integer buf_init) {
		this.buf_init = buf_init;
	}
	public String getTs_mode() {
		return ts_mode;
	}
	public void setTs_mode(String ts_mode) {
		this.ts_mode = ts_mode;
	}
	public Integer getInterlace_depth() {
		return interlace_depth;
	}
	public void setInterlace_depth(Integer interlace_depth) {
		this.interlace_depth = interlace_depth;
	}
	public String getAjust_mode() {
		return ajust_mode;
	}
	public void setAjust_mode(String ajust_mode) {
		this.ajust_mode = ajust_mode;
	}
	public Integer getTs_send_cnt() {
		return ts_send_cnt;
	}
	public void setTs_send_cnt(Integer ts_send_cnt) {
		this.ts_send_cnt = ts_send_cnt;
	}
	public Integer getTarget_cnt() {
		return target_cnt;
	}
	public void setTarget_cnt(Integer target_cnt) {
		this.target_cnt = target_cnt;
	}
	public Integer getPad_pid() {
		return pad_pid;
	}
	public void setPad_pid(Integer pad_pid) {
		this.pad_pid = pad_pid;
	}
	public Integer getPat_version() {
		return pat_version;
	}
	public void setPat_version(Integer pat_version) {
		this.pat_version = pat_version;
	}
	public Integer getCut_allow() {
		return cut_allow;
	}
	public void setCut_allow(Integer cut_allow) {
		this.cut_allow = cut_allow;
	}
	public String getConst_output() {
		return const_output;
	}
	public void setConst_output(String const_output) {
		this.const_output = const_output;
	}
	public String getSend_control() {
		return send_control;
	}
	public void setSend_control(String send_control) {
		this.send_control = send_control;
	}
	public String getSend_gap_min() {
		return send_gap_min;
	}
	public void setSend_gap_min(String send_gap_min) {
		this.send_gap_min = send_gap_min;
	}
	public ArrayList<OutputProgramBO> getProgram_array() {
		return program_array;
	}
	public void setProgram_array(ArrayList<OutputProgramBO> program_array) {
		this.program_array = program_array;
	}
	
	
}
