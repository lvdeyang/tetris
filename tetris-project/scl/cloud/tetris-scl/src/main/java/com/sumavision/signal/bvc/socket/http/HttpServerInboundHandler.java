package com.sumavision.signal.bvc.socket.http;/**
 * Created by Poemafar on 2020/9/23 14:34
 */

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.common.SpringBeanFactory;
import com.sumavision.signal.bvc.feign.FifthGenerationKnapsackFeign;
import com.sumavision.signal.bvc.fifthg.bo.socket.FifthgSocketBO;
import com.sumavision.signal.bvc.socket.ServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONNECTION;
import static io.netty.handler.codec.stomp.StompHeaders.CONTENT_LENGTH;

/**
 * @ClassName: HttpServerInboundHandler
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/23 14:34
 */

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private HttpRequest request;

    private FifthGenerationKnapsackFeign fifthGenerationKnapsackFeign;

    public HttpServerInboundHandler() {
        fifthGenerationKnapsackFeign = SpringBeanFactory.getBean(FifthGenerationKnapsackFeign.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            request.getUri();
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            String temp = buf.toString(io.netty.util.CharsetUtil.UTF_8);
            buf.release();
            LOGGER.info("[收到消息] 消息内容：" + temp.replaceAll("\r|\n|\t",""));

//处理接收到的数据
            JSONObject retObj=new JSONObject();

            if(temp.contains("sn")){
                Pattern pattern =Pattern.compile("\\{([\\s\\S]*)\\}");
                Matcher matcher = pattern.matcher(temp);
                if (matcher.find()){
                    String tempSocket = "{" +matcher.group(1) +"}";
                    //向资源上报设备上线
                    FifthgSocketBO bo = JSONObject.parseObject(tempSocket, FifthgSocketBO.class);
                    BundleDao bundleDao = SpringBeanFactory.getBean(BundleDao.class);
                    BundlePO bundlePO = bundleDao.findByBundleId(bo.getSn());
                    JSONObject portObj = fifthGenerationKnapsackFeign.doRegister(bo.getSn());
                    if (bundlePO != null){
                        bundlePO.setDeviceIp(bo.getIp());
                        bundlePO.setOnlineStatus(BundlePO.ONLINE_STATUS.ONLINE);
                        bundlePO.setBundleName("5G");
                        bundleDao.save(bundlePO);
                    }
                    retObj.put("data", portObj.getInteger("data"));
                    retObj.put("status", portObj.getInteger("status"));
                }
            }

            String res =retObj.toJSONString();
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                    OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
//            response.headers().set(CONTENT_TYPE, "text/plain");
//            response.headers().set(CONTENT_LENGTH,
//                    response.content().readableBytes());
//            if (HttpHeaders.isKeepAlive(request)) {
//                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//            }
            ctx.write(response);
            ctx.flush();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(cause.getMessage());
        ctx.close();
    }

}
