package com.sumavision.tetris.sts.communication.tcp;

import com.suma.xianrd.communication.transport.CommunicationException;
import com.sumavision.tetris.sts.common.CommonUtil;
import com.sumavision.tetris.sts.common.ErrorCodes;
import com.sumavision.tetris.sts.communication.xml.AckInfo;
import com.sumavision.tetris.sts.communication.xml.MsgInfo;
import com.sumavision.tetris.sts.communication.xml.MsgManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * This is tcpClient manager class.
 * Created by Lost on 2016/12/28.
 */
@Component
public class TcpClientManager {

    static Logger logger = LogManager.getLogger(TcpClientManager.class);

    @Autowired
    private Bootstrap bootstrap;

    @Autowired(required = false)
    private TcpClientCallback callback;



//    @Autowired
//    AlarmService alarmService;

    private int reconnect = 1000;

    private int reconnect_time = 15;

    private int offline = 1000 * 10;

    public static final Long SHORT_TIMEOUT = 5000L;

    public static final Long DEFAULT_TIMEOUT = 30000L;//消息超时时间 yzx edit,origin value is 20s

    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static final ConcurrentMap<String , Integer> connectStatusMap = new ConcurrentHashMap<>();

    public static final LinkedBlockingQueue<String> RECONNECT_QUEUE = new LinkedBlockingQueue<>();

    public static final ConcurrentSkipListSet<String> OFFLINE_SET = new ConcurrentSkipListSet<>();

    //初始化连接
    public void init(String socketAddress){
        connectStatusMap.put(socketAddress , TcpClientUtil.DEFAULT);
        RECONNECT_QUEUE.add(socketAddress);
    }

    //初始化连接
    public void init(Set<String> socketAddress){
        socketAddress.stream().forEach(s -> connectStatusMap.put(s , TcpClientUtil.DEFAULT));
        RECONNECT_QUEUE.addAll(socketAddress);
    }

    public void destroy(){
        connectStatusMap.clear();
        RECONNECT_QUEUE.clear();
        OFFLINE_SET.clear();
        channels.close();
    }

    /**
     * @param socketAddress
     * @return
     */
    public boolean connect(String socketAddress){
        try {
            if(null != socketAddress && !socketAddress.isEmpty()){
                ChannelFuture connectFuture = bootstrap.connect(socketAddress.split(":")[0],
                        Integer.parseInt(socketAddress.split(":")[1])).sync();
                return connectFuture.isSuccess();
            }
            return false;
        } catch (Exception e) {
            logger.error("connect {} error." , socketAddress);
            return false;
        }
    }

    //新建一个连接 Channel
    public Channel newConnect(String socketAddress) throws CommunicationException {
        try {
            if(null != socketAddress && !socketAddress.isEmpty()){
                ChannelFuture connectFuture = bootstrap.connect(socketAddress.split(":")[0],
                        Integer.parseInt(socketAddress.split(":")[1])).sync();
                if(connectFuture.isSuccess()){
                    return connectFuture.channel();
                }
            }
            throw new CommunicationException(socketAddress ,ErrorCodes.CONNECTION_FAILED);
        } catch (Exception e) {
            logger.error("connect {} error." , socketAddress);
            throw new CommunicationException(socketAddress , ErrorCodes.CONNECTION_FAILED, e.getMessage() , e.getCause());
        }
    }

    //发送
    public void sendOnly(MsgInfo msgInfo) throws CommunicationException{
        Channel channel = getChannel(msgInfo.getIp() + ":" + msgInfo.getPort());
        sendOnly(msgInfo , channel);
    }

    public void sendOnly(MsgInfo msgInfo , Channel channel) throws CommunicationException{
        if(null == channel || !channel.isActive()){
            throw new CommunicationException(CommonUtil.getSocketAddress(channel.remoteAddress()) , ErrorCodes.CONNECTION_INTERRUPTION);
        }
        channel.writeAndFlush(msgInfo.getSendXml().asXML());
    }


    public AckInfo sendAndClose(MsgInfo msgInfo) throws CommunicationException{
        Channel channel = newConnect(msgInfo.getSocketAddress());
        return sendAndClose(msgInfo , channel , DEFAULT_TIMEOUT);
    }

    public AckInfo sendAndClose(MsgInfo msgInfo , Channel channel) throws CommunicationException{
        return sendAndClose(msgInfo , channel , DEFAULT_TIMEOUT);
    }

    public AckInfo sendAndClose(MsgInfo msgInfo , Channel channel , Long timeout) throws CommunicationException{
        try {
            AckInfo ackInfo = send(msgInfo , channel , timeout);
            return ackInfo;
        } catch (CommunicationException e) {
            throw e;
        } finally {
            if (null != channel)
            channel.close();
        }
    }

    public AckInfo send(MsgInfo msgInfo)throws CommunicationException{
        return send(msgInfo , DEFAULT_TIMEOUT);
    }

    public AckInfo send(MsgInfo msgInfo , boolean throwAble)throws CommunicationException{
        return send(msgInfo , DEFAULT_TIMEOUT , throwAble);
    }

    public AckInfo send(MsgInfo msgInfo , Channel channel) throws CommunicationException {
        return send(msgInfo , channel , DEFAULT_TIMEOUT);
    }

    public AckInfo send(MsgInfo msgInfo , Long timeout) throws CommunicationException{
        return send(msgInfo , getChannel(msgInfo.getSocketAddress()) , timeout);
    }

