package com.sumavision.signal.bvc.socket.http; /**
 * Created by Poemafar on 2020/9/23 14:24
 */

import com.sumavision.signal.bvc.feign.FifthGenerationKnapsackFeign;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: http.HttpServer
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/23 14:24
 */
@Component
public class HttpServer implements Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${socket.port}")
    private int port;

    @Autowired
    FifthGenerationKnapsackFeign fifthGenerationKnapsackFeign;

    @PostConstruct
    public void init() throws Exception {
        LOGGER.info("Http Server listening on 9999 ...");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this);
        LOGGER.info("Http Server start on 9999 ...");
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new HttpServerInboundHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }



    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer();
        System.out.println("Http Server listening on 9999 ...");
//        server.start(9999);
    }


}
