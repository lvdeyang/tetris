package com.sumavision.signal.bvc.socket;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MinaSocketConfig {

	@Value("${socket.port}")
	private int port;
	
	@Bean
	public IoAcceptor ioAcceptor() throws Exception{
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.setHandler(new ServerHandler());
		acceptor.getSessionConfig().setReadBufferSize(1024*1024*1024);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 100);
		acceptor.bind(new InetSocketAddress(port));
		System.out.println("服务器在端口：" + port + "已经启动");
		return acceptor;
	}
	
}
