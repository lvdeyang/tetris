package com.sumavision.tetris.sts.communication.tcp;

import com.sumavision.tetris.sts.communication.xml.AbstractMsg;
import com.sumavision.tetris.sts.communication.xml.AlarmInfo;
import com.sumavision.tetris.sts.communication.xml.XmlParse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Lost on 2016/12/28.
 */
@Component
@ChannelHandler.Sharable
public class TcpClientHandler extends SimpleChannelInboundHandler<Object> {

    private static Logger logger = LogManager.getLogger(TcpClientHandler.class);

    @Autowired(required = false)
    private TcpClientCallback callback;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
     //   Channel incoming = ctx.channel();
     //   System.out.println("加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
        TcpClientManager.channels.add(incoming);
        if(null != incoming.remoteAddress()){
            String socketAddress = incoming.remoteAddress().toString().substring(1);
            if(TcpClientManager.connectStatusMap.replace(socketAddress , TcpClientUtil.OFFLINE , TcpClientUtil.ONLINE)
                    || TcpClientManager.connectStatusMap.replace(socketAddress , TcpClientUtil.DEFAULT , TcpClientUtil.ONLINE)){
                if(callback != null){
                    callback.onLine(socketAddress);
                }
            }
            logger.info("TcpClient : {} online." , socketAddress);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        if(null != incoming.remoteAddress()){
            String socketAddress = incoming.remoteAddress().toString().substring(1);
            if(TcpClientManager.connectStatusMap.containsKey(socketAddress)){
                TcpClientManager.RECONNECT_QUEUE.offer(socketAddress);
            }
            logger.error("TcpClient : {} offline." , socketAddress);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        if(null != incoming.remoteAddress()){
            String socketAddress = incoming.remoteAddress().toString().substring(1);
            logger.error("TcpClient : {} exception." , socketAddress /*, cause*/);
        }
        // 当出现异常就关闭连接
        //cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        String socketAddress = ctx.channel().remoteAddress().toString().substring(1);
        AbstractMsg msg = XmlParse.parse(o.toString() , socketAddress);
        if(null != callback && null != msg){
            if (msg instanceof AlarmInfo)
                callback.alarm((AlarmInfo)msg);
        }
    }

}
