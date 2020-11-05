package com.sumavision.tetris.sts.communication.tcp;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by Lost on 2016/12/29.
 */

public class TcpClientDecoder extends LengthFieldBasedFrameDecoder {
    public TcpClientDecoder() {
        super(TcpClientUtil.BUFF_SIZE, 4 , 4 , 4 , TcpClientUtil.HEADER_LENGTH);
    }
}
