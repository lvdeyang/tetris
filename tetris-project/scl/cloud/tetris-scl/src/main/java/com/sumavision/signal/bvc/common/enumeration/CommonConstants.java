package com.sumavision.signal.bvc.common.enumeration;/**
 * Created by Poemafar on 2020/9/3 9:46
 */

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

import java.util.Locale;

/**
 * @ClassName: CommonConstants
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/3 9:46
 */
public class CommonConstants {

    public enum ProtocolType{
        UDP_TS,RTP_TS,SRT_TS,HTTP_TS,HTTP_FLV,MSS,RTSP,ZIXI,RTMP,DASH,HLS,SDI,FILE;
        public static ProtocolType getProtocolType(String type) throws BaseException {
            switch (type.toLowerCase(Locale.ENGLISH)){
                case "udp":
                case "udp_ts":
                    return UDP_TS;
                case "rtp":
                case "rtp_ts":
                    return RTP_TS;
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

        public static Integer getPackageType(ProtocolType type) throws BaseException {
            switch (type){
                case UDP_TS:
                    return 0;
                case RTP_TS:
                    return 1;
                case SRT_TS:
                    return 2;
                case RTMP:
                    return 3;
            }
            throw new BaseException(StatusCode.FORBIDDEN,"unknown protocol type:"+type.name());
        }

        public static ProtocolType getProtocolTypeByPackage(Integer type) throws BaseException {
            switch (type){
                case 0:
                    return UDP_TS;
                case 1:
                    return RTP_TS;
                case 2:
                    return SRT_TS;
                case 3:
                    return RTMP;
            }
            throw new BaseException(StatusCode.FORBIDDEN,"unknown protocol type:"+type);
        }

    }

    public enum TaskType{
        TRANSCODE, //转码
        STREAM,    //转封装
        PASSBY     //只转发
    }

    public enum SwitchMode{
        TRANSCODE, //转码
        DIRECTOR,  //直接切换，不转码
        FRAME      //按帧切换，不转码
    }

}
