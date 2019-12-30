package com.sumavision.tetris.sts.communication.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by Lost on 2016/12/28.
 */
@Component
public class TcpClientInitializer extends ChannelInitializer<SocketChannel> {

    private static final int READ_IDEL_TIME_OUT = 10; // 读超时
    private static final int WRITE_IDEL_TIME_OUT = 5;// 写超时
    private static final int ALL_IDEL_TIME_OUT = 10; // 所有超时

    @Autowired
    private StringDecoder stringDecoder;

    @Autowired
    private TcpClientHandler tcpClientHandler;

    @Autowired
    private HeartbeatHandler heartbeatHandler;

    @Autowired
    private TcpClientEncoder tcpClientEncoder;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("ping", new IdleStateHandler(READ_IDEL_TIME_OUT,
                WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
        pipeline.addLast("tcpClientDecoder", new TcpClientDecoder());
        pipeline.addLast("stringDecoder", stringDecoder);
        pipeline.addLast("handler", tcpClientHandler);
        pipeline.addLast("tcpClientEncoder", tcpClientEncoder);
        pipeline.addLast("heart", heartbeatHandler);
    }
}
