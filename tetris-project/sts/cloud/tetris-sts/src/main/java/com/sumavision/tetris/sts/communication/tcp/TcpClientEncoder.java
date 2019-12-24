package com.sumavision.tetris.sts.communication.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

/**
 * Created by Lost on 2016/12/28.
 */
@Component
@ChannelHandler.Sharable
public class TcpClientEncoder extends MessageToByteEncoder<String> {

    final byte[] HEADER = {TcpClientUtil.SYN_HEADER , TcpClientUtil.MAJOR_VERSION ,
            TcpClientUtil.MINOR_VERSION , TcpClientUtil.COMPILE_VERSION};

    final int HEADER_LENGTH = (int) TcpClientUtil.SYN_HEADER + (int) TcpClientUtil.MAJOR_VERSION +
            (int) TcpClientUtil.MINOR_VERSION + (int) TcpClientUtil.COMPILE_VERSION;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf out) throws Exception {
//      ByteBuf b = Unpooled.wrappedBuffer(HEADER);
        byte[] data = s.getBytes(CharsetUtil.UTF_8);
        int length = (data.length>>24 & 0xff) + (data.length>>16 & 0xff) + (data.length>>8 & 0xff) + (data.length & 0xff);
        out.writeBytes(HEADER);
        out.writeInt(data.length);
        out.writeInt(length + HEADER_LENGTH);
        out.writeBytes(data);
    }
}