    public AckInfo send(MsgInfo msgInfo , Long timeout , boolean throwAble) throws CommunicationException{
        return send(msgInfo , getChannel(msgInfo.getSocketAddress()) , timeout , throwAble);
    }


    public AckInfo send(MsgInfo msgInfo , Channel channel , Long timeout) throws CommunicationException {
        return send( msgInfo ,  channel ,  timeout ,  true );
    }

    public AckInfo send(MsgInfo msgInfo , Channel channel , Long timeout , boolean throwAble) throws CommunicationException {
        LinkedBlockingQueue ackQueue = new LinkedBlockingQueue<AckInfo>(1);
        MsgManager.ackInfoMap.put(msgInfo.getId() , ackQueue);
        try {
            if (null == channel || !channel.isActive()){
                logger.error("send message to {} fail." , msgInfo.getSocketAddress());
//                alarmService.CommAbnormal(msgInfo.getSocketAddress());
                throw new CommunicationException(msgInfo.getSocketAddress() ,
                        ErrorCodes.CONNECTION_INTERRUPTION);
            }
            String sendXml = msgInfo.getSendXml().asXML();
            channel.writeAndFlush(sendXml);
            logger.info("send message to {}.\n{}" , channel.remoteAddress() , sendXml);
            AckInfo ackInfo = (AckInfo) ackQueue.poll(timeout , TimeUnit.MILLISECONDS);
            if (null == ackInfo) {
                logger.error("message {} to {} ack time out." , msgInfo.getId() , channel.remoteAddress());
//                alarmService.CommAbnormal(msgInfo.getSocketAddress());
                throw new CommunicationException(CommonUtil.getSocketAddress(channel.remoteAddress()) ,
                        ErrorCodes.TIME_OUT);
            }
            if (ackInfo.getAck() != 0 && throwAble) {//todo 返回值错误也抛通信异常？？？？
                throw new CommunicationException(CommonUtil.getSocketAddress(channel.remoteAddress()) ,
                       ErrorCodes.RESPONSE_ERROR);
            }
//            alarmService.CommNormal(msgInfo.getSocketAddress());
            return ackInfo;
        } catch (InterruptedException e) {
            logger.error("message {} to {} ack error." , msgInfo.getId() , channel.remoteAddress());
            throw new CommunicationException(CommonUtil.getSocketAddress(channel.remoteAddress()) ,
                    ErrorCodes.TIME_OUT);
        } finally {
            MsgManager.ackInfoMap.remove(msgInfo.getId());
        }
    }

    public boolean reConnect(String socketAddress){
        try {
            if(isActive(socketAddress)){
                //need close ? if connect has removed.
                if (connectStatusMap.replace(socketAddress, TcpClientUtil.DEFAULT, TcpClientUtil.ONLINE) && callback != null)
                    callback.onLine(socketAddress);
                return true;
            }
            ChannelFuture connectFuture = bootstrap.connect(socketAddress.split(":")[0],
                    Integer.parseInt(socketAddress.split(":")[1])).sync();
            connectFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess() && !connectStatusMap.containsKey(socketAddress)) {
                    future.channel().close();
                }
            });
            if(!connectStatusMap.containsKey(socketAddress)){
                return true;
            } else {
                return connectFuture.isSuccess();
            }
        } catch (Exception e) {
            logger.error("connect {} error." , socketAddress);
            return false;
        }
    }

    public void offLine(String socketAddress){
        if(connectStatusMap.replace(socketAddress , TcpClientUtil.ONLINE , TcpClientUtil.OFFLINE) ||
                connectStatusMap.replace(socketAddress , TcpClientUtil.DEFAULT , TcpClientUtil.OFFLINE)){
            OFFLINE_SET.add(socketAddress);
            if(callback != null){
                callback.offLine(socketAddress);
            }
        }
    }

    public void close(String socketAddress){
        if(null != socketAddress && !socketAddress.isEmpty()){
            connectStatusMap.remove(socketAddress);
            OFFLINE_SET.remove(socketAddress);
            Channel channel = getChannel(socketAddress);
            if(null != channel){
                channel.close();
            }
        }
    }

    public boolean isActive(String socketAddress){
        if(null != socketAddress && !socketAddress.isEmpty()){
            for (Channel c_ :
                    channels) {
                if(socketAddress.equals(c_.remoteAddress().toString().substring(1))){
                    return c_.isActive();
                }
            }
        }
        return false;
    }

    private Channel getChannel(String socketAddress){
        if(null != socketAddress && !socketAddress.isEmpty()){
            for (Channel c_ :
                    channels) {
                if(socketAddress.equals(c_.remoteAddress().toString().substring(1))){
                    return c_;
                }
            }
        }
        return null;
    }

    public List<String> getChannels(){
        List<String> list = channels.stream().map(c_ -> c_.remoteAddress().toString()).collect(Collectors.toList());
        return list;
    }


    public void groupSend(String str){
        channels.writeAndFlush(str);
    }

//    public static void close(){
//        channels.close();
//    }

//    public void setReconnect_time(int reconnect_time) {
//        this.reconnect_time = reconnect_time;
//    }

    public void setReconnect(int reconnect) {
        this.reconnect = reconnect;
    }

    public void setOffline(int offline) {
        this.offline = offline;
    }

    public int getReconnect() {
        return reconnect;
    }

    public int getReconnect_time() {
        return reconnect_time;
    }

    public int getOffline() {
        return offline;
    }

}
