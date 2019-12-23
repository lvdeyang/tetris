package com.sumavision.tetris.sts.communication.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Lost on 2016/12/28.
 */
@Component
public class TcpClientBootstrap {
    @Autowired
    NioEventLoopGroup workerGroup;

    @Autowired
    TcpClientInitializer tcpClientInitializer;

    @Bean(name = "bootstrap")
    public Bootstrap bootstrap() {
        Bootstrap b = new Bootstrap().group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOW_HALF_CLOSURE, false)
                .option(ChannelOption.SO_SNDBUF , TcpClientUtil.WRITE_BUFFER_SIZE)
                .option(ChannelOption.SO_RCVBUF , TcpClientUtil.BUFF_SIZE)
                .option(ChannelOption.TCP_NODELAY , true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TcpClientUtil.CONNECT_TIME)
                .handler(tcpClientInitializer);
        return b;
    }
}
