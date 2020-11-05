package com.sumavision.tetris.sts.communication.tcp;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by Lost on 2016/12/28.
 */
@Component
public class TcpClientUtil {

    public static final int HEADER_LENGTH = 12;
    public static final byte SYN_HEADER = 0x0c;
    public static final byte MAJOR_VERSION = 1;
    public static final byte MINOR_VERSION = 0;
    public static final byte COMPILE_VERSION = 0;
    public static final int WRITE_BUFFER_SIZE = 1448 * 10;
    public static final int BUFF_SIZE = 20 * 1024 * 1024;

    public static final int CONNECT_TIME = 2000;
    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;
    public static final int DEFAULT = -1;

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(name = "stringDecoder")
    public StringDecoder stringDecoder() {
        return new StringDecoder();
    }

    @Bean(name = "taskExecutor", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(20);
        taskExecutor.setKeepAliveSeconds(50000);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(500);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
