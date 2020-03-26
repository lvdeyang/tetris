package com.sumavision.tetris.omms.demo;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshDemo {

	public static void main(String[] args) throws Exception{
		JSch jSch = new JSch();
		Session session = jSch.getSession("root", "192.165.58.167", 22);
	}
	
}
