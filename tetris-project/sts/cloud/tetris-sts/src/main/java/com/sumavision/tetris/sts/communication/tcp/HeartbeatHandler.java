package com.sumavision.tetris.sts.communication.tcp;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Lost on 2017/10/18.
 */
@Component
@ChannelHandler.Sharable
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final String HEARTBEAT_SEQUENCE = "<message id='0' cmd='salt'></message>";

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String type = "";
            if (event.state() == IdleState.READER_IDLE) {
//                System.out.println("read idle");
                ctx.channel().close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
//                System.out.println("write idle");
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else if (event.state() == IdleState.ALL_IDLE) {
                type = "all idle";
            }

//            ctx.writeAndFlush(HEARTBEAT_SEQUENCE).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
//            System.out.println( new Date().toString() + ctx.channel().remoteAddress() + "超时类型：" + type);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
