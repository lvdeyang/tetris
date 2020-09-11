package com.sumavision.tetris.guide.BO;

import java.util.List;

public class UdpTsBO {
	
	private String ip;
	
	private int port;
	
	private String local_ip;
	
	private List<ProgramArrayBO> program_array;

	public String getIp() {
		return ip;
	}

	public UdpTsBO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public int getPort() {
		return port;
	}

	public UdpTsBO setPort(int port) {
		this.port = port;
		return this;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public UdpTsBO setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
		return this;
	}

	public List<ProgramArrayBO> getProgram_array() {
		return program_array;
	}

	public UdpTsBO setProgram_array(List<ProgramArrayBO> program_array) {
		this.program_array = program_array;
		return this;
	}
	
}
