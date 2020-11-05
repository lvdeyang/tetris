package com.sumavision.tetris.business.common.enumeration;/**
 * Created by Poemafar on 2020/9/18 15:52
 */

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

/**
 * @ClassName: ProtocolType
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/18 15:52
 */
public enum ProtocolType {
    UDP_TS,RTP_TS,SRT_TS,HTTP_TS,HTTP_FLV,MSS,RTSP,ZIXI,RTMP,DASH,HLS,SDI,FILE;
    public static ProtocolType getProtocolType(String type) throws BaseException {
        switch (type){
            case "udp":
            case "udp_ts":
                return UDP_TS;
            case "srt":
            case "srt_ts":
                return SRT_TS;
            case "rtmp":
            case "rtmp_flv":
                return RTMP;
            case "file":
                return FILE;
        }
        throw new BaseException(StatusCode.FORBIDDEN,"unknown protocol type:"+type);
    }
}
