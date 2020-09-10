package com.sumavision.tetris.guide.BO;

import java.util.List;

public class UdpTs {
	
	private String ip;
	
	private int port;
	
	private String local_ip;
	
	private List<ProgramArray> program_array;

	public String getIp() {
		return ip;
	}

	public UdpTs setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public int getPort() {
		return port;
	}

	public UdpTs setPort(int port) {
		this.port = port;
		return this;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public UdpTs setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
		return this;
	}

	public List<ProgramArray> getProgram_array() {
		return program_array;
	}

	public UdpTs setProgram_array(List<ProgramArray> program_array) {
		this.program_array = program_array;
		return this;
	}
	
}
